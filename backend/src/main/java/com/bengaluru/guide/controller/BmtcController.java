package com.bengaluru.guide.controller;

import com.bengaluru.guide.dto.TransportOptionDto;
import com.bengaluru.guide.entity.BmtcRoute;
import com.bengaluru.guide.repository.BmtcRouteRepository;
import com.bengaluru.guide.service.BmtcTransitService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bmtc")
@CrossOrigin(origins = "*")
public class BmtcController {

    private final BmtcRouteRepository bmtcRouteRepository;
    private final BmtcTransitService bmtcTransitService;

    public BmtcController(BmtcRouteRepository bmtcRouteRepository, BmtcTransitService bmtcTransitService) {
        this.bmtcRouteRepository = bmtcRouteRepository;
        this.bmtcTransitService = bmtcTransitService;
    }

    @GetMapping("/routes")
    public ResponseEntity<List<BmtcRoute>> getAllRoutes(@RequestParam(required = false) String stop) {
        if (stop != null && !stop.trim().isEmpty()) {
            return ResponseEntity.ok(bmtcRouteRepository.findRoutesPassingStop(stop.trim()));
        }
        return ResponseEntity.ok(bmtcRouteRepository.findAll());
    }

    @GetMapping("/route")
    public ResponseEntity<TransportOptionDto> getBmtcRoute(
            @RequestParam Double fromLat,
            @RequestParam Double fromLng,
            @RequestParam Double toLat,
            @RequestParam Double toLng,
            @RequestParam(required = false) String originArea,
            @RequestParam(required = false) String destArea) {
        TransportOptionDto option = bmtcTransitService.findBmtcBusRoute(fromLat, fromLng, toLat, toLng, originArea, destArea);
        return ResponseEntity.ok(option);
    }
}
