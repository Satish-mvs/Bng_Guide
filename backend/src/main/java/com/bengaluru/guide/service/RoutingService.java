package com.bengaluru.guide.service;

import com.bengaluru.guide.dto.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoutingService {

    private static final Logger log = LoggerFactory.getLogger(RoutingService.class);

    private final GeocodingService geocodingService;
    private final MetroTransitService metroTransitService;
    private final BmtcTransitService bmtcTransitService;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${app.maps.osrm-routing-url:https://router.project-osrm.org}")
    private String osrmUrl;

    public RoutingService(GeocodingService geocodingService,
                          MetroTransitService metroTransitService,
                          BmtcTransitService bmtcTransitService) {
        this.geocodingService = geocodingService;
        this.metroTransitService = metroTransitService;
        this.bmtcTransitService = bmtcTransitService;
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public DistanceMatrixDto calculateDistanceMatrix(Double fromLat, Double fromLng, Double toLat, Double toLng) {
        if (fromLat == null || fromLng == null || toLat == null || toLng == null) {
            return new DistanceMatrixDto(0.0, 0, 0, 0, 0, 0, 0);
        }

        double straightLineKm = GeocodingService.calculateHaversineKm(fromLat, fromLng, toLat, toLng);
        // Realistic Bangalore road network coefficient (1.3x straight line)
        double roadKm = Math.max(0.1, straightLineKm * 1.32);

        // Speeds: Walk (4.5 km/h), Drive (22-26 km/h with traffic), Auto (20 km/h), Cycle (14 km/h), Bus (16 km/h + wait), Metro (35 km/h)
        int walkMin = Math.max(1, (int) Math.round((roadKm / 4.5) * 60));
        int driveMin = Math.max(2, (int) Math.round((roadKm / 24.0) * 60) + 2);
        int autoMin = Math.max(3, (int) Math.round((roadKm / 20.0) * 60) + 2);
        int cycleMin = Math.max(1, (int) Math.round((roadKm / 14.0) * 60));
        int transitMin = Math.max(8, (int) Math.round((roadKm / 16.0) * 60) + 6);
        int metroMin = Math.max(6, (int) Math.round((roadKm / 32.0) * 60) + 8);

        return new DistanceMatrixDto(roadKm, walkMin, driveMin, transitMin, metroMin, autoMin, cycleMin);
    }

    public RouteComparisonDto compareAllRoutes(Double fromLat, Double fromLng, Double toLat, Double toLng, String destName) {
        ReverseGeocodeDto originArea = geocodingService.reverseGeocode(fromLat, fromLng);
        ReverseGeocodeDto destArea = geocodingService.reverseGeocode(toLat, toLng);

        String destinationTitle = (destName != null && !destName.trim().isEmpty()) ? destName : destArea.getAreaName();
        String originTitle = originArea.getAreaName() != null ? originArea.getAreaName() : "Current Location";

        List<TransportOptionDto> options = new ArrayList<>();

        // 1. Walking Option
        options.add(buildWalkingOption(fromLat, fromLng, toLat, toLng, originTitle, destinationTitle));

        // 2. Bus Option (BMTC)
        options.add(bmtcTransitService.findBmtcBusRoute(fromLat, fromLng, toLat, toLng, originTitle, destinationTitle));

        // 3. Metro Option (Namma Metro)
        options.add(metroTransitService.findMetroRoute(fromLat, fromLng, toLat, toLng));

        // 4. Car / Cab Option
        options.add(buildCarOption(fromLat, fromLng, toLat, toLng, originTitle, destinationTitle));

        // 5. Auto Rickshaw Option
        options.add(buildAutoOption(fromLat, fromLng, toLat, toLng, originTitle, destinationTitle));

        // 6. Cycling Option
        options.add(buildCyclingOption(fromLat, fromLng, toLat, toLng, originTitle, destinationTitle));

        return new RouteComparisonDto(
                originTitle, fromLat, fromLng,
                destinationTitle, toLat, toLng,
                options
        );
    }

    private TransportOptionDto buildWalkingOption(Double fromLat, Double fromLng, Double toLat, Double toLng, String origin, String dest) {
        double straightKm = GeocodingService.calculateHaversineKm(fromLat, fromLng, toLat, toLng);
        double walkKm = Math.max(0.1, straightKm * 1.25);
        int walkMin = Math.max(1, (int) Math.round((walkKm / 4.5) * 60));

        List<RouteStepDto> steps = new ArrayList<>();
        steps.add(new RouteStepDto(
                1, "WALK",
                String.format("Head towards %s along main pedestrian walkways (%.1f km)", dest, walkKm),
                String.format("%s ಕಡೆಗೆ ಕಾಲುದಾರಿಯ ಮೂಲಕ ತೆರಳಿ (%.1f ಕಿ.ಮೀ)", dest, walkKm),
                String.format("%s వైపు కాలినడకన వెళ్ళండి (%.1f కి.మీ)", dest, walkKm),
                String.format("%s की ओर पैदल चलें (%.1f किमी)", dest, walkKm),
                walkKm, walkMin, "🚶", null, origin, dest, 0
        ));

        TransportOptionDto opt = new TransportOptionDto(
                "WALK", "🚶 Walk", "🚶", walkKm, walkMin, "Free (₹ 0)",
                String.format("Pedestrian route from %s to %s", origin, dest),
                true, null
        );
        opt.setSteps(steps);

        List<double[]> coords = new ArrayList<>();
        coords.add(new double[]{fromLat, fromLng});
        coords.add(new double[]{(fromLat + toLat) / 2.0, (fromLng + toLng) / 2.0});
        coords.add(new double[]{toLat, toLng});
        opt.setPathCoordinates(coords);
        return opt;
    }

    private TransportOptionDto buildCarOption(Double fromLat, Double fromLng, Double toLat, Double toLng, String origin, String dest) {
        double straightKm = GeocodingService.calculateHaversineKm(fromLat, fromLng, toLat, toLng);
        double roadKm = Math.max(0.2, straightKm * 1.35);
        int driveMin = Math.max(2, (int) Math.round((roadKm / 24.0) * 60) + 3);

        // Approximate Bengaluru Cab Fare (Base ₹100 + ₹16/km)
        int cabFareMin = (int) Math.round(100 + (roadKm * 15));
        int cabFareMax = (int) Math.round(130 + (roadKm * 20));
        String fareText = String.format("₹ %d - ₹ %d (Cab/Taxi)", cabFareMin, cabFareMax);

        List<RouteStepDto> steps = new ArrayList<>();
        steps.add(new RouteStepDto(
                1, "CAR",
                String.format("Drive via arterial roads from %s towards %s (%.1f km)", origin, dest, roadKm),
                String.format("%s ನಿಂದ %s ಕಡೆಗೆ ಮುಖ್ಯ ರಸ್ತೆಯ ಮೂಲಕ ಚಾಲನೆ ಮಾಡಿ (%.1f ಕಿ.ಮೀ)", origin, dest, roadKm),
                String.format("%s నుండి %s వైపు రోడ్డు ద్వారా డ్రైవ్ చేయండి (%.1f కి.మీ)", origin, dest, roadKm),
                String.format("%s से %s की ओर मुख्य सड़क से ड्राइव करें (%.1f किमी)", origin, dest, roadKm),
                roadKm, driveMin, "🚗", "City Roads", origin, dest, 0
        ));

        TransportOptionDto opt = new TransportOptionDto(
                "CAR", "🚗 Car / Taxi", "🚗", roadKm, driveMin, fareText,
                String.format("Via main roads from %s to %s", origin, dest),
                true, null
        );
        opt.setSteps(steps);

        List<double[]> coords = new ArrayList<>();
        coords.add(new double[]{fromLat, fromLng});
        coords.add(new double[]{fromLat + (toLat - fromLat) * 0.3, fromLng + (toLng - fromLng) * 0.2});
        coords.add(new double[]{fromLat + (toLat - fromLat) * 0.7, fromLng + (toLng - fromLng) * 0.8});
        coords.add(new double[]{toLat, toLng});
        opt.setPathCoordinates(coords);
        return opt;
    }

    private TransportOptionDto buildAutoOption(Double fromLat, Double fromLng, Double toLat, Double toLng, String origin, String dest) {
        double straightKm = GeocodingService.calculateHaversineKm(fromLat, fromLng, toLat, toLng);
        double roadKm = Math.max(0.2, straightKm * 1.35);
        int autoMin = Math.max(3, (int) Math.round((roadKm / 20.0) * 60) + 2);

        // Bengaluru Government Approved Auto Fare: ₹30 for first 2 km, then ₹15 per km
        double autoFare = 30.0;
        if (roadKm > 2.0) {
            autoFare += (roadKm - 2.0) * 15.0;
        }
        int fareApprox = (int) Math.round(autoFare);
        String fareText = String.format("₹ %d - ₹ %d (Auto Meter)", fareApprox, fareApprox + 20);

        List<RouteStepDto> steps = new ArrayList<>();
        steps.add(new RouteStepDto(
                1, "AUTO",
                String.format("Take Auto Rickshaw from %s to %s via local roads", origin, dest),
                String.format("%s ನಿಂದ %s ಗೆ ಆಟೋ ರಿಕ್ಷಾ ಮೂಲಕ ಪ್ರಯಾಣಿಸಿ", origin, dest),
                String.format("%s నుండి %s కి ఆటో రిక్షాలో వెళ్లండి", origin, dest),
                String.format("%s से %s तक ऑटो रिक्शा से जाएं", origin, dest),
                roadKm, autoMin, "🛺", "Auto Rickshaw", origin, dest, 0
        ));

        TransportOptionDto opt = new TransportOptionDto(
                "AUTO", "🛺 Auto Rickshaw", "🛺", roadKm, autoMin, fareText,
                String.format("Metered Auto from %s to %s", origin, dest),
                true, null
        );
        opt.setSteps(steps);

        List<double[]> coords = new ArrayList<>();
        coords.add(new double[]{fromLat, fromLng});
        coords.add(new double[]{toLat, toLng});
        opt.setPathCoordinates(coords);
        return opt;
    }

    private TransportOptionDto buildCyclingOption(Double fromLat, Double fromLng, Double toLat, Double toLng, String origin, String dest) {
        double straightKm = GeocodingService.calculateHaversineKm(fromLat, fromLng, toLat, toLng);
        double roadKm = Math.max(0.1, straightKm * 1.28);
        int cycleMin = Math.max(1, (int) Math.round((roadKm / 14.0) * 60));

        List<RouteStepDto> steps = new ArrayList<>();
        steps.add(new RouteStepDto(
                1, "BICYCLE",
                String.format("Cycle from %s to %s via side roads and cycle tracks (%.1f km)", origin, dest, roadKm),
                String.format("%s ನಿಂದ %s ಗೆ ಸೈಕಲ್ ಮೂಲಕ ತೆರಳಿ (%.1f ಕಿ.ಮೀ)", origin, dest, roadKm),
                String.format("%s నుండి %s కి సైకిల్‌పై వెళ్లండి (%.1f కి.మీ)", origin, dest, roadKm),
                String.format("%s से %s तक साइकिल से जाएं (%.1f किमी)", origin, dest, roadKm),
                roadKm, cycleMin, "🚲", "Bicycle Route", origin, dest, 0
        ));

        TransportOptionDto opt = new TransportOptionDto(
                "BICYCLE", "🚲 Cycling", "🚲", roadKm, cycleMin, "Free (₹ 0)",
                String.format("Eco-friendly cycle route to %s", dest),
                true, null
        );
        opt.setSteps(steps);

        List<double[]> coords = new ArrayList<>();
        coords.add(new double[]{fromLat, fromLng});
        coords.add(new double[]{toLat, toLng});
        opt.setPathCoordinates(coords);
        return opt;
    }
}
