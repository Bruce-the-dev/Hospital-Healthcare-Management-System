package com.hospital.Models.DTO;

import java.time.LocalDate;

public class PrescriptionReportDTO {

    private int prescriptionId;
    private int appointmentId;
    private String patientName;
    private String doctorName;
    private String medicationName;
    private String dosage;
    private LocalDate issuedDate;


    public PrescriptionReportDTO() {}

    public PrescriptionReportDTO(int prescriptionId, int appointmentId, String patientName,
                                 String doctorName, String medicationName, String dosage,
                                 LocalDate issuedDate) {
        this.prescriptionId = prescriptionId;
        this.appointmentId = appointmentId;
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.medicationName = medicationName;
        this.dosage = dosage;
        this.issuedDate = issuedDate;
    }

    public int getPrescriptionId() { return prescriptionId; }
    public void setPrescriptionId(int prescriptionId) { this.prescriptionId = prescriptionId; }

    public int getAppointmentId() { return appointmentId; }
    public void setAppointmentId(int appointmentId) { this.appointmentId = appointmentId; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    public String getMedicationName() { return medicationName; }
    public void setMedicationName(String medicationName) { this.medicationName = medicationName; }

    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }

    public LocalDate getIssuedDate() { return issuedDate; }
    public void setIssuedDate(LocalDate issuedDate) { this.issuedDate = issuedDate; }

    @Override
    public String toString() {
        return "PrescriptionReportDTO{" +
                "prescriptionId=" + prescriptionId +
                ", appointmentId=" + appointmentId +
                ", patientName='" + patientName + '\'' +
                ", doctorName='" + doctorName + '\'' +
                ", medicationName='" + medicationName + '\'' +
                ", dosage='" + dosage + '\'' +
                ", issuedDate=" + issuedDate +
                '}';
    }
}

