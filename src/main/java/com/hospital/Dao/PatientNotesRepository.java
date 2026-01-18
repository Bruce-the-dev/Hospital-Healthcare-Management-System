package com.hospital.Dao;

import com.hospital.Models.NoSQL.PatientNoteDocument;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PatientNotesRepository {

    // Simulated NoSQL document store
    private final List<PatientNoteDocument> documents = new ArrayList<>();

    public void save(PatientNoteDocument document) {
        documents.add(document);
    }

    public List<PatientNoteDocument> findByPatientId(int patientId) {
        return documents.stream()
                .filter(d -> d.getPatientId() == patientId)
                .collect(Collectors.toList());
    }

    public List<PatientNoteDocument> findByAppointmentId(int appointmentId) {
        return documents.stream()
                .filter(d -> d.getAppointmentId() == appointmentId)
                .collect(Collectors.toList());
    }
}

