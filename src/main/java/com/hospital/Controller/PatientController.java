package com.hospital.Controller;

import com.hospital.Models.Patient;
import com.hospital.Service.PatientService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDate;

public class PatientController {

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private TextField searchField;
    @FXML private DatePicker dobPicker;
    @FXML private ComboBox<String> genderBox;

    @FXML private TableView<Patient> patientTable;
    @FXML private TableColumn<Patient, Integer> idCol;
    @FXML private TableColumn<Patient, String> firstNameCol;
    @FXML private TableColumn<Patient, String> lastNameCol;
    @FXML private TableColumn<Patient, String> genderCol;
    @FXML private TableColumn<Patient, LocalDate> dobCol;
    @FXML private TableColumn<Patient, String> phoneCol;
    @FXML private TableColumn<Patient, String> emailCol;

    private final PatientService patientService = new PatientService();
    private final ObservableList<Patient> patientList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        genderBox.getItems().addAll("M", "F");

        idCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getPatientId()).asObject());
        firstNameCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getFirstName()));
        lastNameCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getLastName()));
        genderCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(String.valueOf(data.getValue().getGender())));
        dobCol.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getDateOfBirth()));
        phoneCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getPhone()));
        emailCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEmail()));

        loadPatients();

        patientTable.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldVal, newVal) -> populateForm(newVal));
    }

    private void loadPatients() {
        patientList.setAll(patientService.getAllPatients());
        patientTable.setItems(patientList);
    }

    private void populateForm(Patient p) {
        if (p == null) return;
        firstNameField.setText(p.getFirstName());
        lastNameField.setText(p.getLastName());
        phoneField.setText(p.getPhone());
        emailField.setText(p.getEmail());
        dobPicker.setValue(p.getDateOfBirth());
        genderBox.setValue(String.valueOf(p.getGender()));
    }

    @FXML
    private void handleAdd() {
        Patient p = new Patient();
        p.setFirstName(firstNameField.getText());
        p.setLastName(lastNameField.getText());
        if (genderBox.getValue() == null) return;
        p.setGender(genderBox.getValue().charAt(0));
        p.setDateOfBirth(dobPicker.getValue());
        p.setPhone(phoneField.getText());
        p.setEmail(emailField.getText());

        boolean result = patientService.addPatient(p);
        System.out.println(result);
        loadPatients();
        handleClear();
    }

    @FXML
    private void handleUpdate() {
        Patient p = patientTable.getSelectionModel().getSelectedItem();
        if (p == null) return;

        p.setFirstName(firstNameField.getText());
        p.setLastName(lastNameField.getText());
        p.setGender(genderBox.getValue().charAt(0));
        p.setDateOfBirth(dobPicker.getValue());
        p.setPhone(phoneField.getText());
        p.setEmail(emailField.getText());

        boolean result = patientService.updatePatient(p);
        System.out.println(result);
        loadPatients();
    }

    @FXML
    private void handleDelete() {
        Patient p = patientTable.getSelectionModel().getSelectedItem();
        if (p == null) return;

boolean result= patientService.deletePatient(p.getPatientId());
        System.out.println(result);
        loadPatients();
        handleClear();
    }

    @FXML
    private void handleSearch() {
        if (searchField.getText().isBlank()) {
            loadPatients();
        } else {
            patientList.setAll(
                    patientService.searchByLastName(searchField.getText())
            );
        }}

    @FXML
    private void handleClear() {
        firstNameField.clear();
        lastNameField.clear();
        phoneField.clear();
        emailField.clear();
        dobPicker.setValue(null);
        genderBox.setValue(null);
        patientTable.getSelectionModel().clearSelection();
    }
}
