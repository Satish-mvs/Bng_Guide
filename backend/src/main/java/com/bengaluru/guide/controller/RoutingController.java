package com.bengaluru.guide.controller;

import com.bengaluru.guide.dto.DistanceMatrixDto;
import com.bengaluru.guide.dto.ReverseGeocodeDto;
import com.bengaluru.guide.dto.RouteComparisonDto;
import com.bengaluru.guide.service.GeocodingService;
import com.bengaluru.guide.service.RoutingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class RoutingController {

    private final RoutingService routingService;
    private final GeocodingService geocodingService;

    public RoutingController(RoutingService routingService, GeocodingService geocodingService) {
        this.routingService = routingService;
        this.geocodingService = geocodingService;
    }

    @GetMapping("/transport-options")
    public ResponseEntity<RouteComparisonDto> getTransportOptions(
            @RequestParam Double fromLat,
            @RequestParam Double fromLng,
            @RequestParam Double toLat,
            @RequestParam Double toLng,
            @RequestParam(required = false) String destName) {
        RouteComparisonDto comparison = routingService.compareAllRoutes(fromLat, fromLng, toLat, toLng, destName);
        return ResponseEntity.ok(comparison);
    }

    @GetMapping("/directions")
    public ResponseEntity<RouteComparisonDto> getDirections(
            @RequestParam Double fromLat,
            @RequestParam Double fromLng,
            @RequestParam Double toLat,
            @RequestParam Double toLng,
            @RequestParam(required = false) String mode,
            @RequestParam(required = false) String destName) {
        RouteComparisonDto comparison = routingService.compareAllRoutes(fromLat, fromLng, toLat, toLng, destName);
        return ResponseEntity.ok(comparison);
    }

    @GetMapping("/distance-matrix")
    public ResponseEntity<DistanceMatrixDto> getDistanceMatrix(
            @RequestParam Double fromLat,
            @RequestParam Double fromLng,
            @RequestParam Double toLat,
            @RequestParam Double toLng) {
        DistanceMatrixDto matrix = routingService.calculateDistanceMatrix(fromLat, fromLng, toLat, toLng);
        return ResponseEntity.ok(matrix);
    }

    @GetMapping("/location/reverse")
    public ResponseEntity<ReverseGeocodeDto> reverseGeocode(
            @RequestParam Double lat,
            @RequestParam Double lng) {
        ReverseGeocodeDto result = geocodingService.reverseGeocode(lat, lng);
        return ResponseEntity.ok(result);
    }
}
