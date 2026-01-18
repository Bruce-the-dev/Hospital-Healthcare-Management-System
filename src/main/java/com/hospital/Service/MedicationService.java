package com.hospital.Service;

import com.hospital.Dao.MedicationDAO;
import com.hospital.Models.Doctor;
import com.hospital.Models.Medication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MedicationService {

    private final MedicationDAO medicationDAO = new MedicationDAO();
    private Map<Integer, Medication> medsCache;

    public MedicationService() {
        this.medsCache = new  HashMap<>();
    }


    public void addMedication(String name, String description) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Medication name is required");
        }
        medicationDAO.addMedication(new Medication(0, name.trim(), description));
    }

    public List<Medication> getAllMedications() {
        return medicationDAO.getAllMedications();
    }

    public Medication getMedicationById(int id) {
        return medicationDAO.getMedicationById(id);
    }

    public void deleteMedication(int medicationId) {
        boolean success = medicationDAO.deleteMedication(medicationId);
        if (success) {
            medsCache.remove(medicationId); // invalidate cache
        }
    }

    public void updateMedication(Medication selected) {
        boolean success = medicationDAO.updateMedication(selected);
        if (success) {
            medsCache.put(selected.getMedicationId(), selected); // update cache
        }
    }
}
