package com.bengaluru.guide.service;

import com.bengaluru.guide.dto.CategoryDto;
import com.bengaluru.guide.dto.DistanceMatrixDto;
import com.bengaluru.guide.dto.PlaceDto;
import com.bengaluru.guide.entity.Category;
import com.bengaluru.guide.entity.Place;
import com.bengaluru.guide.exception.ResourceNotFoundException;
import com.bengaluru.guide.repository.CategoryRepository;
import com.bengaluru.guide.repository.PlaceRepository;
import com.bengaluru.guide.repository.SavedPlaceRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final CategoryRepository categoryRepository;
    private final SavedPlaceRepository savedPlaceRepository;
    private final RoutingService routingService;

    public PlaceService(PlaceRepository placeRepository,
                        CategoryRepository categoryRepository,
                        SavedPlaceRepository savedPlaceRepository,
                        RoutingService routingService) {
        this.placeRepository = placeRepository;
        this.categoryRepository = categoryRepository;
        this.savedPlaceRepository = savedPlaceRepository;
        this.routingService = routingService;
    }

    public List<PlaceDto> getNearbyPlaces(Double userLat, Double userLng, Double radiusKm, String category) {
        double radius = (radiusKm != null && radiusKm > 0) ? radiusKm : 5.0;
        List<Place> places;

        if (category != null && !category.trim().isEmpty() && !category.equalsIgnoreCase("ALL")) {
            places = placeRepository.findByCategoryIgnoreCase(category.trim());
        } else {
            places = placeRepository.findAll();
        }

        Set<Long> savedPlaceIds = getSavedPlaceIds();

        // If user coordinates provided, calculate distance and filter within radius
        if (userLat != null && userLng != null) {
            return places.stream()
                    .map(p -> {
                        PlaceDto dto = PlaceDto.fromEntity(p);
                        dto.setIsSaved(savedPlaceIds.contains(p.getId()));
                        DistanceMatrixDto matrix = routingService.calculateDistanceMatrix(userLat, userLng, p.getLatitude(), p.getLongitude());
                        dto.setDistanceMatrix(matrix);
                        return dto;
                    })
                    .filter(dto -> dto.getDistanceMatrix().getDistanceKm() <= radius)
                    .sorted(Comparator.comparingDouble(a -> a.getDistanceMatrix().getDistanceKm()))
                    .collect(Collectors.toList());
        }

        // Default: return all with dummy/zero distance if user location is not yet ready
        return places.stream()
                .map(p -> {
                    PlaceDto dto = PlaceDto.fromEntity(p);
                    dto.setIsSaved(savedPlaceIds.contains(p.getId()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<PlaceDto> getFamousPlaces(Double userLat, Double userLng, Double radiusKm, String category) {
        List<Place> places;
        if (category != null && !category.trim().isEmpty() && !category.equalsIgnoreCase("ALL")) {
            places = placeRepository.findFamousPlacesByCategory(category.trim());
        } else {
            places = placeRepository.findByIsFamousTrue();
        }

        Set<Long> savedPlaceIds = getSavedPlaceIds();

        return places.stream()
                .map(p -> {
                    PlaceDto dto = PlaceDto.fromEntity(p);
                    dto.setIsSaved(savedPlaceIds.contains(p.getId()));
                    if (userLat != null && userLng != null) {
                        dto.setDistanceMatrix(routingService.calculateDistanceMatrix(userLat, userLng, p.getLatitude(), p.getLongitude()));
                    }
                    return dto;
                })
                .filter(dto -> {
                    if (radiusKm != null && radiusKm > 0 && dto.getDistanceMatrix() != null) {
                        return dto.getDistanceMatrix().getDistanceKm() <= radiusKm;
                    }
                    return true;
                })
                .sorted((a, b) -> {
                    if (a.getDistanceMatrix() != null && b.getDistanceMatrix() != null) {
                        return Double.compare(a.getDistanceMatrix().getDistanceKm(), b.getDistanceMatrix().getDistanceKm());
                    }
                    return 0;
                })
                .collect(Collectors.toList());
    }

    public List<PlaceDto> searchPlaces(String query, Double userLat, Double userLng) {
        if (query == null || query.trim().isEmpty()) {
            return getNearbyPlaces(userLat, userLng, 10.0, null);
        }

        String cleanedQuery = query.trim().toLowerCase()
                .replace("near me", "")
                .replace("nearby", "")
                .replace("places to visit in", "")
                .replace("places in", "")
                .trim();

        if (cleanedQuery.isEmpty()) {
            return getNearbyPlaces(userLat, userLng, 10.0, null);
        }

        List<Place> results = placeRepository.searchPlaces(cleanedQuery);
        Set<Long> savedPlaceIds = getSavedPlaceIds();

        return results.stream()
                .map(p -> {
                    PlaceDto dto = PlaceDto.fromEntity(p);
                    dto.setIsSaved(savedPlaceIds.contains(p.getId()));
                    if (userLat != null && userLng != null) {
                        dto.setDistanceMatrix(routingService.calculateDistanceMatrix(userLat, userLng, p.getLatitude(), p.getLongitude()));
                    }
                    return dto;
                })
                .sorted((a, b) -> {
                    if (a.getDistanceMatrix() != null && b.getDistanceMatrix() != null) {
                        return Double.compare(a.getDistanceMatrix().getDistanceKm(), b.getDistanceMatrix().getDistanceKm());
                    }
                    return 0;
                })
                .collect(Collectors.toList());
    }

    public PlaceDto getPlaceById(Long id, Double userLat, Double userLng) {
        Place place = placeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Place not found with id: " + id));

        PlaceDto dto = PlaceDto.fromEntity(place);
        dto.setIsSaved(savedPlaceRepository.findByPlaceId(id).isPresent());

        if (userLat != null && userLng != null) {
            dto.setDistanceMatrix(routingService.calculateDistanceMatrix(userLat, userLng, place.getLatitude(), place.getLongitude()));
        }
        return dto;
    }

    public List<CategoryDto> getCategories() {
        List<Category> categories = categoryRepository.findAllByOrderByDisplayOrderAsc();
        List<Place> allPlaces = placeRepository.findAll();

        Map<String, Long> countMap = allPlaces.stream()
                .collect(Collectors.groupingBy(Place::getCategory, Collectors.counting()));

        return categories.stream()
                .map(cat -> CategoryDto.fromEntity(cat, countMap.getOrDefault(cat.getKey(), 0L)))
                .collect(Collectors.toList());
    }

    private Set<Long> getSavedPlaceIds() {
        return savedPlaceRepository.findAll().stream()
                .map(s -> s.getPlaceId())
                .collect(Collectors.toSet());
    }
}
