package com.bengaluru.guide.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "metro_stations")
public class MetroStation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String kannadaName;

    @Column(name = "metro_line", nullable = false)
    private String line; // "PURPLE" or "GREEN" or "YELLOW"

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    private Boolean isInterchange;
    private String interchangeLines; // e.g. "PURPLE,GREEN"
    private Integer sequenceOrder;

    public MetroStation() {
        this.isInterchange = false;
    }

    public MetroStation(String name, String kannadaName, String line, Double latitude, Double longitude, Boolean isInterchange, String interchangeLines, Integer sequenceOrder) {
        this.name = name;
        this.kannadaName = kannadaName;
        this.line = line;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isInterchange = isInterchange != null ? isInterchange : false;
        this.interchangeLines = interchangeLines;
        this.sequenceOrder = sequenceOrder;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getKannadaName() { return kannadaName; }
    public void setKannadaName(String kannadaName) { this.kannadaName = kannadaName; }

    public String getLine() { return line; }
    public void setLine(String line) { this.line = line; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public Boolean getIsInterchange() { return isInterchange; }
    public void setIsInterchange(Boolean isInterchange) { this.isInterchange = isInterchange; }

    public String getInterchangeLines() { return interchangeLines; }
    public void setInterchangeLines(String interchangeLines) { this.interchangeLines = interchangeLines; }

    public Integer getSequenceOrder() { return sequenceOrder; }
    public void setSequenceOrder(Integer sequenceOrder) { this.sequenceOrder = sequenceOrder; }
}
