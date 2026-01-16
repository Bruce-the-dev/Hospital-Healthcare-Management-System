package com.hospital.Service;

import com.hospital.Dao.ReportDAO;
import com.hospital.Models.DTO.MedicationUsageDTO;
import com.hospital.Models.DTO.PatientPrescriptionDTO;

import java.util.List;

public class ReportService {

    private final ReportDAO reportDAO = new ReportDAO();

    public List<MedicationUsageDTO> getMedicationUsage() {
        return reportDAO.getMedicationUsage();
    }

    public List<PatientPrescriptionDTO> getPrescriptionsPerPatient() {
        return reportDAO.getPrescriptionsPerPatient();
    }
}

