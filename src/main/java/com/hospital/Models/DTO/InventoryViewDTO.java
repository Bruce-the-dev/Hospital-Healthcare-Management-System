package com.hospital.Models.DTO;

import java.time.LocalDateTime;

public class InventoryViewDTO {
    private int medicationId;
    private String medicationName;
    private int quantity;
    private LocalDateTime lastUpdated;

    public InventoryViewDTO(int medicationId, String medicationName,
                            int quantity, LocalDateTime lastUpdated) {
        this.medicationId = medicationId;
        this.medicationName = medicationName;
        this.quantity = quantity;
        this.lastUpdated = lastUpdated;
    }

    public int getMedicationId() { return medicationId; }
    public String getMedicationName() { return medicationName; }
    public int getQuantity() { return quantity; }
    public LocalDateTime getLastUpdated() { return lastUpdated; }
}
