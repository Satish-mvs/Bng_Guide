package com.bengaluru.guide.dto;

public class DistanceMatrixDto {
    private Double distanceKm;
    private Integer walkingMinutes;
    private Integer drivingMinutes;
    private Integer transitMinutes;
    private Integer metroMinutes;
    private Integer autoMinutes;
    private Integer cyclingMinutes;
    private String formattedDistance;

    public DistanceMatrixDto() {}

    public DistanceMatrixDto(Double distanceKm, Integer walkingMinutes, Integer drivingMinutes,
                             Integer transitMinutes, Integer metroMinutes, Integer autoMinutes, Integer cyclingMinutes) {
        this.distanceKm = Math.round(distanceKm * 10.0) / 10.0;
        this.walkingMinutes = walkingMinutes;
        this.drivingMinutes = drivingMinutes;
        this.transitMinutes = transitMinutes;
        this.metroMinutes = metroMinutes;
        this.autoMinutes = autoMinutes;
        this.cyclingMinutes = cyclingMinutes;
        this.formattedDistance = formatKm(this.distanceKm);
    }

    private String formatKm(Double km) {
        if (km == null) return "Unknown";
        if (km < 1.0) {
            return Math.round(km * 1000) + " m";
        }
        return String.format("%.1f km", km);
    }

    public Double getDistanceKm() { return distanceKm; }
    public void setDistanceKm(Double distanceKm) {
        this.distanceKm = distanceKm;
        this.formattedDistance = formatKm(distanceKm);
    }

    public Integer getWalkingMinutes() { return walkingMinutes; }
    public void setWalkingMinutes(Integer walkingMinutes) { this.walkingMinutes = walkingMinutes; }

    public Integer getDrivingMinutes() { return drivingMinutes; }
    public void setDrivingMinutes(Integer drivingMinutes) { this.drivingMinutes = drivingMinutes; }

    public Integer getTransitMinutes() { return transitMinutes; }
    public void setTransitMinutes(Integer transitMinutes) { this.transitMinutes = transitMinutes; }

    public Integer getMetroMinutes() { return metroMinutes; }
    public void setMetroMinutes(Integer metroMinutes) { this.metroMinutes = metroMinutes; }

    public Integer getAutoMinutes() { return autoMinutes; }
    public void setAutoMinutes(Integer autoMinutes) { this.autoMinutes = autoMinutes; }

    public Integer getCyclingMinutes() { return cyclingMinutes; }
    public void setCyclingMinutes(Integer cyclingMinutes) { this.cyclingMinutes = cyclingMinutes; }

    public String getFormattedDistance() { return formattedDistance; }
    public void setFormattedDistance(String formattedDistance) { this.formattedDistance = formattedDistance; }
}
