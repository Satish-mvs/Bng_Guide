package com.bengaluru.guide.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cat_key", unique = true, nullable = false)
    private String key; // e.g., FAMOUS_PLACES, TEMPLES, SHOPPING_MALLS, etc.

    @Column(nullable = false)
    private String name; // e.g., "Famous Places"

    private String kannadaName;
    private String teluguName;
    private String hindiName;

    private String icon; // Emoji / Icon key, e.g. "🏛" or "landmark"
    private Integer displayOrder;
    private String description;

    public Category() {}

    public Category(String key, String name, String kannadaName, String teluguName, String hindiName, String icon, Integer displayOrder, String description) {
        this.key = key;
        this.name = name;
        this.kannadaName = kannadaName;
        this.teluguName = teluguName;
        this.hindiName = hindiName;
        this.icon = icon;
        this.displayOrder = displayOrder;
        this.description = description;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getKannadaName() { return kannadaName; }
    public void setKannadaName(String kannadaName) { this.kannadaName = kannadaName; }

    public String getTeluguName() { return teluguName; }
    public void setTeluguName(String teluguName) { this.teluguName = teluguName; }

    public String getHindiName() { return hindiName; }
    public void setHindiName(String hindiName) { this.hindiName = hindiName; }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

    public Integer getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
