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
    }

    @FXML
    private void handleAddMedication() {
        medicationService.addMedication(
                txtName.getText(),
                txtDescription.getText()
        );
        loadMedications();
        clearFields();
    }

    private void loadMedications() {
        medicationList.clear();
        medicationList.addAll(medicationService.getAllMedications());
        tblMedication.setItems(medicationList);
    }

    private void clearFields() {
        txtName.clear();
        txtDescription.clear();
    }
}
