package com.hospital.Controller;

import com.hospital.Models.DTO.FullAppointmentReport;
import com.hospital.Models.Medication;
import com.hospital.Models.Prescription;
import com.hospital.Models.PrescriptionMedication;
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
    @FXML private Button btnSavePrescription; // Fix fx:id reference

    private final MedicationService medicationService = new MedicationService();
    private final PrescriptionService prescriptionService = new PrescriptionService();
    private final AppointmentService appointmentService = new AppointmentService();

    private final ObservableList<PrescriptionMedication> medications = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        ObservableList<FullAppointmentReport> appointments =
                FXCollections.observableArrayList(appointmentService.getAppointmentsWithoutPrescription());
        cmbAppointment.setItems(appointments);

        cmbAppointment.setConverter(new StringConverter<>() {
            @Override
            public String toString(FullAppointmentReport dto) {
                if (dto == null) return "";
                return dto.getPatientName() + " - " + dto.getDoctorName() + " - " +
                        dto.getAppointmentDate().toLocalDate();
            }

            @Override
            public FullAppointmentReport fromString(String string) {
                return null; // ComboBox not editable
            }
        });

        cmbMedication.setItems(FXCollections.observableArrayList(medicationService.getAllMedications()));
        cmbMedication.setConverter(new StringConverter<>() {
            @Override
            public String toString(Medication medication) {
                return medication == null ? "" : medication.getName();
            }

            @Override
            public Medication fromString(String string) {
                return null;
            }
        });

        colMedication.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        medicationService.getMedicationById(data.getValue().getMedicationId()).getName()
                )
        );
        colDosage.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getDosage())
        );
        tblMedications.setItems(medications);

        btnSavePrescription.setDisable(true); // initially disabled

        // Enable Save button only when there’s at least one medication
        medications.addListener((javafx.collections.ListChangeListener<PrescriptionMedication>) c -> {
            btnSavePrescription.setDisable(medications.isEmpty());
        });

        // Disable Add Medication unless both fields are filled
        txtDosage.textProperty().addListener((obs, oldVal, newVal) -> updateAddButtonState());
        cmbMedication.valueProperty().addListener((obs, oldVal, newVal) -> updateAddButtonState());
    }

    // Enable/disable Add Medication button
    private void updateAddButtonState() {
        boolean disable = cmbMedication.getValue() == null || txtDosage.getText().trim().isEmpty();
        // Assuming your FXML has no fx:id for Add button, you can use lookup:
        Button btnAdd = (Button) tblMedications.getScene().lookup(".button[text='Add Medication']");
        if (btnAdd != null) btnAdd.setDisable(disable);
    }

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

    @FXML
    private void handleSavePrescription() {
        FullAppointmentReport selectedAppointment = cmbAppointment.getValue();
        if (selectedAppointment == null) {
            showAlert("Validation Error", "Select an appointment");
            return;
        }

        if (medications.isEmpty()) {
            showAlert("Validation Error", "Add at least one medication");
            return;
        }

        try {
            Prescription presc = new Prescription(0, selectedAppointment.getAppointmentId(),
                    LocalDate.now(), txtNotes.getText());

            prescriptionService.addPrescription(presc, medications);

            medications.clear();
            txtNotes.clear();
            cmbAppointment.getSelectionModel().clearSelection();

            showAlert("Success", "Prescription saved successfully!");
        } catch (RuntimeException e) {
            showAlert("Error", e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
