package com.hospital.Models;

import java.time.LocalDate;

public class Patient {
    private int patientId;
    private String firstName;
    private String lastName;
    private char gender;
    private LocalDate dateOfBirth;
    private String phone;
    private String email;

    @Override
    public String toString() {
        return "Patient{" +
                "patientId=" + patientId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender=" + gender +
                ", dateOfBirth=" + dateOfBirth +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
