package com.bengaluru.guide.service;

import com.bengaluru.guide.dto.SavedPlaceRequest;
import com.bengaluru.guide.entity.Place;
import com.bengaluru.guide.entity.SavedPlace;
import com.bengaluru.guide.exception.ResourceNotFoundException;
import com.bengaluru.guide.repository.PlaceRepository;
import com.bengaluru.guide.repository.SavedPlaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SavedPlaceService {

    private final SavedPlaceRepository savedPlaceRepository;
    private final PlaceRepository placeRepository;

    public SavedPlaceService(SavedPlaceRepository savedPlaceRepository, PlaceRepository placeRepository) {
        this.savedPlaceRepository = savedPlaceRepository;
        this.placeRepository = placeRepository;
    }

    public List<SavedPlace> getAllSavedPlaces() {
        return savedPlaceRepository.findAllByOrderBySavedAtDesc();
    }

    public SavedPlace savePlace(SavedPlaceRequest request) {
        Place place = placeRepository.findById(request.getPlaceId())
                .orElseThrow(() -> new ResourceNotFoundException("Cannot bookmark non-existent place: " + request.getPlaceId()));

        Optional<SavedPlace> existing = savedPlaceRepository.findByPlaceId(request.getPlaceId());
        if (existing.isPresent()) {
            SavedPlace saved = existing.get();
            if (request.getNotes() != null) {
                saved.setNotes(request.getNotes());
            }
            return savedPlaceRepository.save(saved);
        }

        SavedPlace newSaved = new SavedPlace(
                place.getId(),
                place.getName(),
                place.getCategory(),
                place.getAddress(),
                place.getLatitude(),
                place.getLongitude(),
                place.getImageUrl(),
                request.getNotes()
        );
        return savedPlaceRepository.save(newSaved);
    }

    @Transactional
    public void deleteSavedPlace(Long id) {
        if (savedPlaceRepository.existsById(id)) {
            savedPlaceRepository.deleteById(id);
        } else {
            savedPlaceRepository.deleteByPlaceId(id);
        }
    }
}
