package com.hospital.Models.DTO;

public class MedicationUsageDTO {
    private String medicationName;
    private int timesUsed;

    public MedicationUsageDTO(String medicationName, int timesUsed) {
        this.medicationName = medicationName;
        this.timesUsed = timesUsed;
    }

    public String getMedicationName() { return medicationName; }
    public int getTimesUsed() { return timesUsed; }
}
