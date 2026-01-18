package com.hospital.Models.DTO;

import java.time.LocalDateTime;

public class AppointmentReport {

    private final int appointmentId;
    private final LocalDateTime appointmentDate;
    private final String status;
    private final String relatedPersonName;

    public AppointmentReport(int appointmentId,
                             LocalDateTime appointmentDate,
                             String status,
                             String relatedPersonName) {
        this.appointmentId = appointmentId;
        this.appointmentDate = appointmentDate;
        this.status = status;
        this.relatedPersonName = relatedPersonName;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public LocalDateTime getAppointmentDate() {
        return appointmentDate;
    }

    public String getStatus() {
        return status;
    }

    public String getRelatedPersonName() {
        return relatedPersonName;
    }

    @Override
    public String toString() {
        return "AppointmentReport{" +
                "appointmentId=" + appointmentId +
                ", appointmentDate=" + appointmentDate +
                ", status='" + status + '\'' +
                ", relatedPersonName='" + relatedPersonName + '\'' +
                '}';
    }
}


