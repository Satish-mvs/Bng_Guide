package com.bengaluru.guide.service;

import com.bengaluru.guide.dto.ReverseGeocodeDto;
import com.bengaluru.guide.entity.Place;
import com.bengaluru.guide.repository.PlaceRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Comparator;
import java.util.List;

@Service
public class GeocodingService {

    private static final Logger log = LoggerFactory.getLogger(GeocodingService.class);

    private final PlaceRepository placeRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${app.maps.nominatim-url:https://nominatim.openstreetmap.org}")
    private String nominatimUrl;

    @Value("${app.maps.google-api-key:}")
    private String googleApiKey;

    public GeocodingService(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public ReverseGeocodeDto reverseGeocode(Double lat, Double lng) {
        if (lat == null || lng == null) {
            return new ReverseGeocodeDto("Majestic", "Kempegowda Bus Station", "Majestic, Bengaluru, Karnataka 560009", 12.9767, 77.5713, "Kempegowda Bus Station", true);
        }

        // 1. Try reverse geocoding via Nominatim with user-agent
        try {
            String url = nominatimUrl + "/reverse?format=json&lat=" + lat + "&lon=" + lng + "&zoom=18&addressdetails=1";
            var headers = new org.springframework.http.HttpHeaders();
            headers.set("User-Agent", "BengaluruGuideMobileApp/1.0");
            var entity = new org.springframework.http.HttpEntity<>(headers);
            var response = restTemplate.exchange(url, org.springframework.http.HttpMethod.GET, entity, String.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                JsonNode root = objectMapper.readTree(response.getBody());
                JsonNode addressNode = root.path("address");

                String suburb = addressNode.path("suburb").asText("");
                String neighbourhood = addressNode.path("neighbourhood").asText("");
                String road = addressNode.path("road").asText("");
                String city = addressNode.path("city").asText(addressNode.path("town").asText("Bengaluru"));
                String state = addressNode.path("state").asText("Karnataka");
                String displayName = root.path("display_name").asText("");

                String areaName = !suburb.isEmpty() ? suburb : (!neighbourhood.isEmpty() ? neighbourhood : (!road.isEmpty() ? road : "Bengaluru"));
                String subArea = !neighbourhood.isEmpty() && !neighbourhood.equals(areaName) ? neighbourhood : (!road.isEmpty() ? road : "");

                return new ReverseGeocodeDto(areaName, subArea, displayName, lat, lng, areaName, true);
            }
        } catch (Exception e) {
            log.warn("External reverse geocoding call failed, falling back to local POI spatial matching: {}", e.getMessage());
        }

        // 2. Spatial Landmark Resolution Fallback from rich local POI dataset
        List<Place> allPlaces = placeRepository.findAll();
        Place closest = allPlaces.stream()
                .min(Comparator.comparingDouble(p -> calculateHaversineKm(lat, lng, p.getLatitude(), p.getLongitude())))
                .orElse(null);

        if (closest != null) {
            double distanceKm = calculateHaversineKm(lat, lng, closest.getLatitude(), closest.getLongitude());
            String areaName = closest.getArea() != null ? closest.getArea() : closest.getName();
            String address = closest.getAddress() != null ? closest.getAddress() : areaName + ", Bengaluru, Karnataka";

            String subArea = distanceKm < 0.5 ? "Near " + closest.getName() : areaName;
            return new ReverseGeocodeDto(areaName, subArea, address, lat, lng, closest.getName(), true);
        }

        return new ReverseGeocodeDto("Majestic", "Central Bengaluru", "Majestic, Bengaluru, Karnataka", lat, lng, "Kempegowda Bus Station", true);
    }

    public static double calculateHaversineKm(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Earth radius in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
