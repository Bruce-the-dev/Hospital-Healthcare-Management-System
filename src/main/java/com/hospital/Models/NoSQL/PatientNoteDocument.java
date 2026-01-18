package com.hospital.Models.NoSQL;

import java.time.LocalDateTime;
import java.util.Map;

public class PatientNoteDocument {

    private int patientId;
    private int doctorId;
    private int appointmentId;
    private LocalDateTime createdAt;
    private Map<String, Object> notes;

    public PatientNoteDocument(
            int patientId,
            int doctorId,
            int appointmentId,
            Map<String, Object> notes
    ) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentId = appointmentId;
        this.notes = notes;
        this.createdAt = LocalDateTime.now();
    }

    public int getPatientId() { return patientId; }
    public int getDoctorId() { return doctorId; }
    public int getAppointmentId() { return appointmentId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public Map<String, Object> getNotes() { return notes; }
}
