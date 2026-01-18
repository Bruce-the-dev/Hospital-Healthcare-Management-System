package com.hospital.Models;

public class PrescriptionMedication {
    private int prescriptionId; // FK
    private int medicationId;   // FK
    private String dosage;

    public PrescriptionMedication(int prescriptionId, int medicationId, String dosage) {
        this.prescriptionId = prescriptionId;
        this.medicationId = medicationId;
        this.dosage = dosage;

    }


    public int getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(int prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public int getMedicationId() {
        return medicationId;
    }

    public void setMedicationId(int medicationId) {
        this.medicationId = medicationId;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }
    @Override
    public String toString() {
        return "PrescriptionMedication{" +
                "prescriptionId=" + prescriptionId +
                ", medicationId=" + medicationId +
                ", dosage='" + dosage + '\'' +
                '}';
    }
}
