package com.bengaluru.guide.dto;

import jakarta.validation.constraints.NotNull;

public class SavedPlaceRequest {

    @NotNull(message = "placeId is required")
    private Long placeId;

    private String notes;

    public SavedPlaceRequest() {}

    public SavedPlaceRequest(Long placeId, String notes) {
        this.placeId = placeId;
        this.notes = notes;
    }

    public Long getPlaceId() { return placeId; }
    public void setPlaceId(Long placeId) { this.placeId = placeId; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
