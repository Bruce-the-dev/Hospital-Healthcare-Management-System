package com.hospital.Models;

import java.time.LocalDate;

public class Prescription {
    private int prescriptionId;
    private int appointmentId; // FK
    private LocalDate issuedDate;
    private String notes;
public Prescription(){}
    public Prescription(int prescriptionId, int appointmentId, LocalDate issuedDate, String notes) {
        this.prescriptionId = prescriptionId;
        this.appointmentId = appointmentId;
        this.issuedDate = issuedDate;
        this.notes = notes;
    }

    public int getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(int prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public LocalDate getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(LocalDate issuedDate) {
        this.issuedDate = issuedDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
    @Override
    public String toString() {
        return "Prescription{" +
                "prescriptionId=" + prescriptionId +
                ", appointmentId=" + appointmentId +
                ", issuedDate=" + issuedDate +
                ", notes='" + notes + '\'' +
                '}';
    }
}

