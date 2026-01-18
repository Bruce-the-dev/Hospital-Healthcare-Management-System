package com.hospital.Dao;

import com.hospital.Models.PrescriptionMedication;
import com.hospital.Util.DBConnection;

import java.sql.*;

public class PrescriptionMedicationDAO {

    public void addMedicationToPrescription(PrescriptionMedication pm) {
        String sql = "INSERT INTO Prescription_Medication(prescription_id, medication_id, dosage) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, pm.getPrescriptionId());
            ps.setInt(2, pm.getMedicationId());
            ps.setString(3, pm.getDosage());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
