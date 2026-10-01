package com.bengaluru.guide.service;

import com.bengaluru.guide.dto.RouteStepDto;
import com.bengaluru.guide.dto.TransportOptionDto;
import com.bengaluru.guide.entity.MetroStation;
import com.bengaluru.guide.repository.MetroStationRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class MetroTransitService {

    private final MetroStationRepository metroStationRepository;

    public MetroTransitService(MetroStationRepository metroStationRepository) {
        this.metroStationRepository = metroStationRepository;
    }

    public TransportOptionDto findMetroRoute(Double fromLat, Double fromLng, Double toLat, Double toLng) {
        List<MetroStation> allStations = metroStationRepository.findAll();
        if (allStations.isEmpty()) {
            return new TransportOptionDto("METRO", "🚇 Namma Metro", "🚇", 0.0, 0, "N/A", "Metro route unavailable", false, "No metro stations in database");
        }

        // Find nearest origin station & destination station
        MetroStation originStation = allStations.stream()
                .min(Comparator.comparingDouble(s -> GeocodingService.calculateHaversineKm(fromLat, fromLng, s.getLatitude(), s.getLongitude())))
                .orElse(null);

        MetroStation destStation = allStations.stream()
                .min(Comparator.comparingDouble(s -> GeocodingService.calculateHaversineKm(toLat, toLng, s.getLatitude(), s.getLongitude())))
                .orElse(null);

        if (originStation == null || destStation == null) {
            return new TransportOptionDto("METRO", "🚇 Namma Metro", "🚇", 0.0, 0, "N/A", "Metro route unavailable", false, "Could not locate nearby stations");
        }

        double walkToOriginKm = GeocodingService.calculateHaversineKm(fromLat, fromLng, originStation.getLatitude(), originStation.getLongitude());
        double walkFromDestKm = GeocodingService.calculateHaversineKm(toLat, toLng, destStation.getLatitude(), destStation.getLongitude());

        // If either station is farther than 3.5 km from the point, metro might be impractical compared to direct bus/cab, but we still calculate and advise
        int walkToOriginMin = (int) Math.round((walkToOriginKm / 4.5) * 60);
        int walkFromDestMin = (int) Math.round((walkFromDestKm / 4.5) * 60);

        List<RouteStepDto> steps = new ArrayList<>();
        int stepIdx = 1;

        // Step 1: Walk to origin metro station
        steps.add(new RouteStepDto(
                stepIdx++, "WALK",
                String.format("Walk to %s Metro Station (%.1f km)", originStation.getName(), walkToOriginKm),
                String.format("%s ಮೆಟ್ರೋ ನಿಲ್ದಾಣಕ್ಕೆ ನಡೆಯಿರಿ (%.1f ಕಿ.ಮೀ)", originStation.getKannadaName() != null ? originStation.getKannadaName() : originStation.getName(), walkToOriginKm),
                String.format("%s మెట్రో స్టేషన్‌కు నడవండి (%.1f కి.మీ)", originStation.getName(), walkToOriginKm),
                String.format("%s मेट्रो स्टेशन तक चलें (%.1f किमी)", originStation.getName(), walkToOriginKm),
                walkToOriginKm, Math.max(1, walkToOriginMin), "🚶", null, null, originStation.getName(), 0
        ));

        int metroTravelMinutes = 0;
        double metroDistanceKm = 0.0;
        int estimatedFare = 15;

        // Check if on same line or requires interchange
        boolean sameLine = originStation.getLine().equalsIgnoreCase(destStation.getLine());

        if (sameLine) {
            int stopCount = Math.abs(originStation.getSequenceOrder() - destStation.getSequenceOrder());
            if (stopCount == 0) {
                // Same station
                stopCount = 1;
            }
            metroTravelMinutes = stopCount * 2 + 4; // ~2 min per station + 4 min waiting
            metroDistanceKm = stopCount * 1.4;
            estimatedFare = Math.min(60, 15 + (stopCount * 5));

            steps.add(new RouteStepDto(
                    stepIdx++, "METRO",
                    String.format("Board %s Line at %s towards %s (%d stations)", originStation.getLine(), originStation.getName(), destStation.getName(), stopCount),
                    String.format("%s ಲೈನ್ ಮೆಟ್ರೋವನ್ನು %s ನಿಂದ %s ಕಡೆಗೆ ಹತ್ತಿರಿ (%d ನಿಲ್ದಾಣಗಳು)", originStation.getLine(), originStation.getName(), destStation.getName(), stopCount),
                    String.format("%s లైన్ మెట్రోను %s వద్ద ఎక్కి %s వైపు వెళ్లండి (%d స్టేషన్లు)", originStation.getLine(), originStation.getName(), destStation.getName(), stopCount),
                    String.format("%s लाइन मेट्रो %s से %s की ओर लें (%d स्टेशन)", originStation.getLine(), originStation.getName(), destStation.getName(), stopCount),
                    metroDistanceKm, metroTravelMinutes, "🚇", originStation.getLine() + " Line", originStation.getName(), destStation.getName(), stopCount
            ));
        } else {
            // Requires interchange at Majestic (Nadaprabhu Kempegowda Station)
            MetroStation interchange = allStations.stream()
                    .filter(s -> Boolean.TRUE.equals(s.getIsInterchange()))
                    .findFirst()
                    .orElse(originStation);

            int stopsToInterchange = Math.abs(originStation.getSequenceOrder() - interchange.getSequenceOrder());
            int stopsFromInterchange = Math.abs(destStation.getSequenceOrder() - interchange.getSequenceOrder());
            int totalStops = Math.max(1, stopsToInterchange + stopsFromInterchange);

            int leg1Min = stopsToInterchange * 2 + 3;
            int interchangeMin = 5; // Walk between Purple & Green platforms at Majestic
            int leg2Min = stopsFromInterchange * 2 + 3;
            metroTravelMinutes = leg1Min + interchangeMin + leg2Min;
            metroDistanceKm = totalStops * 1.4;
            estimatedFare = Math.min(60, 20 + (totalStops * 4));

            steps.add(new RouteStepDto(
                    stepIdx++, "METRO",
                    String.format("Board %s Line from %s to %s (%d stations)", originStation.getLine(), originStation.getName(), interchange.getName(), stopsToInterchange),
                    String.format("%s ಲೈನ್ ಮೂಲಕ %s ನಿಂದ %s ಗೆ ಪ್ರಯಾಣಿಸಿ", originStation.getLine(), originStation.getName(), interchange.getName()),
                    String.format("%s లైన్‌లో %s నుండి %s కి వెళ్లండి", originStation.getLine(), originStation.getName(), interchange.getName()),
                    String.format("%s लाइन से %s से %s तक जाएं", originStation.getLine(), originStation.getName(), interchange.getName()),
                    stopsToInterchange * 1.4, leg1Min, "🚇", originStation.getLine() + " Line", originStation.getName(), interchange.getName(), stopsToInterchange
            ));

            steps.add(new RouteStepDto(
                    stepIdx++, "WALK",
                    String.format("Interchange platforms at %s to %s Line (approx 5 min)", interchange.getName(), destStation.getLine()),
                    String.format("%s ನಲ್ಲಿ %s ಲೈನ್‌ಗೆ ಬದಲಾಯಿಸಿ", interchange.getName(), destStation.getLine()),
                    String.format("%s వద్ద %s లైన్‌కి మారండి", interchange.getName(), destStation.getLine()),
                    String.format("%s पर %s लाइन में बदलें", interchange.getName(), destStation.getLine()),
                    0.2, interchangeMin, "🔄", "Interchange", interchange.getName(), interchange.getName(), 0
            ));

            steps.add(new RouteStepDto(
                    stepIdx++, "METRO",
                    String.format("Take %s Line from %s to %s (%d stations)", destStation.getLine(), interchange.getName(), destStation.getName(), stopsFromInterchange),
                    String.format("%s ಲೈನ್ ಮೂಲಕ %s ನಿಂದ %s ಗೆ ಪ್ರಯಾಣಿಸಿ", destStation.getLine(), interchange.getName(), destStation.getName()),
                    String.format("%s లైన్‌లో %s నుండి %s కి వెళ్లండి", destStation.getLine(), interchange.getName(), destStation.getName()),
                    String.format("%s लाइन से %s से %s तक जाएं", destStation.getLine(), interchange.getName(), destStation.getName()),
                    stopsFromInterchange * 1.4, leg2Min, "🚇", destStation.getLine() + " Line", interchange.getName(), destStation.getName(), stopsFromInterchange
            ));
        }

        // Final step: Walk from destination station to final point
        steps.add(new RouteStepDto(
                stepIdx, "WALK",
                String.format("Exit %s Station and walk to destination (%.1f km)", destStation.getName(), walkFromDestKm),
                String.format("%s ನಿಲ್ದಾಣದಿಂದ ಹೊರಬಂದು ಗಮ್ಯಸ್ಥಾನಕ್ಕೆ ನಡೆಯಿರಿ (%.1f ಕಿ.ಮೀ)", destStation.getKannadaName() != null ? destStation.getKannadaName() : destStation.getName(), walkFromDestKm),
                String.format("%s స్టేషన్ నుండి బయటకు వచ్చి గమ్యస్థానానికి నడవండి (%.1f కి.మీ)", destStation.getName(), walkFromDestKm),
                String.format("%s स्टेशन से बाहर निकलकर गंतव्य तक चलें (%.1f किमी)", destStation.getName(), walkFromDestKm),
                walkFromDestKm, Math.max(1, walkFromDestMin), "🚶", null, destStation.getName(), null, 0
        ));

        double totalDistanceKm = walkToOriginKm + metroDistanceKm + walkFromDestKm;
        int totalDurationMin = walkToOriginMin + metroTravelMinutes + walkFromDestMin;

        String summary = String.format("%s Station → %s Line → %s Station", originStation.getName(), destStation.getLine(), destStation.getName());

        TransportOptionDto option = new TransportOptionDto(
                "METRO", "🚇 Namma Metro", "🚇", totalDistanceKm, totalDurationMin,
                "₹ " + estimatedFare, summary, true, null
        );
        option.setSteps(steps);

        List<double[]> coords = new ArrayList<>();
        coords.add(new double[]{fromLat, fromLng});
        coords.add(new double[]{originStation.getLatitude(), originStation.getLongitude()});
        coords.add(new double[]{destStation.getLatitude(), destStation.getLongitude()});
        coords.add(new double[]{toLat, toLng});
        option.setPathCoordinates(coords);

        return option;
    }
}
