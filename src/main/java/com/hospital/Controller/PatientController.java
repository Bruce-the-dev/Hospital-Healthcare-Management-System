package com.hospital.Controller;

import com.hospital.Models.Patient;
import com.hospital.Service.PatientService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDate;
import java.util.regex.Pattern;

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
    @FXML private TextField txtSearch;

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

        // ---------------- Live search ----------------
        txtSearch.textProperty().addListener((obs, oldText, newText) -> {
            searchPatients(newText);
        });
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

    private boolean validateForm() {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        LocalDate dob = dobPicker.getValue();
        String gender = genderBox.getValue();

        if (firstName.isEmpty()) {
            showAlert("Validation Error", "First name is required.");
            return true;
        }

        if (lastName.isEmpty()) {
            showAlert("Validation Error", "Last name is required.");
            return true;
        }

        if (gender == null || (!gender.equals("M") && !gender.equals("F"))) {
            showAlert("Validation Error", "Select a valid gender.");
            return true;
        }

        if (dob == null || dob.isAfter(LocalDate.now())) {
            showAlert("Validation Error", "Select a valid date of birth.");
            return true;
        }

        if (!phone.matches("\\d{10,15}")) { // 10-15 digits
            showAlert("Validation Error", "Enter a valid phone number (10-15 digits).");
            return true;
        }

        if (!email.isEmpty() && !isValidEmail(email)) {
            showAlert("Validation Error", "Enter a valid email address.");
            return true;
        }

        return false;
    }

    private boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile("^[\\w-.]+@[\\w-]+\\.[a-zA-Z]{2,}$");
        return pattern.matcher(email).matches();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    private void handleAdd() {
        if (validateForm()) return;

        Patient p = new Patient();
        p.setFirstName(firstNameField.getText().trim());
        p.setLastName(lastNameField.getText().trim());
        p.setGender(genderBox.getValue().charAt(0));
        p.setDateOfBirth(dobPicker.getValue());
        p.setPhone(phoneField.getText().trim());
        p.setEmail(emailField.getText().trim());

        boolean result = patientService.addPatient(p);
        if (result) showAlert("Success", "Patient added successfully!");
        loadPatients();
        handleClear();
    }

    @FXML
    private void handleUpdate() {
        Patient p = patientTable.getSelectionModel().getSelectedItem();
        if (p == null) return;

        if (validateForm()) return;

        p.setFirstName(firstNameField.getText().trim());
        p.setLastName(lastNameField.getText().trim());
        p.setGender(genderBox.getValue().charAt(0));
        p.setDateOfBirth(dobPicker.getValue());
        p.setPhone(phoneField.getText().trim());
        p.setEmail(emailField.getText().trim());

        boolean result = patientService.updatePatient(p);
        if (result) showAlert("Success", "Patient updated successfully!");
        loadPatients();
    }

    @FXML
    private void handleDelete() {
        Patient p = patientTable.getSelectionModel().getSelectedItem();
        if (p == null) return;

        boolean result = patientService.deletePatient(p.getPatientId());
        if (result) showAlert("Success", "Patient deleted successfully!");
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
        }
    }

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


private void searchPatients(String query) {
    if (query == null || query.isEmpty()) {
        loadPatients();
        return;
    }
    patientList.setAll(patientService.searchByLastName(query));
}

}
