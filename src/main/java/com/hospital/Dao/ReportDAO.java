package com.hospital.Dao;

import com.hospital.Models.DTO.MedicationUsageDTO;
import com.hospital.Models.DTO.PatientPrescriptionDTO;
import com.hospital.Util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReportDAO {

    public List<MedicationUsageDTO> getMedicationUsage() {
        List<MedicationUsageDTO> list = new ArrayList<>();
        String sql = """
            SELECT m.name, COUNT(pm.medication_id) AS times_used
            FROM prescription_medication pm
            JOIN medication m ON pm.medication_id = m.medication_id
            GROUP BY m.name
            ORDER BY times_used DESC
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new MedicationUsageDTO(
                        rs.getString("name"),
                        rs.getInt("times_used")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

        public List<PatientPrescriptionDTO> getPrescriptionsPerPatient() {
            List<PatientPrescriptionDTO> list = new ArrayList<>();
            String sql = """
            SELECT p.first_name, p.last_name, COUNT(pr.prescription_id) AS total
            FROM prescription pr
            JOIN appointment a ON pr.appointment_id = a.appointment_id
            JOIN patient p ON a.patient_id = p.patient_id
            GROUP BY p.first_name, p.last_name
            ORDER BY total DESC
        """;

            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    list.add(new PatientPrescriptionDTO(
                            rs.getString("first_name") + " " + rs.getString("last_name"),
                            rs.getInt("total")
                    ));
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return list;
        }
    }




