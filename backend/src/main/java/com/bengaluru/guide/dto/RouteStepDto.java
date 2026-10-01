package com.bengaluru.guide.dto;

public class RouteStepDto {
    private Integer stepNumber;
    private String mode; // "WALK", "BUS", "METRO", "CAR", "AUTO", "BICYCLE"
    private String instruction;
    private String instructionKn;
    private String instructionTe;
    private String instructionHi;
    private Double distanceKm;
    private Integer durationMinutes;
    private String icon;
    private String lineOrRouteName; // e.g., "Purple Line", "Bus G-4"
    private String departureStop;
    private String arrivalStop;
    private Integer numStops;

    public RouteStepDto() {}

    public RouteStepDto(Integer stepNumber, String mode, String instruction, String instructionKn,
                        String instructionTe, String instructionHi, Double distanceKm,
                        Integer durationMinutes, String icon, String lineOrRouteName,
                        String departureStop, String arrivalStop, Integer numStops) {
        this.stepNumber = stepNumber;
        this.mode = mode;
        this.instruction = instruction;
        this.instructionKn = instructionKn;
        this.instructionTe = instructionTe;
        this.instructionHi = instructionHi;
        this.distanceKm = distanceKm != null ? Math.round(distanceKm * 10.0) / 10.0 : 0.0;
        this.durationMinutes = durationMinutes;
        this.icon = icon;
        this.lineOrRouteName = lineOrRouteName;
        this.departureStop = departureStop;
        this.arrivalStop = arrivalStop;
        this.numStops = numStops;
    }

    public Integer getStepNumber() { return stepNumber; }
    public void setStepNumber(Integer stepNumber) { this.stepNumber = stepNumber; }

    public String getMode() { return mode; }
    public void setMode(String mode) { this.mode = mode; }

    public String getInstruction() { return instruction; }
    public void setInstruction(String instruction) { this.instruction = instruction; }

    public String getInstructionKn() { return instructionKn; }
    public void setInstructionKn(String instructionKn) { this.instructionKn = instructionKn; }

    public String getInstructionTe() { return instructionTe; }
    public void setInstructionTe(String instructionTe) { this.instructionTe = instructionTe; }

    public String getInstructionHi() { return instructionHi; }
    public void setInstructionHi(String instructionHi) { this.instructionHi = instructionHi; }

    public Double getDistanceKm() { return distanceKm; }
    public void setDistanceKm(Double distanceKm) { this.distanceKm = distanceKm; }

    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

    public String getLineOrRouteName() { return lineOrRouteName; }
    public void setLineOrRouteName(String lineOrRouteName) { this.lineOrRouteName = lineOrRouteName; }

    public String getDepartureStop() { return departureStop; }
    public void setDepartureStop(String departureStop) { this.departureStop = departureStop; }

    public String getArrivalStop() { return arrivalStop; }
    public void setArrivalStop(String arrivalStop) { this.arrivalStop = arrivalStop; }

    public Integer getNumStops() { return numStops; }
    public void setNumStops(Integer numStops) { this.numStops = numStops; }
}
