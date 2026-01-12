package com.hospital.Models;

public class Medication {
    private int medicationId;
    private String name;
    private String description;

    // Constructors
    public Medication() {}
    public Medication(int medicationId, String name, String description) {
        this.medicationId = medicationId;
        this.name = name;
        this.description = description;
    }

    // Getters and Setters
    public int getMedicationId() { return medicationId; }
    public void setMedicationId(int medicationId) { this.medicationId = medicationId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return "Medication{" +
                "medicationId=" + medicationId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}

