package com.hospital.Dao;

import com.hospital.Models.Medication;
import com.hospital.Util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicationDAO {

    public void addMedication(Medication medication) {
        String sql = """
            INSERT INTO medication (name, description)
            VALUES (?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, medication.getName());
            ps.setString(2, medication.getDescription());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error adding medication", e);
        }
    }
    public List<Medication> getAllMedications() {
        List<Medication> meds = new ArrayList<>();
        String sql = "SELECT * FROM Medication";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Medication m = new Medication();
                m.setMedicationId(rs.getInt("medication_id"));
                m.setName(rs.getString("name"));
                m.setDescription(rs.getString("description"));
                meds.add(m);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return meds;
    }
    public Medication getMedicationById(int id) {
        String sql = "SELECT * FROM medication WHERE medication_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Medication(
                        rs.getInt("medication_id"),
                        rs.getString("name"),
                        rs.getString("description")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching medication", e);
        }
        return null;
    }

    public boolean deleteMedication(int medicationId) {
        String sql = "DELETE FROM medication WHERE medication_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, medicationId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateMedication(Medication selected) {
        String sql = "UPDATE medication SET name=?,description=? WHERE medication_id =?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, selected.getName());
            ps.setString(2, selected.getDescription());
            ps.setInt(3, selected.getMedicationId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

