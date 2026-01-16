package com.hospital.Models.DTO;

public class PatientPrescriptionDTO {
    private String patientName;
    private int totalPrescriptions;

    public PatientPrescriptionDTO(String patientName, int totalPrescriptions) {
        this.patientName = patientName;
        this.totalPrescriptions = totalPrescriptions;
    }

    public String getPatientName() { return patientName; }
    public int getTotalPrescriptions() { return totalPrescriptions; }
}
