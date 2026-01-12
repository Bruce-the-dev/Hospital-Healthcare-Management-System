package com.hospital.Models;

public class Feedback {
    private int feedbackId;
    private int patientId; // FK
    private int doctorId;  // FK
    private int rating;    // 1–5
    private String comment;

    // Constructors
    public Feedback() {}
    public Feedback(int feedbackId, int patientId, int doctorId, int rating, String comment) {
        this.feedbackId = feedbackId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.rating = rating;
        this.comment = comment;
    }

    // Getters and Setters
    public int getFeedbackId() { return feedbackId; }
    public void setFeedbackId(int feedbackId) { this.feedbackId = feedbackId; }

    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }

    public int getDoctorId() { return doctorId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    @Override
    public String toString() {
        return "Feedback{" +
                "feedbackId=" + feedbackId +
                ", patientId=" + patientId +
                ", doctorId=" + doctorId +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                '}';
    }
}
