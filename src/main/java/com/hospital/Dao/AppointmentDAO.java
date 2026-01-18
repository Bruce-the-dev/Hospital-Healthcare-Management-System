package com.hospital.Dao;

import com.hospital.Models.Appointment;
import com.hospital.Models.DTO.AppointmentReport;
import com.hospital.Models.DTO.FullAppointmentReport;
import com.hospital.Util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {

    public boolean addAppointment(Appointment appointment) {
        String sql = """
                    INSERT INTO appointment (patient_id, doctor_id, appointment_date, status)
                    VALUES (?, ?, ?, ?)
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, appointment.getPatientId());
            ps.setInt(2, appointment.getDoctorId());
            ps.setTimestamp(3, Timestamp.valueOf(appointment.getAppointmentDate()));
            ps.setString(4, appointment.getStatus());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    appointment.setAppointmentId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Appointment getAppointmentById(int id) {
        String sql = "SELECT * FROM appointment WHERE appointment_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapAppointment(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Appointment> getAllAppointments() {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT * FROM appointment";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapAppointment(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateAppointment(Appointment appointment) {
        String sql = """
                    UPDATE appointment
                    SET patient_id = ?, doctor_id = ?, appointment_date = ?, status = ?
                    WHERE appointment_id = ?
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, appointment.getPatientId());
            ps.setInt(2, appointment.getDoctorId());
            ps.setTimestamp(3, Timestamp.valueOf(appointment.getAppointmentDate()));
            ps.setString(4, appointment.getStatus());
            ps.setInt(5, appointment.getAppointmentId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteAppointment(int id) {
        String sql = "DELETE FROM appointment WHERE appointment_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    // 🔹 Appointments for a patient (with doctor name)
    public List<AppointmentReport> getAppointmentsByPatient(int patientId) {
        List<AppointmentReport> list = new ArrayList<>();

        String sql = """
                    SELECT a.appointment_id,
                           a.appointment_date,
                           a.status,
                           d.first_name AS doctor_first_name,
                           d.last_name  AS doctor_last_name
                    FROM appointment a
                    JOIN doctor d ON a.doctor_id = d.doctor_id
                    WHERE a.patient_id = ?
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, patientId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new AppointmentReport(
                        rs.getInt("appointment_id"),
                        rs.getTimestamp("appointment_date").toLocalDateTime(),
                        rs.getString("status"),
                        rs.getString("doctor_first_name") + " " +
                                rs.getString("doctor_last_name")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 🔹 Appointments for a doctor (with patient name)
    public List<AppointmentReport> getAppointmentsByDoctor(int doctorId) {
        List<AppointmentReport> list = new ArrayList<>();

        String sql = """
                    SELECT a.appointment_id,
                           a.appointment_date,
                           a.status,
                           p.first_name AS patient_first_name,
                           p.last_name  AS patient_last_name
                    FROM appointment a
                    JOIN patient p ON a.patient_id = p.patient_id
                    WHERE a.doctor_id = ?
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, doctorId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new AppointmentReport(
                        rs.getInt("appointment_id"),
                        rs.getTimestamp("appointment_date").toLocalDateTime(),
                        rs.getString("status"),
                        rs.getString("patient_first_name") + " " +
                                rs.getString("patient_last_name")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 🔹 Full appointment report (patient + doctor + department)
    public List<FullAppointmentReport> getFullAppointmentReport() {
        List<FullAppointmentReport> list = new ArrayList<>();

        String sql = """
                    SELECT a.appointment_id,
                           a.appointment_date,
                           a.status,
                           p.first_name AS patient_first,
                           p.last_name  AS patient_last,
                           d.first_name AS doctor_first,
                           d.last_name  AS doctor_last,
                           dept.name    AS department_name
                    FROM appointment a
                    JOIN patient p ON a.patient_id = p.patient_id
                    JOIN doctor d ON a.doctor_id = d.doctor_id
                    JOIN department dept ON d.department_id = dept.department_id
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                FullAppointmentReport report = new FullAppointmentReport(
                        rs.getTimestamp("appointment_date").toLocalDateTime(),
                        rs.getString("status"),
                        rs.getString("patient_first") + " " + rs.getString("patient_last"),
                        rs.getString("doctor_first") + " " + rs.getString("doctor_last"),
                        rs.getString("department_name")
                );

                report.setAppointmentId(rs.getInt("appointment_id"));
                list.add(report);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    public List<FullAppointmentReport> getAppointmentsWithoutPrescription() {

        List<FullAppointmentReport> list = new ArrayList<>();

        String sql = """
                    SELECT
                        a.appointment_id,
                        a.appointment_date,
                        a.status,
                        p.patient_id,
                        p.first_name||' '|| p.last_name AS patient_name,
                        d.doctor_id,
                        d.first_name||' '||d.last_name AS doctor_name,
                        dept.name AS department_name
                    FROM appointment a
                    JOIN patient p ON a.patient_id = p.patient_id
                    JOIN doctor d ON a.doctor_id = d.doctor_id
                    JOIN department dept ON d.department_id = dept.department_id
                    WHERE NOT EXISTS (
                        SELECT 1
                        FROM prescription pr
                        WHERE pr.appointment_id = a.appointment_id
                    )
                    ORDER BY a.appointment_date
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                FullAppointmentReport dto = new FullAppointmentReport(
                        rs.getTimestamp("appointment_date").toLocalDateTime(),
                        rs.getString("status"),
                        rs.getString("patient_name"),
                        rs.getString("doctor_name"),
                        rs.getString("department_name")
                );

                dto.setAppointmentId(rs.getInt("appointment_id"));
                dto.setPatientId(rs.getInt("patient_id"));
                dto.setDoctorId(rs.getInt("doctor_id"));

                list.add(dto);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }


    private Appointment mapAppointment(ResultSet rs) throws SQLException {
        Appointment a = new Appointment();
        a.setAppointmentId(rs.getInt("appointment_id"));
        a.setPatientId(rs.getInt("patient_id"));
        a.setDoctorId(rs.getInt("doctor_id"));
        a.setAppointmentDate(rs.getTimestamp("appointment_date").toLocalDateTime());
        a.setStatus(rs.getString("status"));
        return a;
    }
}

