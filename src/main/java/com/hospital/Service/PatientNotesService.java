package com.hospital.Service;

import com.hospital.Models.NoSQL.PatientNoteDocument;
import com.hospital.Dao.PatientNotesRepository;

import java.util.List;
import java.util.Map;

public class PatientNotesService {

    private final PatientNotesRepository repository = new PatientNotesRepository();

    public void addNote(
            int patientId,
            int doctorId,
            int appointmentId,
            Map<String, Object> notes
    ) {
        repository.save(
                new PatientNoteDocument(patientId, doctorId, appointmentId, notes)
        );
    }

    public List<PatientNoteDocument> getNotesForPatient(int patientId) {
        return repository.findByPatientId(patientId);
    }

    public List<PatientNoteDocument> getNotesForAppointment(int appointmentId) {
        return repository.findByAppointmentId(appointmentId);
    }
}
