package com.hospital.Dao;

import com.hospital.Models.Prescription;
import com.hospital.Util.DBConnection;

import java.sql.*;

public class PrescriptionDAO {

    public boolean addPrescription(Prescription p) {
        String sql = "INSERT INTO Prescription(appointment_id, issued_date, notes) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, p.getAppointmentId());
            ps.setDate(2, Date.valueOf(p.getIssuedDate()));
            ps.setString(3, p.getNotes());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                p.setPrescriptionId(rs.getInt(1));
            }
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

