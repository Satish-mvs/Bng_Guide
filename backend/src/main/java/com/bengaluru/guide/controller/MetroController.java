package com.bengaluru.guide.controller;

import com.bengaluru.guide.dto.TransportOptionDto;
import com.bengaluru.guide.entity.MetroStation;
import com.bengaluru.guide.repository.MetroStationRepository;
import com.bengaluru.guide.service.MetroTransitService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/metro")
@CrossOrigin(origins = "*")
public class MetroController {

    private final MetroStationRepository metroStationRepository;
    private final MetroTransitService metroTransitService;

    public MetroController(MetroStationRepository metroStationRepository, MetroTransitService metroTransitService) {
        this.metroStationRepository = metroStationRepository;
        this.metroTransitService = metroTransitService;
    }

    @GetMapping("/stations")
    public ResponseEntity<List<MetroStation>> getAllStations(@RequestParam(required = false) String line) {
        if (line != null && !line.trim().isEmpty()) {
            return ResponseEntity.ok(metroStationRepository.findByLineOrderBySequenceOrderAsc(line.toUpperCase()));
        }
        return ResponseEntity.ok(metroStationRepository.findAll());
    }

    @GetMapping("/route")
    public ResponseEntity<TransportOptionDto> getMetroRoute(
            @RequestParam Double fromLat,
            @RequestParam Double fromLng,
            @RequestParam Double toLat,
            @RequestParam Double toLng) {
        TransportOptionDto option = metroTransitService.findMetroRoute(fromLat, fromLng, toLat, toLng);
        return ResponseEntity.ok(option);
    }
}
