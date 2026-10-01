package com.bengaluru.guide.controller;

import com.bengaluru.guide.dto.CategoryDto;
import com.bengaluru.guide.dto.PlaceDto;
import com.bengaluru.guide.service.PlaceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class PlaceController {

    private final PlaceService placeService;

    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }

    @GetMapping("/places/nearby")
    public ResponseEntity<List<PlaceDto>> getNearbyPlaces(
            @RequestParam(required = false) Double lat,
            @RequestParam(required = false) Double lng,
            @RequestParam(required = false, defaultValue = "5.0") Double radius,
            @RequestParam(required = false) String category) {
        List<PlaceDto> places = placeService.getNearbyPlaces(lat, lng, radius, category);
        return ResponseEntity.ok(places);
    }

    @GetMapping("/places/search")
    public ResponseEntity<List<PlaceDto>> searchPlaces(
            @RequestParam String q,
            @RequestParam(required = false) Double lat,
            @RequestParam(required = false) Double lng) {
        List<PlaceDto> results = placeService.searchPlaces(q, lat, lng);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/places/{id}")
    public ResponseEntity<PlaceDto> getPlaceById(
            @PathVariable Long id,
            @RequestParam(required = false) Double lat,
            @RequestParam(required = false) Double lng) {
        PlaceDto place = placeService.getPlaceById(id, lat, lng);
        return ResponseEntity.ok(place);
    }

    @GetMapping("/places/category/{category}")
    public ResponseEntity<List<PlaceDto>> getPlacesByCategory(
            @PathVariable String category,
            @RequestParam(required = false) Double lat,
            @RequestParam(required = false) Double lng,
            @RequestParam(required = false, defaultValue = "25.0") Double radius) {
        List<PlaceDto> places = placeService.getNearbyPlaces(lat, lng, radius, category);
        return ResponseEntity.ok(places);
    }

    @GetMapping("/famous-places")
    public ResponseEntity<List<PlaceDto>> getFamousPlaces(
            @RequestParam(required = false) Double lat,
            @RequestParam(required = false) Double lng,
            @RequestParam(required = false) Double radius,
            @RequestParam(required = false) String category) {
        List<PlaceDto> famous = placeService.getFamousPlaces(lat, lng, radius, category);
        return ResponseEntity.ok(famous);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDto>> getCategories() {
        List<CategoryDto> categories = placeService.getCategories();
        return ResponseEntity.ok(categories);
    }
}
