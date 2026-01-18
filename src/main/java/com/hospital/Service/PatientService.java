package com.hospital.Service;

import com.hospital.Dao.PatientDao;
import com.hospital.Models.Patient;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class PatientService {

    private final PatientDao patientDAO;
    private final Map<Integer, Patient> patientCache;
    private final Map<String, List<Patient>> lastNameCache;

    public PatientService() {
        this.patientDAO = new PatientDao();
        this.patientCache = new HashMap<>();
        this.lastNameCache = new HashMap<>();
    }

    // Load cache if empty
    private void loadCacheEmpty() {
        if (patientCache.isEmpty()) {
            List<Patient> patients = patientDAO.getAllPatients();
            for (Patient p : patients) {
                patientCache.put(p.getPatientId(), p);
                indexLastName(p);
            }
        }
    }

    private void indexLastName(Patient p) {
        String key = p.getLastName().toLowerCase();
        lastNameCache.computeIfAbsent(key, k -> new ArrayList<>()).add(p);
    }

    public boolean addPatient(Patient patient) {
        boolean success = patientDAO.addPatient(patient);
        if (success) {
            patientCache.put(patient.getPatientId(), patient);
            indexLastName(patient);
        }
        return success;
    }

    public boolean updatePatient(Patient patient) {
        boolean success = patientDAO.updatePatient(patient);
        if (success) {
            patientCache.put(patient.getPatientId(), patient);

            rebuildLastNameCache();
        }
        return success;
    }

    public boolean deletePatient(int id) {
        boolean success = patientDAO.deleteById(id);
        if (success) {
            Patient removed = patientCache.remove(id);
            if (removed != null) rebuildLastNameCache();
        }
        return success;
    }

    private void rebuildLastNameCache() {
        lastNameCache.clear();
        for (Patient p : patientCache.values()) {
            indexLastName(p);
        }
    }

    public Patient getPatientById(int id) throws SQLException {
        if (patientCache.containsKey(id)) return patientCache.get(id);

        Patient patient = patientDAO.getPatient(id);
        if (patient != null) {
            patientCache.put(id, patient);
            indexLastName(patient);
        }
        return patient;
    }

    public List<Patient> getAllPatients() {
        loadCacheEmpty();
        return new ArrayList<>(patientCache.values());
    }

    public List<Patient> searchByLastName(String lastName) {
        loadCacheEmpty();
        if (lastName == null || lastName.isEmpty()) return new ArrayList<>(patientCache.values());

        String key = lastName.toLowerCase();
        // Exact match from cache
        List<Patient> exact = lastNameCache.getOrDefault(key, Collections.emptyList());

        // Also include partial matches
        List<Patient> partial = patientCache.values().stream()
                .filter(p -> p.getLastName().toLowerCase().contains(key) && !exact.contains(p))
                .toList();

        List<Patient> results = new ArrayList<>(exact);
        results.addAll(partial);
        return results;
    }


    public List<Patient> sortByLastName() {
        loadCacheEmpty();
        return patientCache.values().stream()
                .sorted(Comparator.comparing(Patient::getLastName))
                .collect(Collectors.toList());
    }


}
