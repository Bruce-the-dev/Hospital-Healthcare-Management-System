package com.hospital.Dao;

import com.hospital.Models.Appointment;
import com.hospital.Util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {

    public boolean addAppointment(Appointment a) {
        String sql = "INSERT INTO Appointment(patient_id, doctor_id, appointment_date, status) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, a.getPatientId());
            ps.setInt(2, a.getDoctorId());
            ps.setTimestamp(3, Timestamp.valueOf(a.getAppointmentDate()));
            ps.setString(4, a.getStatus());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                a.setAppointmentId(rs.getInt(1));
            }
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Appointment> getAppointmentsByPatient(int patientId) {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT * FROM Appointment WHERE patient_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, patientId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Appointment a = new Appointment();
                a.setAppointmentId(rs.getInt("appointment_id"));
                a.setPatientId(rs.getInt("patient_id"));
                a.setDoctorId(rs.getInt("doctor_id"));
                a.setAppointmentDate(rs.getTimestamp("appointment_date").toLocalDateTime());
                a.setStatus(rs.getString("status"));
                list.add(a);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
