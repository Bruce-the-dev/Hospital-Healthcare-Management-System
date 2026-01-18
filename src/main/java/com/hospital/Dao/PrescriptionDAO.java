package com.hospital.Dao;

import com.hospital.Models.DTO.PrescriptionReportDTO;
import com.hospital.Models.Prescription;
import com.hospital.Util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
    public Prescription getByAppointmentId(int appointmentId) {
        String sql = "SELECT prescription_id, appointment_id, issued_date, notes FROM prescription WHERE appointment_id = ?";
        Prescription prescription = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, appointmentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    prescription = new Prescription();
                    prescription.setPrescriptionId(rs.getInt("prescription_id"));
                    prescription.setAppointmentId(rs.getInt("appointment_id"));
                    prescription.setIssuedDate(rs.getDate("issued_date").toLocalDate());
                    prescription.setNotes(rs.getString("notes"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prescription;
    }

    // Get all prescription reports (joins with patient, doctor, medications)
    public List<PrescriptionReportDTO> getAllPrescriptionReports() {
        String sql = """
                SELECT
                    p.prescription_id,
                    a.appointment_id,
                    pat.first_name || ' ' || pat.last_name AS patient_name,
                    d.first_name || ' ' || d.last_name AS doctor_name,
                    m.name AS medication_name,
                    pm.dosage,
                    p.issued_date
                FROM prescription p
                JOIN appointment a ON p.appointment_id = a.appointment_id
                JOIN patient pat ON a.patient_id = pat.patient_id
                JOIN doctor d ON a.doctor_id = d.doctor_id
                JOIN prescription_medication pm ON p.prescription_id = pm.prescription_id
                JOIN medication m ON pm.medication_id = m.medication_id
                ORDER BY p.issued_date DESC
                """;

        List<PrescriptionReportDTO> reports = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                PrescriptionReportDTO dto = new PrescriptionReportDTO();
                dto.setPrescriptionId(rs.getInt("prescription_id"));
                dto.setAppointmentId(rs.getInt("appointment_id"));
                dto.setPatientName(rs.getString("patient_name"));
                dto.setDoctorName(rs.getString("doctor_name"));
                dto.setMedicationName(rs.getString("medication_name"));
                dto.setDosage(rs.getString("dosage"));
                dto.setIssuedDate(rs.getDate("issued_date").toLocalDate());

                reports.add(dto);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reports;
    }

    // Get reports filtered by patient
    public List<PrescriptionReportDTO> getReportsByPatient(int patientId) {
        String sql = """
                SELECT
                    p.prescription_id,
                    a.appointment_id,
                    pat.first_name || ' ' || pat.last_name AS patient_name,
                    d.first_name || ' ' || d.last_name AS doctor_name,
                    m.name AS medication_name,
                    pm.dosage,
                    p.issued_date
                FROM prescription p
                JOIN appointment a ON p.appointment_id = a.appointment_id
                JOIN patient pat ON a.patient_id = pat.patient_id
                JOIN doctor d ON a.doctor_id = d.doctor_id
                JOIN prescription_medication pm ON p.prescription_id = pm.prescription_id
                JOIN medication m ON pm.medication_id = m.medication_id
                WHERE pat.patient_id = ?
                ORDER BY p.issued_date DESC
                """;

        List<PrescriptionReportDTO> reports = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, patientId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PrescriptionReportDTO dto = new PrescriptionReportDTO();
                    dto.setPrescriptionId(rs.getInt("prescription_id"));
                    dto.setAppointmentId(rs.getInt("appointment_id"));
                    dto.setPatientName(rs.getString("patient_name"));
                    dto.setDoctorName(rs.getString("doctor_name"));
                    dto.setMedicationName(rs.getString("medication_name"));
                    dto.setDosage(rs.getString("dosage"));
                    dto.setIssuedDate(rs.getDate("issued_date").toLocalDate());

                    reports.add(dto);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reports;
    }

    // Get reports filtered by doctor
    public List<PrescriptionReportDTO> getReportsByDoctor(int doctorId) {
        String sql = """
                SELECT
                    p.prescription_id,
                    a.appointment_id,
                    pat.first_name || ' ' || pat.last_name AS patient_name,
                    d.first_name || ' ' || d.last_name AS doctor_name,
                    m.name AS medication_name,
                    pm.dosage,
                    p.issued_date
                FROM prescription p
                JOIN appointment a ON p.appointment_id = a.appointment_id
                JOIN patient pat ON a.patient_id = pat.patient_id
                JOIN doctor d ON a.doctor_id = d.doctor_id
                JOIN prescription_medication pm ON p.prescription_id = pm.prescription_id
                JOIN medication m ON pm.medication_id = m.medication_id
                WHERE d.doctor_id = ?
                ORDER BY p.issued_date DESC
                """;

        List<PrescriptionReportDTO> reports = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, doctorId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PrescriptionReportDTO dto = new PrescriptionReportDTO();
                    dto.setPrescriptionId(rs.getInt("prescription_id"));
                    dto.setAppointmentId(rs.getInt("appointment_id"));
                    dto.setPatientName(rs.getString("patient_name"));
                    dto.setDoctorName(rs.getString("doctor_name"));
                    dto.setMedicationName(rs.getString("medication_name"));
                    dto.setDosage(rs.getString("dosage"));
                    dto.setIssuedDate(rs.getDate("issued_date").toLocalDate());

                    reports.add(dto);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reports;
    }
}

