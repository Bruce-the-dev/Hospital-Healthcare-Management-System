package com.hospital.Service;

import com.hospital.Dao.MedicationDAO;
import com.hospital.Models.Medication;

import java.util.List;

public class MedicationService {

    private final MedicationDAO medicationDAO = new MedicationDAO();

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
}
