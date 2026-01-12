package com.hospital.Models;

import java.time.LocalDateTime;

public class Inventory {
    private int medicationId; // FK
    private int quantity;
    private LocalDateTime lastUpdated;

    // Constructors
    public Inventory() {}
    public Inventory(int medicationId, int quantity, LocalDateTime lastUpdated) {
        this.medicationId = medicationId;
        this.quantity = quantity;
        this.lastUpdated = lastUpdated;
    }

    // Getters and Setters
    public int getMedicationId() { return medicationId; }
    public void setMedicationId(int medicationId) { this.medicationId = medicationId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }

    @Override
    public String toString() {
        return "Inventory{" +
                "medicationId=" + medicationId +
                ", quantity=" + quantity +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
}
