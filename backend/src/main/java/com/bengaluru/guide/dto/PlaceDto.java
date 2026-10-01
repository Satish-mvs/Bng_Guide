package com.bengaluru.guide.dto;

import com.bengaluru.guide.entity.Place;

public class PlaceDto {
    private Long id;
    private String name;
    private String kannadaName;
    private String teluguName;
    private String hindiName;
    private String category;
    private String categoryLabel;
    private String subCategory;
    private Double latitude;
    private Double longitude;
    private String address;
    private String area;
    private String description;
    private Double rating;
    private Integer reviewCount;
    private String openingHours;
    private String phone;
    private String website;
    private String imageUrl;
    private Boolean isFamous;
    private String tags;
    private String popularFor;

    // Dynamic calculated distances from user coordinates
    private DistanceMatrixDto distanceMatrix;
    private Boolean isSaved;

    public PlaceDto() {}

    public static PlaceDto fromEntity(Place place) {
        PlaceDto dto = new PlaceDto();
        dto.setId(place.getId());
        dto.setName(place.getName());
        dto.setKannadaName(place.getKannadaName());
        dto.setTeluguName(place.getTeluguName());
        dto.setHindiName(place.getHindiName());
        dto.setCategory(place.getCategory());
        dto.setCategoryLabel(formatCategory(place.getCategory()));
        dto.setSubCategory(place.getSubCategory());
        dto.setLatitude(place.getLatitude());
        dto.setLongitude(place.getLongitude());
        dto.setAddress(place.getAddress());
        dto.setArea(place.getArea());
        dto.setDescription(place.getDescription());
        dto.setRating(place.getRating());
        dto.setReviewCount(place.getReviewCount());
        dto.setOpeningHours(place.getOpeningHours());
        dto.setPhone(place.getPhone());
        dto.setWebsite(place.getWebsite());
        dto.setImageUrl(place.getImageUrl());
        dto.setIsFamous(place.getIsFamous());
        dto.setTags(place.getTags());
        dto.setPopularFor(place.getPopularFor());
        dto.setIsSaved(false);
        return dto;
    }

    private static String formatCategory(String category) {
        if (category == null) return "General";
        return category.replace("_", " ").toLowerCase();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getKannadaName() { return kannadaName; }
    public void setKannadaName(String kannadaName) { this.kannadaName = kannadaName; }

    public String getTeluguName() { return teluguName; }
    public void setTeluguName(String teluguName) { this.teluguName = teluguName; }

    public String getHindiName() { return hindiName; }
    public void setHindiName(String hindiName) { this.hindiName = hindiName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getCategoryLabel() { return categoryLabel; }
    public void setCategoryLabel(String categoryLabel) { this.categoryLabel = categoryLabel; }

    public String getSubCategory() { return subCategory; }
    public void setSubCategory(String subCategory) { this.subCategory = subCategory; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }

    public Integer getReviewCount() { return reviewCount; }
    public void setReviewCount(Integer reviewCount) { this.reviewCount = reviewCount; }

    public String getOpeningHours() { return openingHours; }
    public void setOpeningHours(String openingHours) { this.openingHours = openingHours; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Boolean getIsFamous() { return isFamous; }
    public void setIsFamous(Boolean famous) { isFamous = famous; }

    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }

    public String getPopularFor() { return popularFor; }
    public void setPopularFor(String popularFor) { this.popularFor = popularFor; }

    public DistanceMatrixDto getDistanceMatrix() { return distanceMatrix; }
    public void setDistanceMatrix(DistanceMatrixDto distanceMatrix) { this.distanceMatrix = distanceMatrix; }

    public Boolean getIsSaved() { return isSaved; }
    public void setIsSaved(Boolean saved) { isSaved = saved; }
}
