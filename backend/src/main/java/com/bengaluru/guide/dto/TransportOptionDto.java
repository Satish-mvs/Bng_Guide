package com.bengaluru.guide.dto;

import java.util.ArrayList;
import java.util.List;

public class TransportOptionDto {
    private String mode; // "WALK", "BUS", "METRO", "CAR", "AUTO", "BICYCLE"
    private String title; // e.g. "🚶 Walk", "🚌 BMTC Bus", "🚇 Namma Metro"
    private String icon;
    private Double distanceKm;
    private Integer durationMinutes;
    private String formattedDuration;
    private String formattedDistance;
    private String estimatedFare; // e.g., "₹ 20 - ₹ 35" or "N/A"
    private String summary;
    private Boolean isAvailable;
    private String unavailabilityReason;
    private List<RouteStepDto> steps = new ArrayList<>();
    private List<double[]> pathCoordinates = new ArrayList<>();

    public TransportOptionDto() {}

    public TransportOptionDto(String mode, String title, String icon, Double distanceKm,
                              Integer durationMinutes, String estimatedFare, String summary,
                              Boolean isAvailable, String unavailabilityReason) {
        this.mode = mode;
        this.title = title;
        this.icon = icon;
        this.distanceKm = distanceKm != null ? Math.round(distanceKm * 10.0) / 10.0 : 0.0;
        this.durationMinutes = durationMinutes;
        this.formattedDuration = formatDuration(durationMinutes);
        this.formattedDistance = formatDistance(this.distanceKm);
        this.estimatedFare = estimatedFare;
        this.summary = summary;
        this.isAvailable = isAvailable != null ? isAvailable : true;
        this.unavailabilityReason = unavailabilityReason;
    }

    private String formatDuration(Integer minutes) {
        if (minutes == null) return "N/A";
        if (minutes < 60) return minutes + " min";
        int hrs = minutes / 60;
        int remMin = minutes % 60;
        return remMin == 0 ? hrs + " hr" : hrs + " hr " + remMin + " min";
    }

    private String formatDistance(Double km) {
        if (km == null) return "N/A";
        if (km < 1.0) return Math.round(km * 1000) + " m";
        return String.format("%.1f km", km);
    }

    public String getMode() { return mode; }
    public void setMode(String mode) { this.mode = mode; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

    public Double getDistanceKm() { return distanceKm; }
    public void setDistanceKm(Double distanceKm) {
        this.distanceKm = distanceKm;
        this.formattedDistance = formatDistance(distanceKm);
    }

    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
        this.formattedDuration = formatDuration(durationMinutes);
    }

    public String getFormattedDuration() { return formattedDuration; }
    public void setFormattedDuration(String formattedDuration) { this.formattedDuration = formattedDuration; }

    public String getFormattedDistance() { return formattedDistance; }
    public void setFormattedDistance(String formattedDistance) { this.formattedDistance = formattedDistance; }

    public String getEstimatedFare() { return estimatedFare; }
    public void setEstimatedFare(String estimatedFare) { this.estimatedFare = estimatedFare; }

    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }

    public Boolean getIsAvailable() { return isAvailable; }
    public void setIsAvailable(Boolean available) { isAvailable = available; }

    public String getUnavailabilityReason() { return unavailabilityReason; }
    public void setUnavailabilityReason(String unavailabilityReason) { this.unavailabilityReason = unavailabilityReason; }

    public List<RouteStepDto> getSteps() { return steps; }
    public void setSteps(List<RouteStepDto> steps) { this.steps = steps; }

    public List<double[]> getPathCoordinates() { return pathCoordinates; }
    public void setPathCoordinates(List<double[]> pathCoordinates) { this.pathCoordinates = pathCoordinates; }
}
