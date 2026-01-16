package com.hospital.Dao;

import com.hospital.Models.DTO.InventoryViewDTO;
import com.hospital.Models.Inventory;
import com.hospital.Util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class InventoryDAO {

    // Add new inventory entry
    public void addInventory(Inventory inventory) {
        String sql = "INSERT INTO inventory (medication_id, quantity, last_updated) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            System.out.println(inventory.getMedicationId()+" "+inventory.getQuantity());
            ps.setInt(1, inventory.getMedicationId());
            ps.setInt(2, inventory.getQuantity());
            ps.setTimestamp(3, Timestamp.valueOf(inventory.getLastUpdated()));
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get current stock of a medication
    public int getQuantity(int medicationId) {
        String sql = "SELECT quantity FROM inventory WHERE medication_id = ?";
        int quantity = 0;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, medicationId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    quantity = rs.getInt("quantity");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return quantity;
    }

    // Update stock for a medication
    public void updateStock(int medicationId, int quantity) {
        String sql = "UPDATE inventory SET quantity = ?, last_updated = ? WHERE medication_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, quantity);
            ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(3, medicationId);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Reduce stock safely (used when prescription is created)
    public boolean reduceStock(int medicationId, int amount) {
        int currentQty = getQuantity(medicationId);
        if (currentQty < amount) {
            return false; // insufficient stock
        }
        updateStock(medicationId, currentQty - amount);
        return true;
    }

    // Get all inventory items
    public List<Inventory> getAllInventory() {
        List<Inventory> inventoryList = new ArrayList<>();
        String sql = "SELECT medication_id, quantity, last_updated FROM inventory";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Inventory inv = new Inventory();
                inv.setMedicationId(rs.getInt("medication_id"));
                inv.setQuantity(rs.getInt("quantity"));
                inv.setLastUpdated(rs.getTimestamp("last_updated").toLocalDateTime());
                inventoryList.add(inv);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return inventoryList;
    }
    public List<InventoryViewDTO> getInventoryWithMedication() {
        String sql = """
        SELECT i.medication_id, m.name, i.quantity, i.last_updated
        FROM inventory i
        JOIN medication m ON i.medication_id = m.medication_id
        ORDER BY m.name
    """;

        List<InventoryViewDTO> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new InventoryViewDTO(
                        rs.getInt("medication_id"),
                        rs.getString("name"),
                        rs.getInt("quantity"),
                        rs.getTimestamp("last_updated").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

}
