package com.hospital.Service;

import com.hospital.Dao.InventoryDAO;
import com.hospital.Models.Inventory;

import java.time.LocalDateTime;
import java.util.List;

public class InventoryService {

    private final InventoryDAO inventoryDAO = new InventoryDAO();

    // Add stock
    public void addStock(int medicationId, int quantity) {
        Inventory inv = new Inventory(medicationId, quantity, LocalDateTime.now());
        inventoryDAO.addInventory(inv);
    }

    // Check if enough stock exists
    public boolean checkStock(int medicationId, int requiredQuantity) {
        int available = inventoryDAO.getQuantity(medicationId);
        return available >= requiredQuantity;
    }

    // Deduct stock
    public boolean deductStock(int medicationId, int quantity) {
        return inventoryDAO.reduceStock(medicationId, quantity);
    }

    // Get current stock
    public int getStock(int medicationId) {
        return inventoryDAO.getQuantity(medicationId);
    }

    // Fetch all inventory items (for reporting)
    public List<Inventory> getAllInventory() {
        return inventoryDAO.getAllInventory();
    }
}
