package com.hospital.Controller;

import com.hospital.Models.DTO.FullAppointmentReport;
import com.hospital.Models.Medication;
import com.hospital.Models.Prescription;
import com.hospital.Models.PrescriptionMedication;
import com.hospital.Models.DTO.PrescriptionReportDTO;
import com.hospital.Service.AppointmentService;
import com.hospital.Service.MedicationService;
import com.hospital.Service.PrescriptionService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.time.LocalDate;

public class PrescriptionController {

    @FXML private ComboBox<Medication> cmbMedication;
    @FXML private TextField txtDosage;
    @FXML private TableView<PrescriptionMedication> tblMedications;
    @FXML private TableColumn<PrescriptionMedication, String> colMedication;
    @FXML private TableColumn<PrescriptionMedication, String> colDosage;
    @FXML private TextArea txtNotes;

    @FXML private ComboBox<FullAppointmentReport> cmbAppointment; // Appointment selector

    private final MedicationService medicationService = new MedicationService();
    private final PrescriptionService prescriptionService = new PrescriptionService();

    private final ObservableList<PrescriptionMedication> medications = FXCollections.observableArrayList();

        private final AppointmentService appointmentService = new AppointmentService();
    @FXML
    public void initialize() {


        ObservableList<FullAppointmentReport> appointments =
                FXCollections.observableArrayList(
                        appointmentService.getAppointmentsWithoutPrescription()
                );

        cmbAppointment.setItems(appointments);

        // Display patient + doctor + issued date
        cmbAppointment.setConverter(new StringConverter<>() {
            @Override
            public String toString(FullAppointmentReport dto) {
                if (dto == null) return "";
                return dto.getPatientName() + " - " +
                        dto.getDoctorName() + " - " +
                        dto.getAppointmentDate().toLocalDate();
            }

            @Override
            public FullAppointmentReport fromString(String string) {
                return null;
            }
        });



        // ============================
        // Setup Medication ComboBox
        // ============================
        cmbMedication.setItems(FXCollections.observableArrayList(medicationService.getAllMedications()));

        cmbMedication.setConverter(new StringConverter<>() {
            @Override
            public String toString(Medication medication) {
                return medication == null ? "" : medication.getName();
            }

            @Override
            public Medication fromString(String string) {
                return null; // ComboBox not editable
            }
        });

        // ============================
        // Setup Medications Table
        // ============================
        colMedication.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        medicationService.getMedicationById(data.getValue().getMedicationId()).getName()
                )
        );
        colDosage.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getDosage())
        );

        tblMedications.setItems(medications);

    }

    // ============================
    // Add Medication to Table
    // ============================
    @FXML
    private void handleAddMedication() {
        Medication med = cmbMedication.getValue();
        String dosage = txtDosage.getText().trim();

        if (med == null || dosage.isEmpty()) {
            showAlert("Validation Error", "Select a medication and enter dosage");
            return;
        }

        medications.add(new PrescriptionMedication(0, med.getMedicationId(), dosage));

        cmbMedication.getSelectionModel().clearSelection();
        txtDosage.clear();
    }

    // ============================
    // Save Prescription
    // ============================
    @FXML
    private void handleSavePrescription() {
        FullAppointmentReport selectedAppointment = cmbAppointment.getValue();
        if (selectedAppointment == null) {
            showAlert("Validation Error", "Select an appointment");
            return;
        }

        int appointmentId = selectedAppointment.getAppointmentId();

        if (medications.isEmpty()) {
            showAlert("Validation Error", "Add at least one medication");
            return;
        }

        try {
            prescriptionService.addPrescription(
                    new Prescription(0, appointmentId, LocalDate.now(), txtNotes.getText()),
                    medications
            );

            medications.clear();
            txtNotes.clear();
            cmbAppointment.getSelectionModel().clearSelection();

            showAlert("Success", "Prescription saved successfully!");
        } catch (RuntimeException e) {
            showAlert("Error", e.getMessage());
        }
    }

    // ============================
    // Helper: Alert
    // ============================
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}