package com.hospital.Controller;

import com.hospital.Models.Department;
import com.hospital.Models.Doctor;
import com.hospital.Service.DepartmentService;
import com.hospital.Service.DoctorService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class DoctorController {

    @FXML private TextField txtFirstName;
    @FXML private TextField txtLastName;
    @FXML private TextField txtSpecialization;
    @FXML private ComboBox<Department> cmbDepartment;

    @FXML private TableView<Doctor> tblDoctors;
    @FXML private TableColumn<Doctor, Integer> colId;
    @FXML private TableColumn<Doctor, String> colFirstName;
    @FXML private TableColumn<Doctor, String> colLastName;
    @FXML private TableColumn<Doctor, String> colSpecialization;
    @FXML private TableColumn<Doctor, String> colDepartment;

    private final DoctorService doctorService = new DoctorService();
    private final DepartmentService departmentService = new DepartmentService();
    private final ObservableList<Doctor> doctorList = FXCollections.observableArrayList();
    private final ObservableList<Department> departmentList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getDoctorId()).asObject());
        colFirstName.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getFirstName()));
        colLastName.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getLastName()));
        colSpecialization.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getSpecialization()));
        colDepartment.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                departmentService.getDepartmentById(data.getValue().getDepartmentId()).getName()
        ));

        loadDepartments();
        loadDoctors();

        tblDoctors.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) populateForm(newSelection);
        });
    }

    private void loadDepartments() {
        departmentList.clear();
        departmentList.addAll(departmentService.getAllDepartments());
        cmbDepartment.setItems(departmentList);
    }

    private void loadDoctors() {
        doctorList.clear();
        doctorList.addAll(doctorService.getAllDoctors());
        tblDoctors.setItems(doctorList);
    }

    private void populateForm(Doctor d) {
        txtFirstName.setText(d.getFirstName());
        txtLastName.setText(d.getLastName());
        txtSpecialization.setText(d.getSpecialization());
        cmbDepartment.setValue(departmentService.getDepartmentById(d.getDepartmentId()));
    }

    // ==============================
    // Validation
    // ==============================
    private boolean validateForm() {
        if (txtFirstName.getText().trim().isEmpty()) {
            showAlert("Validation Error", "First name is required.");
            return true;
        }
        if (txtLastName.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Last name is required.");
            return true;
        }
        if (txtSpecialization.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Specialization is required.");
            return true;
        }
        if (cmbDepartment.getValue() == null) {
            showAlert("Validation Error", "Please select a department.");
            return true;
        }
        return false;
    }

    // ==============================
    // CRUD Handlers
    // ==============================
    @FXML
    private void handleAddDoctor() {
        if (validateForm()) return;

        Doctor d = new Doctor();
        d.setFirstName(txtFirstName.getText().trim());
        d.setLastName(txtLastName.getText().trim());
        d.setSpecialization(txtSpecialization.getText().trim());
        d.setDepartmentId(cmbDepartment.getValue().getDepartmentId());

        doctorService.addDoctor(d);
        loadDoctors();
        clearFields();
    }

    @FXML
    private void handleUpdateDoctor() {
        Doctor selected = tblDoctors.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Selection Error", "Please select a doctor to update");
            return;
        }
        if (validateForm()) return;

        selected.setFirstName(txtFirstName.getText().trim());
        selected.setLastName(txtLastName.getText().trim());
        selected.setSpecialization(txtSpecialization.getText().trim());
        selected.setDepartmentId(cmbDepartment.getValue().getDepartmentId());

        doctorService.updateDoctor(selected);
        loadDoctors();
        clearFields();
    }

    @FXML
    private void handleDeleteDoctor() {
        Doctor selected = tblDoctors.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Selection Error", "Please select a doctor to delete");
            return;
        }

        doctorService.deleteDoctor(selected.getDoctorId());
        loadDoctors();
        clearFields();
    }

    private void clearFields() {
        txtFirstName.clear();
        txtLastName.clear();
        txtSpecialization.clear();
        cmbDepartment.getSelectionModel().clearSelection();
        tblDoctors.getSelectionModel().clearSelection();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
