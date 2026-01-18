package com.hospital.Models.DTO;

import java.time.LocalDateTime;

public class FullAppointmentReport {

    private int appointmentId;
    private int patientId;
    private int doctorId;
    private final LocalDateTime appointmentDate;
    private final String status;
    private final String patientName;
    private final String doctorName;
    private final String departmentName;

    public FullAppointmentReport(LocalDateTime appointmentDate,
                                 String status,
                                 String patientName,
                                 String doctorName,
                                 String departmentName) {
        this.appointmentDate = appointmentDate;
        this.status = status;
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.departmentName = departmentName;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public LocalDateTime getAppointmentDate() {
        return appointmentDate;
    }

    public String getStatus() {
        return status;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    @Override
    public String toString() {
        return "FullAppointmentReport{" +
                "appointmentDate=" + appointmentDate +
                ", status='" + status + '\'' +
                ", patientName='" + patientName + '\'' +
                ", doctorName='" + doctorName + '\'' +
                ", departmentName='" + departmentName + '\'' +
                '}';
    }
}

