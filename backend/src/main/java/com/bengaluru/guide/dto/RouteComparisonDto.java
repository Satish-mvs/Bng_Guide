package com.bengaluru.guide.dto;

import java.util.ArrayList;
import java.util.List;

public class RouteComparisonDto {
    private String originName;
    private Double originLat;
    private Double originLng;
    private String destinationName;
    private Double destinationLat;
    private Double destinationLng;
    private List<TransportOptionDto> options = new ArrayList<>();

    public RouteComparisonDto() {}

    public RouteComparisonDto(String originName, Double originLat, Double originLng,
                              String destinationName, Double destinationLat, Double destinationLng,
                              List<TransportOptionDto> options) {
        this.originName = originName;
        this.originLat = originLat;
        this.originLng = originLng;
        this.destinationName = destinationName;
        this.destinationLat = destinationLat;
        this.destinationLng = destinationLng;
        this.options = options;
    }

    public String getOriginName() { return originName; }
    public void setOriginName(String originName) { this.originName = originName; }

    public Double getOriginLat() { return originLat; }
    public void setOriginLat(Double originLat) { this.originLat = originLat; }

    public Double getOriginLng() { return originLng; }
    public void setOriginLng(Double originLng) { this.originLng = originLng; }

    public String getDestinationName() { return destinationName; }
    public void setDestinationName(String destinationName) { this.destinationName = destinationName; }

    public Double getDestinationLat() { return destinationLat; }
    public void setDestinationLat(Double destinationLat) { this.destinationLat = destinationLat; }

    public Double getDestinationLng() { return destinationLng; }
    public void setDestinationLng(Double destinationLng) { this.destinationLng = destinationLng; }

    public List<TransportOptionDto> getOptions() { return options; }
    public void setOptions(List<TransportOptionDto> options) { this.options = options; }
}
