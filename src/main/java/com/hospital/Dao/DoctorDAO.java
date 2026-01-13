package com.hospital.Dao;

import com.hospital.Models.Doctor;
import com.hospital.Util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorDAO {

    public boolean addDoctor(Doctor doctor) {
        String sql = "INSERT INTO Doctor(first_name, last_name, specialization, department_id) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, doctor.getFirstName());
            ps.setString(2, doctor.getLastName());
            ps.setString(3, doctor.getSpecialization());
            ps.setInt(4, doctor.getDepartmentId());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                doctor.setDoctorId(rs.getInt(1));
            }
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Doctor getDoctorById(int id) {
        String sql = "SELECT * FROM Doctor WHERE doctor_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return map(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Doctor> getAllDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT * FROM Doctor";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                doctors.add(map(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctors;
    }

    public boolean updateDoctor(Doctor doctor) {
        String sql = "UPDATE Doctor SET first_name=?, last_name=?, specialization=?, department_id=? WHERE doctor_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, doctor.getFirstName());
            ps.setString(2, doctor.getLastName());
            ps.setString(3, doctor.getSpecialization());
            ps.setInt(4, doctor.getDepartmentId());
            ps.setInt(5, doctor.getDoctorId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteDoctor(int id) {
        String sql = "DELETE FROM Doctor WHERE doctor_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Doctor map(ResultSet rs) throws SQLException {
        Doctor d = new Doctor();
        d.setDoctorId(rs.getInt("doctor_id"));
        d.setFirstName(rs.getString("first_name"));
        d.setLastName(rs.getString("last_name"));
        d.setSpecialization(rs.getString("specialization"));
        d.setDepartmentId(rs.getInt("department_id"));
        return d;
    }
}

