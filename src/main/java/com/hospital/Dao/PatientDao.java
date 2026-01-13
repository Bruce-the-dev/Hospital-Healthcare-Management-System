package com.hospital.Dao;

import com.hospital.Models.Patient;
import com.hospital.Util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDao {
    public boolean addPatient(Patient patient) {
        String sql = "Insert into Patient(first_name, last_name, gender, date_of_birth, phone, email)" + "Values(?,?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, patient.getFirstName());
            ps.setString(2, patient.getLastName());
            ps.setString(3, String.valueOf(patient.getGender()));
            ps.setDate(4, Date.valueOf(patient.getDateOfBirth()));
            ps.setString(5, patient.getPhone());
            ps.setString(6, patient.getEmail());
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        patient.setPatientId(id); // Update the Patient object with the DB ID
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Patient getPatient(int patientId) throws SQLException {
        String sql = "SELECT * FROM Patient WHERE patient_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPatient(rs);
                }
            }
        }
        return null;
    }

    public List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * from Patient";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                patients.add(mapResultSetToPatient(rs));
            }
            System.out.println("Patients loaded: " + patients.size());

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }

    public boolean deleteById(int id) {
        String sql = "DELETE FROM Patient WHERE patient_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updatePatient(Patient patient) {
        String sql = "UPDATE Patient SET first_name=?, last_name=?, gender=?, date_of_birth=?, phone=?, email=? " +
                "WHERE patient_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, patient.getFirstName());
            ps.setString(2, patient.getLastName());
            ps.setString(3, String.valueOf(patient.getGender()));
            ps.setDate(4, Date.valueOf(patient.getDateOfBirth()));
            ps.setString(5, patient.getPhone());
            ps.setString(6, patient.getEmail());
            ps.setInt(7, patient.getPatientId());
            int rowsAffected = ps.executeUpdate();
            System.out.println(rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Patient mapResultSetToPatient(ResultSet rs) throws SQLException {
        Patient patient = new Patient();
        patient.setPatientId(rs.getInt("patient_id"));
        patient.setFirstName(rs.getString("first_name"));
        patient.setLastName(rs.getString("last_name"));
        // Safe gender handling
        String genderStr = rs.getString("gender");
        if (genderStr != null && !genderStr.isEmpty()) {
            patient.setGender(genderStr.charAt(0));
        }
        Date dob = rs.getDate("date_of_birth");
        if (dob != null) {
            patient.setDateOfBirth(dob.toLocalDate());
        }
        patient.setPhone(rs.getString("phone"));
        patient.setEmail(rs.getString("email"));
        return patient;
    }
}
