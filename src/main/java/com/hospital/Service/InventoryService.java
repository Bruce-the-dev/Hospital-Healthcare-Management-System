package com.hospital.Service;

import com.hospital.Dao.InventoryDAO;
import com.hospital.Dao.MedicationDAO;
import com.hospital.Models.DTO.InventoryViewDTO;
import com.hospital.Models.Inventory;
import com.hospital.Models.Medication;

import java.time.LocalDateTime;
import java.util.List;

public class InventoryService {

    private final InventoryDAO inventoryDAO = new InventoryDAO();
    private final MedicationService medicationService= new MedicationService ();
    private static final int Low_stock =10;

    // Update stock
    public void updateStock(int medicationId, int quantity) {
//        Inventory inv = new Inventory(medicationId, quantity, LocalDateTime.now());
        inventoryDAO.updateStock(medicationId,quantity);
    }
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
    public List<InventoryViewDTO> getInventoryView() {
        return inventoryDAO.getInventoryWithMedication();
    }
    public List<InventoryViewDTO> getLowStockItems() {
        return getInventoryView().stream()
                .filter(i -> i.getQuantity() <= Low_stock)
                .toList();
    }
    public String getMedicationNameById(int medicationId) {
        Medication med = medicationService.getMedicationById(medicationId);
        return med != null ? med.getName() : "Unknown";
    }

}
