package com.bengaluru.guide.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "bmtc_routes")
public class BmtcRoute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String routeNumber; // e.g. "G-4", "252", "335E", "500D", "KIA-9"

    @Column(nullable = false)
    private String routeName; // e.g. "Kempegowda Bus Station (Majestic) to Brigade Road"

    @Column(nullable = false)
    private String origin;

    @Column(nullable = false)
    private String destination;

    @Column(length = 2000)
    private String viaStops; // Comma separated key stops

    private Integer frequencyMinutes;
    private Double fareMin;
    private Double fareMax;

    public BmtcRoute() {}

    public BmtcRoute(String routeNumber, String routeName, String origin, String destination, String viaStops, Integer frequencyMinutes, Double fareMin, Double fareMax) {
        this.routeNumber = routeNumber;
        this.routeName = routeName;
        this.origin = origin;
        this.destination = destination;
        this.viaStops = viaStops;
        this.frequencyMinutes = frequencyMinutes;
        this.fareMin = fareMin;
        this.fareMax = fareMax;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRouteNumber() { return routeNumber; }
    public void setRouteNumber(String routeNumber) { this.routeNumber = routeNumber; }

    public String getRouteName() { return routeName; }
    public void setRouteName(String routeName) { this.routeName = routeName; }

    public String getOrigin() { return origin; }
    public void setOrigin(String origin) { this.origin = origin; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public String getViaStops() { return viaStops; }
    public void setViaStops(String viaStops) { this.viaStops = viaStops; }

    public Integer getFrequencyMinutes() { return frequencyMinutes; }
    public void setFrequencyMinutes(Integer frequencyMinutes) { this.frequencyMinutes = frequencyMinutes; }

    public Double getFareMin() { return fareMin; }
    public void setFareMin(Double fareMin) { this.fareMin = fareMin; }

    public Double getFareMax() { return fareMax; }
    public void setFareMax(Double fareMax) { this.fareMax = fareMax; }
}
