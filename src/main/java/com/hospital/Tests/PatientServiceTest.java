package com.hospital.Tests;

import com.hospital.Models.Patient;
import com.hospital.Service.PatientService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class PatientServiceTest {

    public static void main(String[] args) throws SQLException {

        PatientService patientService = new PatientService();

        // ------------------- CREATE -------------------
        Patient p = new Patient();
        p.setFirstName("John");
        p.setLastName("Doe");
        p.setGender('M');
        p.setDateOfBirth(LocalDate.of(2000, 1, 1));
        p.setPhone("0781234567");
        p.setEmail("john.doe@example.com");

        patientService.addPatient(p);
        System.out.println("Inserted Patient ID: " + p.getPatientId());

        // ------------------- READ (DB FIRST TIME) -------------------
        long startDb = System.nanoTime();
        Patient fromDb = patientService.getPatientById(p.getPatientId());
        long endDb = System.nanoTime();

        System.out.println("Fetched from DB: " + fromDb);
        System.out.println("DB fetch time (ns): " + (endDb - startDb));

        // ------------------- READ (CACHE SECOND TIME) -------------------
        long startCache = System.nanoTime();
        Patient fromCache = patientService.getPatientById(p.getPatientId());
        long endCache = System.nanoTime();

        System.out.println("Fetched from Cache: " + fromCache);
        System.out.println("Cache fetch time (ns): " + (endCache - startCache));

        // ------------------- SEARCH -------------------
        List<Patient> searchResults = patientService.searchByLastName("Doe");
        System.out.println("Search by last name 'Doe': " + searchResults.size() + " result(s)");

        // ------------------- SORT -------------------
        List<Patient> sortedPatients = patientService.sortByLastName();
        System.out.println("Patients sorted by last name:");
        for (Patient patient : sortedPatients) {
            System.out.println(patient);
        }

        // ------------------- UPDATE -------------------
        p.setPhone("0729999999");
        patientService.updatePatient(p);
        System.out.println("Updated patient phone.");

        // ------------------- VERIFY CACHE INVALIDATION -------------------
        Patient updated = patientService.getPatientById(p.getPatientId());
        System.out.println("After update: " + updated);

        // ------------------- DELETE -------------------
        patientService.deletePatient(p.getPatientId());
        System.out.println("Deleted patient.");

        // ------------------- VERIFY DELETE -------------------
        Patient deleted = patientService.getPatientById(p.getPatientId());
        System.out.println("After delete (should be null): " + deleted);
    }
}

