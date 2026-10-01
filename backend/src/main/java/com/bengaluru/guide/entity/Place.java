package com.bengaluru.guide.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "places")
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String kannadaName;
    private String teluguName;
    private String hindiName;

    @Column(nullable = false)
    private String category; // e.g., FAMOUS_PLACES, TEMPLES, RESTAURANTS, METRO_STATIONS, etc.

    private String subCategory;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(length = 500)
    private String address;

    private String area; // e.g., Majestic, Malleshwaram, Indiranagar, Koramangala, etc.

    @Column(length = 2000)
    private String description;

    private Double rating;
    private Integer reviewCount;

    private String openingHours;
    private String phone;
    private String website;

    @Column(length = 1000)
    private String imageUrl;

    private Boolean isFamous;

    private String tags; // Comma-separated tags, e.g. "heritage,palace,royal,garden"
    private String popularFor;

    private LocalDateTime createdAt;

    public Place() {
        this.createdAt = LocalDateTime.now();
        this.isFamous = false;
        this.rating = 4.5;
        this.reviewCount = 100;
    }

    public Place(String name, String kannadaName, String teluguName, String hindiName,
                 String category, String subCategory, Double latitude, Double longitude,
                 String address, String area, String description, Double rating, Integer reviewCount,
                 String openingHours, String phone, String website, String imageUrl,
                 Boolean isFamous, String tags, String popularFor) {
        this.name = name;
        this.kannadaName = kannadaName;
        this.teluguName = teluguName;
        this.hindiName = hindiName;
        this.category = category;
        this.subCategory = subCategory;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.area = area;
        this.description = description;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.openingHours = openingHours;
        this.phone = phone;
        this.website = website;
        this.imageUrl = imageUrl;
        this.isFamous = isFamous != null ? isFamous : false;
        this.tags = tags;
        this.popularFor = popularFor;
        this.createdAt = LocalDateTime.now();
    }

    public Place(String name, String kannadaName, String teluguName, String hindiName,
                 String category, String subCategory, Double latitude, Double longitude,
                 String address, String area, String description, Double rating, Integer reviewCount,
                 String openingHours, String phone, String website,
                 Boolean isFamous, String tags, String popularFor) {
        this(name, kannadaName, teluguName, hindiName, category, subCategory, latitude, longitude,
             address, area, description, rating, reviewCount, openingHours, phone, website,
             getDefaultImageForCategory(category), isFamous, tags, popularFor);
    }

    private static String getDefaultImageForCategory(String category) {
        if (category == null) return "https://images.unsplash.com/photo-1596176530529-78163a4f7af2?w=800&q=80";
        return switch (category.toUpperCase()) {
            case "TEMPLES" -> "https://images.unsplash.com/photo-1609766857041-ed402ea8069a?w=800&q=80";
            case "RESTAURANTS" -> "https://images.unsplash.com/photo-1668236543090-82eba5ee5976?w=800&q=80";
            case "CAFES" -> "https://images.unsplash.com/photo-1501339847302-ac426a4a7cbb?w=800&q=80";
            case "PARKS" -> "https://images.unsplash.com/photo-1628178873041-39a5840d21e8?w=800&q=80";
            case "SHOPPING_MALLS" -> "https://images.unsplash.com/photo-1519567241046-7f570eee3ce6?w=800&q=80";
            case "HOSPITALS" -> "https://images.unsplash.com/photo-1586773860418-d37222d8fce3?w=800&q=80";
            case "PHARMACIES" -> "https://images.unsplash.com/photo-1584308666744-24d5c474f2ae?w=800&q=80";
            case "BUS_STOPS", "METRO_STATIONS", "RAILWAY_STATIONS" -> "https://images.unsplash.com/photo-1544620347-c4fd4a3d5957?w=800&q=80";
            case "BANKS", "ATMS" -> "https://images.unsplash.com/photo-1501167786227-4cba60f6d58f?w=800&q=80";
            case "MARKETS" -> "https://images.unsplash.com/photo-1555529669-e69e7aa0ba9a?w=800&q=80";
            case "HOTELS" -> "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=800&q=80";
            case "MOVIE_THEATRES" -> "https://images.unsplash.com/photo-1517604931442-7e0c8ed2963c?w=800&q=80";
            default -> "https://images.unsplash.com/photo-1596176530529-78163a4f7af2?w=800&q=80";
        };
    }

    // Getters and Setters
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

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
