package com.bengaluru.guide.controller;

import com.bengaluru.guide.dto.SavedPlaceRequest;
import com.bengaluru.guide.entity.SavedPlace;
import com.bengaluru.guide.service.SavedPlaceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/saved-places")
@CrossOrigin(origins = "*")
public class SavedPlaceController {

    private final SavedPlaceService savedPlaceService;

    public SavedPlaceController(SavedPlaceService savedPlaceService) {
        this.savedPlaceService = savedPlaceService;
    }

    @GetMapping
    public ResponseEntity<List<SavedPlace>> getAllSavedPlaces() {
        return ResponseEntity.ok(savedPlaceService.getAllSavedPlaces());
    }

    @PostMapping
    public ResponseEntity<SavedPlace> savePlace(@Valid @RequestBody SavedPlaceRequest request) {
        SavedPlace saved = savedPlaceService.savePlace(request);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteSavedPlace(@PathVariable Long id) {
        savedPlaceService.deleteSavedPlace(id);
        return ResponseEntity.ok(Map.of("message", "Place removed from bookmarks successfully"));
    }
}
