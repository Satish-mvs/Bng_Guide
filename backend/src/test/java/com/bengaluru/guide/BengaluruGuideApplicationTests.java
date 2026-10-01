package com.bengaluru.guide;

import com.bengaluru.guide.dto.*;
import com.bengaluru.guide.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BengaluruGuideApplicationTests {

    @Autowired
    private PlaceService placeService;

    @Autowired
    private RoutingService routingService;

    @Autowired
    private GeocodingService geocodingService;

    @Autowired
    private MetroTransitService metroTransitService;

    @Autowired
    private BmtcTransitService bmtcTransitService;

    @Test
    void testCoreUserScenario_MajesticArrival() {
        // User arrives at Majestic / Kempegowda Bus Station (12.9767, 77.5713)
        double majesticLat = 12.9767;
        double majesticLng = 77.5713;

        // 1. Reverse geocoding detects area
        ReverseGeocodeDto area = geocodingService.reverseGeocode(majesticLat, majesticLng);
        assertNotNull(area);
        assertTrue(area.getIsInsideBengaluru());
        assertNotNull(area.getAreaName());

        // 2. Discover nearby places within 5 km default radius
        List<PlaceDto> nearby = placeService.getNearbyPlaces(majesticLat, majesticLng, 5.0, null);
        assertFalse(nearby.isEmpty(), "Should find authentic places near Majestic");

        // 3. Discover famous places near Majestic
        List<PlaceDto> famous = placeService.getFamousPlaces(majesticLat, majesticLng, 10.0, null);
        assertFalse(famous.isEmpty(), "Should find famous places like Bangalore Palace, Vidhana Soudha, ISKCON");

        // Verify Bangalore Palace is in results with distance and travel times
        PlaceDto palace = famous.stream()
                .filter(p -> p.getName().toLowerCase().contains("palace"))
                .findFirst()
                .orElse(null);

        assertNotNull(palace, "Bangalore Palace should be available");
        assertNotNull(palace.getDistanceMatrix());
        assertTrue(palace.getDistanceMatrix().getDistanceKm() > 0.0);
        assertTrue(palace.getDistanceMatrix().getWalkingMinutes() > 0);
        assertTrue(palace.getDistanceMatrix().getDrivingMinutes() > 0);
        assertTrue(palace.getDistanceMatrix().getTransitMinutes() > 0);

        // 4. Test "How to Reach" Bangalore Palace from Majestic
        RouteComparisonDto routeComparison = routingService.compareAllRoutes(
                majesticLat, majesticLng,
                palace.getLatitude(), palace.getLongitude(),
                palace.getName()
        );

        assertNotNull(routeComparison);
        assertEquals(6, routeComparison.getOptions().size(), "Must provide 6 transport options: Walk, Bus, Metro, Car, Auto, Cycle");

        // Verify Walk Option
        TransportOptionDto walkOpt = routeComparison.getOptions().stream().filter(o -> o.getMode().equals("WALK")).findFirst().orElse(null);
        assertNotNull(walkOpt);
        assertFalse(walkOpt.getSteps().isEmpty());

        // Verify Bus Option (BMTC)
        TransportOptionDto busOpt = routeComparison.getOptions().stream().filter(o -> o.getMode().equals("BUS")).findFirst().orElse(null);
        assertNotNull(busOpt);
        assertFalse(busOpt.getSteps().isEmpty());

        // Verify Metro Option (Namma Metro)
        TransportOptionDto metroOpt = routeComparison.getOptions().stream().filter(o -> o.getMode().equals("METRO")).findFirst().orElse(null);
        assertNotNull(metroOpt);
        assertFalse(metroOpt.getSteps().isEmpty());

        // Verify Auto Rickshaw Option with Bengaluru meter pricing
        TransportOptionDto autoOpt = routeComparison.getOptions().stream().filter(o -> o.getMode().equals("AUTO")).findFirst().orElse(null);
        assertNotNull(autoOpt);
        assertTrue(autoOpt.getEstimatedFare().contains("₹"));

        // Verify Car / Taxi Option
        TransportOptionDto carOpt = routeComparison.getOptions().stream().filter(o -> o.getMode().equals("CAR")).findFirst().orElse(null);
        assertNotNull(carOpt);
        assertTrue(carOpt.getEstimatedFare().contains("₹"));
    }

    @Test
    void testNaturalSearch() {
        double majesticLat = 12.9767;
        double majesticLng = 77.5713;

        // Search for "temples near me"
        List<PlaceDto> temples = placeService.searchPlaces("temples near me", majesticLat, majesticLng);
        assertFalse(temples.isEmpty(), "Should find temples in Bengaluru");

        // Search for "restaurants"
        List<PlaceDto> restaurants = placeService.searchPlaces("restaurants", majesticLat, majesticLng);
        assertFalse(restaurants.isEmpty(), "Should find restaurants in Bengaluru");
    }

    @Test
    void testCategoriesWithCounts() {
        List<CategoryDto> categories = placeService.getCategories();
        assertEquals(17, categories.size(), "Should have all 17 specified categories");
        assertTrue(categories.stream().anyMatch(c -> c.getKey().equals("FAMOUS_PLACES")));
        assertTrue(categories.stream().anyMatch(c -> c.getKey().equals("METRO_STATIONS")));
        assertTrue(categories.stream().anyMatch(c -> c.getKey().equals("BUS_STOPS")));
    }
}
