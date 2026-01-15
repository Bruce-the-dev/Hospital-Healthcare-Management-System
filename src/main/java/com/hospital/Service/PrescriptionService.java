package com.hospital.Service;

import com.hospital.Dao.MedicationDAO;
import com.hospital.Dao.PrescriptionDAO;
import com.hospital.Dao.PrescriptionMedicationDAO;
import com.hospital.Models.Medication;
import com.hospital.Models.Prescription;
import com.hospital.Models.PrescriptionMedication;
import com.hospital.Models.DTO.PrescriptionReportDTO;

import java.time.LocalDate;
import java.util.*;

public class PrescriptionService {

    private final PrescriptionDAO prescriptionDAO = new PrescriptionDAO();
    private final PrescriptionMedicationDAO prescriptionMedicationDAO = new PrescriptionMedicationDAO();
    private final MedicationDAO medicationDAO = new MedicationDAO();
    private final InventoryService inventoryService = new InventoryService();

    // Simple in-memory cache: prescriptionId -> list of DTOs
    private final Map<Integer, List<PrescriptionReportDTO>> prescriptionCache = new HashMap<>();

    // Add a new prescription with multiple medications
    public void addPrescription(Prescription prescription, List<PrescriptionMedication> medications) {

        // Step 1: Validate stock for all medications
        for (PrescriptionMedication pm : medications) {
            if (!inventoryService.checkStock(pm.getMedicationId(), Integer.parseInt(pm.getDosage()))) {
                throw new RuntimeException("Insufficient stock for medication ID: " + pm.getMedicationId());
            }
        }

        // Step 2: add prescription
        prescription.setIssuedDate(LocalDate.now());
        prescriptionDAO.addPrescription(prescription);

        // Step 3: add medications for this prescription
        for (PrescriptionMedication pm : medications) {
            pm.setPrescriptionId(prescription.getPrescriptionId());
            prescriptionMedicationDAO.addMedicationToPrescription(pm);

            // Step 4: Deduct stock automatically
            inventoryService.deductStock(pm.getMedicationId(), Integer.parseInt(pm.getDosage()));
        }

        // Step 5: Invalidate cache
        prescriptionCache.remove(prescription.getPrescriptionId());
    }

    // Fetch all prescription reports
    public List<PrescriptionReportDTO> getAllPrescriptionReports() {
        // Cache all reports under a dummy key 0
        if (!prescriptionCache.containsKey(0)) {
            List<PrescriptionReportDTO> reports = prescriptionDAO.getAllPrescriptionReports();
            prescriptionCache.put(0, reports);
        }
        return prescriptionCache.get(0);
    }

    // Fetch reports by patient
    public List<PrescriptionReportDTO> getReportsByPatient(int patientId) {
        // Cache per patientId
        if (!prescriptionCache.containsKey(patientId)) {
            List<PrescriptionReportDTO> reports = prescriptionDAO.getReportsByPatient(patientId);
            prescriptionCache.put(patientId, reports);
        }
        return prescriptionCache.get(patientId);
    }

    // Fetch reports by doctor
    public List<PrescriptionReportDTO> getReportsByDoctor(int doctorId) {
        // Cache per doctorId
        if (!prescriptionCache.containsKey(-doctorId)) { // negative key for doctor
            List<PrescriptionReportDTO> reports = prescriptionDAO.getReportsByDoctor(doctorId);
            prescriptionCache.put(-doctorId, reports);
        }
        return prescriptionCache.get(-doctorId);
    }

    // Optional: get a prescription by appointment (direct entity)
    public Prescription getPrescriptionByAppointment(int appointmentId) {
        return prescriptionDAO.getByAppointmentId(appointmentId);
    }

    // Optional: get all medications (for dropdowns)
    public List<Medication> getAllMedications() {
        return medicationDAO.getAllMedications();
    }

    // Invalidate cache manually (if you update/delete)
    public void invalidateCache() {
        prescriptionCache.clear();
    }
}
