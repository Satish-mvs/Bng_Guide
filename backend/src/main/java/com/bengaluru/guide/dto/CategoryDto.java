package com.bengaluru.guide.dto;

import com.bengaluru.guide.entity.Category;

public class CategoryDto {
    private Long id;
    private String key;
    private String name;
    private String kannadaName;
    private String teluguName;
    private String hindiName;
    private String icon;
    private Integer displayOrder;
    private String description;
    private Long placeCount;

    public CategoryDto() {}

    public static CategoryDto fromEntity(Category cat, Long count) {
        CategoryDto dto = new CategoryDto();
        dto.setId(cat.getId());
        dto.setKey(cat.getKey());
        dto.setName(cat.getName());
        dto.setKannadaName(cat.getKannadaName());
        dto.setTeluguName(cat.getTeluguName());
        dto.setHindiName(cat.getHindiName());
        dto.setIcon(cat.getIcon());
        dto.setDisplayOrder(cat.getDisplayOrder());
        dto.setDescription(cat.getDescription());
        dto.setPlaceCount(count != null ? count : 0L);
        return dto;
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

    public Long getPlaceCount() { return placeCount; }
    public void setPlaceCount(Long placeCount) { this.placeCount = placeCount; }
}
