package com.hospital.Tests;

import com.hospital.Models.Appointment;
import com.hospital.Models.DTO.AppointmentReport;
import com.hospital.Models.DTO.BootstrapDataLoader;
import com.hospital.Models.DTO.FullAppointmentReport;
import com.hospital.Service.AppointmentService;

import java.time.LocalDateTime;
import java.util.List;

public class AppointmentServiceTest {

    public static void main(String[] args) {
        new BootstrapDataLoader().loadInitialData();
        AppointmentService service = new AppointmentService();

        // ----------------- ADD APPOINTMENTS -----------------
        Appointment a1 = new Appointment();
        a1.setPatientId(10); // assuming patient with ID 1 exists
        a1.setDoctorId(1);  // assuming doctor with ID 1 exists
        a1.setAppointmentDate(LocalDateTime.of(2026, 1, 15, 10, 0));
        a1.setStatus("Scheduled");
        service.addAppointment(a1);

        Appointment a2 = new Appointment();
        a2.setPatientId(8);
        a2.setDoctorId(1);
        a2.setAppointmentDate(LocalDateTime.of(2026, 1, 16, 14, 30));
        a2.setStatus("Scheduled");
        service.addAppointment(a2);

        Appointment a3 = new Appointment();
        a3.setPatientId(9);
        a3.setDoctorId(2);
        a3.setAppointmentDate(LocalDateTime.of(2026, 1, 17, 9, 0));
        a3.setStatus("Completed");
        service.addAppointment(a3);

        // ----------------- FETCH BY ID (Cache test) -----------------
        System.out.println("\n--- Fetch appointment by ID (DB fetch) ---");
        long start1 = System.nanoTime();
        Appointment fetched1 = service.getAppointmentById(a1.getAppointmentId());
        long end1 = System.nanoTime();
        System.out.println(fetched1);
        System.out.println("DB fetch time: " + (end1 - start1) + " ns");

        System.out.println("\n--- Fetch same appointment (Cache fetch) ---");
        long start2 = System.nanoTime();
        Appointment fetched2 = service.getAppointmentById(a1.getAppointmentId());
        long end2 = System.nanoTime();
        System.out.println(fetched2);
        System.out.println("Cache fetch time: " + (end2 - start2) + " ns");

        // ----------------- REPORTING: APPOINTMENTS BY PATIENT -----------------
        System.out.println("\n--- Appointments for Patient ID 1 ---");
        List<AppointmentReport> patientAppointments = service.getAppointmentsByPatient(1);
        patientAppointments.forEach(System.out::println);

        // ----------------- REPORTING: APPOINTMENTS BY DOCTOR -----------------
        System.out.println("\n--- Appointments for Doctor ID 1 ---");
        List<AppointmentReport> doctorAppointments = service.getAppointmentsByDoctor(1);
        doctorAppointments.forEach(System.out::println);

        // ----------------- REPORTING: FULL APPOINTMENT REPORT -----------------
        System.out.println("\n--- Full Appointment Report ---");
        List<FullAppointmentReport> fullReport = service.getFullAppointmentReport();
        fullReport.forEach(System.out::println);

        // ----------------- SEARCH -----------------
        System.out.println("\n--- Search Appointments by Status 'Scheduled' ---");
        service.searchByStatus("Scheduled").forEach(System.out::println);

        // ----------------- SORT -----------------
        System.out.println("\n--- Sort Appointments by Date ---");
        service.sortByDate().forEach(System.out::println);

        // ----------------- UPDATE -----------------
        System.out.println("\n--- Update Appointment Status ---");
        a1.setStatus("Completed");
        service.updateAppointment(a1);
        System.out.println(service.getAppointmentById(a1.getAppointmentId()));

        // ----------------- DELETE -----------------
        System.out.println("\n--- Delete Appointment ID " + a2.getAppointmentId() + " ---");
        service.deleteAppointment(a2.getAppointmentId());
        System.out.println("Fetch deleted appointment: " + service.getAppointmentById(a2.getAppointmentId()));
    }
}

