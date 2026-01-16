package com.hospital.Service;

import com.hospital.Dao.AppointmentDAO;
import com.hospital.Models.Appointment;
import com.hospital.Models.DTO.AppointmentReport;
import com.hospital.Models.DTO.FullAppointmentReport;

import java.util.*;
import java.util.stream.Collectors;

public class AppointmentService {
    private final AppointmentDAO appointmentDAO;
    private final Map <Integer, Appointment> appointmentCache;

    public AppointmentService() {
        this.appointmentDAO = new AppointmentDAO();
        this.appointmentCache = new HashMap<>();
    }
    public void addAppointment(Appointment appointment) {
        boolean success = appointmentDAO.addAppointment(appointment);
        if (success) {
            appointmentCache.put(appointment.getAppointmentId(), appointment);
        }
    }
    public Appointment getAppointmentById(int id) {
        if (appointmentCache.containsKey(id)) {
            return appointmentCache.get(id);
        }
        Appointment a = appointmentDAO.getAppointmentById(id);
        if (a != null) appointmentCache.put(a.getAppointmentId(), a);
        return a;
    }
    public List<Appointment> getAllAppointments() {
       appointmentCache.clear();
        List<Appointment> all = appointmentDAO.getAllAppointments();
        for (Appointment a : all) {
            appointmentCache.put(a.getAppointmentId(), a);
        }
        return all;
    }
    public void updateAppointment(Appointment appointment) {
        boolean success = appointmentDAO.updateAppointment(appointment);
        if (success) {
            appointmentCache.put(appointment.getAppointmentId(), appointment);
        }
    }
    public void deleteAppointment(int id) {
        boolean success = appointmentDAO.deleteAppointment(id);
        if (success) {
            appointmentCache.remove(id);
        }
    }

    // Appointments for a patient (with doctor name)
    public List<AppointmentReport> getAppointmentsByPatient(int patientId) {
        return appointmentDAO.getAppointmentsByPatient(patientId);
    }
    // Appointments for a doctor (with patient name)
    public List<AppointmentReport> getAppointmentsByDoctor(int doctorId) {
        return appointmentDAO.getAppointmentsByDoctor(doctorId);
    }
    // Full appointment report (patient + doctor + department)
    public List<FullAppointmentReport> getFullAppointmentReport() {
        return appointmentDAO.getFullAppointmentReport();
    }
    public List<Appointment> searchByStatus(String status) {
        return getAllAppointments().stream()
                .filter(a -> a.getStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList());
    }

    public List<Appointment> sortByDate() {
        List<Appointment> list = new ArrayList<>(getAllAppointments());
        list.sort(Comparator.comparing(Appointment::getAppointmentDate));
        return list;
    }
    public List<FullAppointmentReport> getAppointmentsWithoutPrescription() {
        return appointmentDAO.getAppointmentsWithoutPrescription();
    }

    public void clearCache() {
        appointmentCache.clear();
    }

}
