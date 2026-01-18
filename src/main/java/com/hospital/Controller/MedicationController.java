package com.hospital.Controller;

import com.hospital.Models.Medication;
import com.hospital.Service.MedicationService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class MedicationController {

    @FXML private TextField txtName;
    @FXML private TextArea txtDescription;

    @FXML private TableView<Medication> tblMedication;
    @FXML private TableColumn<Medication, String> colName;
    @FXML private TableColumn<Medication, String> colDescription;

    private final MedicationService medicationService = new MedicationService();
    private final ObservableList<Medication> medicationList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colName.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        colDescription.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getDescription()));

        loadMedications();

        // Populate form when a row is selected
        tblMedication.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) populateForm(newSelection);
        });
    }

    private void populateForm(Medication m) {
        txtName.setText(m.getName());
        txtDescription.setText(m.getDescription());
    }

    // ===================== Validation =====================
    private boolean validateForm() {
        if (txtName.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Medication name cannot be empty.");
            return false;
        }
        if (txtDescription.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Medication description cannot be empty.");
            return false;
        }
        return true;
    }

    // ===================== CRUD =====================
    @FXML
    private void handleAddMedication() {
        if (!validateForm()) return;

        medicationService.addMedication(
                txtName.getText().trim(),
                txtDescription.getText().trim()
        );
        loadMedications();
        clearFields();
    }

    @FXML
    private void handleUpdateMedication() {
        Medication selected = tblMedication.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Selection Error", "Please select a medication to update.");
            return;
        }
        if (!validateForm()) return;

        selected.setName(txtName.getText().trim());
        selected.setDescription(txtDescription.getText().trim());

        medicationService.updateMedication(selected);
        loadMedications();
        clearFields();
    }

    @FXML
    private void handleDeleteMedication() {
        Medication selected = tblMedication.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Selection Error", "Please select a medication to delete.");
            return;
        }

        medicationService.deleteMedication(selected.getMedicationId());
        loadMedications();
        clearFields();
    }

    private void loadMedications() {
        medicationList.clear();
        medicationList.addAll(medicationService.getAllMedications());
        tblMedication.setItems(medicationList);
    }

    @FXML
    private void clearFields() {
        txtName.clear();
        txtDescription.clear();
        tblMedication.getSelectionModel().clearSelection();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
