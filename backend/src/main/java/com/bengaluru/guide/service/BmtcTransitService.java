package com.bengaluru.guide.service;

import com.bengaluru.guide.dto.RouteStepDto;
import com.bengaluru.guide.dto.TransportOptionDto;
import com.bengaluru.guide.entity.BmtcRoute;
import com.bengaluru.guide.entity.Place;
import com.bengaluru.guide.repository.BmtcRouteRepository;
import com.bengaluru.guide.repository.PlaceRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class BmtcTransitService {

    private final BmtcRouteRepository bmtcRouteRepository;
    private final PlaceRepository placeRepository;

    public BmtcTransitService(BmtcRouteRepository bmtcRouteRepository, PlaceRepository placeRepository) {
        this.bmtcRouteRepository = bmtcRouteRepository;
        this.placeRepository = placeRepository;
    }

    public TransportOptionDto findBmtcBusRoute(Double fromLat, Double fromLng, Double toLat, Double toLng, String originArea, String destArea) {
        List<BmtcRoute> allRoutes = bmtcRouteRepository.findAll();
        if (allRoutes.isEmpty()) {
            return new TransportOptionDto("BUS", "🚌 BMTC Bus", "🚌", 0.0, 0, "N/A", "Bus route information unavailable", false, "No bus routes available");
        }

        // Find closest bus stop / transit place in our database
        List<Place> busStops = placeRepository.findByCategoryIgnoreCase("BUS_STOPS");
        if (busStops.isEmpty()) {
            busStops = placeRepository.findAll(); // fallback to any landmark
        }

        Place nearestOriginStop = busStops.stream()
                .min(Comparator.comparingDouble(p -> GeocodingService.calculateHaversineKm(fromLat, fromLng, p.getLatitude(), p.getLongitude())))
                .orElse(null);

        Place nearestDestStop = busStops.stream()
                .min(Comparator.comparingDouble(p -> GeocodingService.calculateHaversineKm(toLat, toLng, p.getLatitude(), p.getLongitude())))
                .orElse(null);

        String originStopName = nearestOriginStop != null ? nearestOriginStop.getName() : (originArea != null ? originArea : "Majestic Bus Station");
        String destStopName = nearestDestStop != null ? nearestDestStop.getName() : (destArea != null ? destArea : "Destination Bus Stop");

        // Look for matching BMTC route
        BmtcRoute matchedRoute = allRoutes.stream()
                .filter(r -> matchesArea(r, originArea, destArea))
                .findFirst()
                .orElse(allRoutes.get(0)); // default to central Bangalore route if no specific match

        double straightLineKm = GeocodingService.calculateHaversineKm(fromLat, fromLng, toLat, toLng);
        double roadDistanceKm = Math.max(1.0, straightLineKm * 1.35); // realistic road routing factor

        double walkToStopKm = nearestOriginStop != null ?
                GeocodingService.calculateHaversineKm(fromLat, fromLng, nearestOriginStop.getLatitude(), nearestOriginStop.getLongitude()) : 0.3;
        double walkFromStopKm = nearestDestStop != null ?
                GeocodingService.calculateHaversineKm(toLat, toLng, nearestDestStop.getLatitude(), nearestDestStop.getLongitude()) : 0.4;

        int walkToMin = Math.max(1, (int) Math.round((walkToStopKm / 4.5) * 60));
        int busRideMin = Math.max(8, (int) Math.round((roadDistanceKm / 18.0) * 60) + 7); // BMTC average speed in traffic + 7 min wait
        int walkFromMin = Math.max(1, (int) Math.round((walkFromStopKm / 4.5) * 60));

        int totalMinutes = walkToMin + busRideMin + walkFromMin;
        double totalDistanceKm = walkToStopKm + roadDistanceKm + walkFromStopKm;

        String fareText = matchedRoute.getFareMin() != null && matchedRoute.getFareMax() != null ?
                String.format("₹ %.0f - ₹ %.0f", matchedRoute.getFareMin(), matchedRoute.getFareMax()) : "₹ 15 - ₹ 35";

        List<RouteStepDto> steps = new ArrayList<>();
        int stepIdx = 1;

        // Step 1: Walk to nearest bus stop
        steps.add(new RouteStepDto(
                stepIdx++, "WALK",
                String.format("Walk to %s (%.1f km)", originStopName, walkToStopKm),
                String.format("%s ಬಸ್ ನಿಲ್ದಾಣಕ್ಕೆ ನಡೆಯಿರಿ (%.1f ಕಿ.ಮೀ)", originStopName, walkToStopKm),
                String.format("%s బస్ స్టాప్‌కి నడవండి (%.1f కి.మీ)", originStopName, walkToStopKm),
                String.format("%s बस स्टॉप तक चलें (%.1f किमी)", originStopName, walkToStopKm),
                walkToStopKm, walkToMin, "🚶", null, null, originStopName, 0
        ));

        // Step 2: Board BMTC Bus
        String routeInfo = matchedRoute.getRouteNumber() != null ? "Route " + matchedRoute.getRouteNumber() : "BMTC City Bus";
        steps.add(new RouteStepDto(
                stepIdx++, "BUS",
                String.format("Board %s at %s towards %s", routeInfo, originStopName, destStopName),
                String.format("%s ಬಸ್ ಅನ್ನು %s ನಲ್ಲಿ ಹತ್ತಿ %s ಕಡೆಗೆ ಪ್ರಯಾಣಿಸಿ", routeInfo, originStopName, destStopName),
                String.format("%s బస్సును %s వద్ద ఎక్కి %s వైపు వెళ్లండి", routeInfo, originStopName, destStopName),
                String.format("%s बस %s पर चढ़ें और %s की ओर जाएं", routeInfo, originStopName, destStopName),
                roadDistanceKm, busRideMin, "🚌", routeInfo, originStopName, destStopName, Math.max(3, (int) (roadDistanceKm / 0.8))
        ));

        // Step 3: Walk to destination
        steps.add(new RouteStepDto(
                stepIdx, "WALK",
                String.format("Get down at %s and walk to destination (%.1f km)", destStopName, walkFromStopKm),
                String.format("%s ನಲ್ಲಿ ಇಳಿದು ಗಮ್ಯಸ್ಥಾನಕ್ಕೆ ನಡೆಯಿರಿ (%.1f ಕಿ.ಮೀ)", destStopName, walkFromStopKm),
                String.format("%s వద్ద దిగి గమ్యస్థానానికి నడవండి (%.1f కి.మీ)", destStopName, walkFromStopKm),
                String.format("%s पर उतरें और गंतव्य तक चलें (%.1f किमी)", destStopName, walkFromStopKm),
                walkFromStopKm, walkFromMin, "🚶", null, destStopName, null, 0
        ));

        String summary = String.format("%s → %s (%s) → %s", originStopName, routeInfo, matchedRoute.getRouteName(), destStopName);

        TransportOptionDto option = new TransportOptionDto(
                "BUS", "🚌 BMTC Bus", "🚌", totalDistanceKm, totalMinutes, fareText, summary, true, null
        );
        option.setSteps(steps);

        List<double[]> coords = new ArrayList<>();
        coords.add(new double[]{fromLat, fromLng});
        if (nearestOriginStop != null) {
            coords.add(new double[]{nearestOriginStop.getLatitude(), nearestOriginStop.getLongitude()});
        }
        if (nearestDestStop != null) {
            coords.add(new double[]{nearestDestStop.getLatitude(), nearestDestStop.getLongitude()});
        }
        coords.add(new double[]{toLat, toLng});
        option.setPathCoordinates(coords);

        return option;
    }

    private boolean matchesArea(BmtcRoute r, String origin, String dest) {
        if (origin == null && dest == null) return false;
        String fullText = (r.getOrigin() + " " + r.getDestination() + " " + r.getViaStops() + " " + r.getRouteName()).toLowerCase();
        boolean matchesOrigin = origin != null && fullText.contains(origin.toLowerCase());
        boolean matchesDest = dest != null && fullText.contains(dest.toLowerCase());
        return matchesOrigin || matchesDest;
    }
}
