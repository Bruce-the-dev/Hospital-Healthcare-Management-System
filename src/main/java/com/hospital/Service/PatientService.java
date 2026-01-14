package com.hospital.Service;

import com.hospital.Dao.PatientDao;
import com.hospital.Models.Patient;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class PatientService {
    private final PatientDao patientDAO;
    private final Map<Integer, Patient> patientCache;

    public PatientService() {
        this.patientDAO = new PatientDao();
        this.patientCache = new HashMap<>();
    }

    private void loadCacheEmpty(){
        if (patientCache.isEmpty()){
            List<Patient> patients = patientDAO.getAllPatients();
            for (Patient p: patients){
                patientCache.put(p.getPatientId(),p);
            }
        }
    }
    public boolean addPatient(Patient patient) {
        boolean success = patientDAO.addPatient(patient);
        if (success) {
            // Cache update (cache invalidation handled)
            patientCache.put(patient.getPatientId(), patient);
        }
        return success;
    }
    public Patient getPatientById(int id) throws SQLException {
        // O(1) cache lookup
        if (patientCache.containsKey(id)) {
            return patientCache.get(id);
        }
        Patient patient = patientDAO.getPatient(id);
        if (patient != null) {
            patientCache.put(id, patient);
        }
        return patient;
    }
    public List<Patient> getAllPatients(){
        patientCache.clear();
        List<Patient> patients = patientDAO.getAllPatients();
        for (Patient p : patients) {
            patientCache.put(p.getPatientId(), p);
        }
        return new ArrayList<>(patientCache.values());
    }

    public boolean updatePatient(Patient patient){
        boolean success = patientDAO.updatePatient(patient);
        if (success) {
            // Cache invalidation + refresh
            patientCache.put(patient.getPatientId(), patient);
        }
        return success;
    }
    public boolean deletePatient(int id){
        boolean success = patientDAO.deleteById(id);
        if (success) {
            patientCache.remove(id);

        }
        return success;
    }
    // Linear search by last name (O(n))
    public List<Patient> searchByLastName(String lastName) {
        loadCacheEmpty();
        return patientCache.values().stream()
                .filter(p -> p.getLastName().toLowerCase().
                        contains(lastName.toLowerCase())).collect(Collectors.toList());
    }
    // Sort by last name
    public List<Patient> sortByLastName() {
        loadCacheEmpty();
        return patientCache.values().stream()
                .sorted(Comparator.comparing(Patient::getLastName))
                .collect(Collectors.toList());
    }
    // Sort by date of birth
    public List<Patient> sortByDateOfBirth() {
        loadCacheEmpty();
        return patientCache.values().stream()
                .sorted(Comparator.comparing(Patient::getDateOfBirth))
                .collect(Collectors.toList());
    }
    public void clearCache() {
        patientCache.clear();
    }

}
