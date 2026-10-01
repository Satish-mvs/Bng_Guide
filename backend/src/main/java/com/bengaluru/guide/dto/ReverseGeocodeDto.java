package com.bengaluru.guide.dto;

public class ReverseGeocodeDto {
    private String areaName;
    private String subArea;
    private String formattedAddress;
    private Double latitude;
    private Double longitude;
    private String city;
    private String state;
    private String landmark;
    private Boolean isInsideBengaluru;

    public ReverseGeocodeDto() {
        this.city = "Bengaluru";
        this.state = "Karnataka";
        this.isInsideBengaluru = true;
    }

    public ReverseGeocodeDto(String areaName, String subArea, String formattedAddress, Double latitude, Double longitude, String landmark, Boolean isInsideBengaluru) {
        this.areaName = areaName;
        this.subArea = subArea;
        this.formattedAddress = formattedAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.landmark = landmark;
        this.city = "Bengaluru";
        this.state = "Karnataka";
        this.isInsideBengaluru = isInsideBengaluru != null ? isInsideBengaluru : true;
    }

    public String getAreaName() { return areaName; }
    public void setAreaName(String areaName) { this.areaName = areaName; }

    public String getSubArea() { return subArea; }
    public void setSubArea(String subArea) { this.subArea = subArea; }

    public String getFormattedAddress() { return formattedAddress; }
    public void setFormattedAddress(String formattedAddress) { this.formattedAddress = formattedAddress; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getLandmark() { return landmark; }
    public void setLandmark(String landmark) { this.landmark = landmark; }

    public Boolean getIsInsideBengaluru() { return isInsideBengaluru; }
    public void setIsInsideBengaluru(Boolean insideBengaluru) { isInsideBengaluru = insideBengaluru; }
}
