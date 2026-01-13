package com.hospital.Dao;

import com.hospital.Models.Medication;
import com.hospital.Util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicationDAO {

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
}

