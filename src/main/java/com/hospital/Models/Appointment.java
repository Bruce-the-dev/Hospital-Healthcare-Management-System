package com.hospital.Models;

import java.time.LocalDateTime;

public class Appointment {
    private int appointmentId;
    private int patientId; // FK
    private int doctorId; // FK
    private LocalDateTime appointmentDate;
    private String status;

    @Override
    public String toString() {
        return "Appointment{" +
                "appointmentId=" + appointmentId +
                ", patientId=" + patientId +
                ", doctorId=" + doctorId +
                ", appointmentDate=" + appointmentDate +
                ", status='" + status + '\'' +
                '}';
    }
}
