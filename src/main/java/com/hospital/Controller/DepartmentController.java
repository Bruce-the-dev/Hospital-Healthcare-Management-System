package com.hospital.Controller;

import com.hospital.Models.Department;
import com.hospital.Service.DepartmentService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class DepartmentController {

    @FXML private TextField txtDepartmentName;
    @FXML private TextField txtLocation;
    @FXML private TableView<Department> tblDepartments;
    @FXML private TableColumn<Department, Integer> colId;
    @FXML private TableColumn<Department, String> colName;
    @FXML private TableColumn<Department, String> colLocation;

    private final DepartmentService departmentService = new DepartmentService();
    private final ObservableList<Department> departmentList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getDepartmentId()).asObject());
        colName.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        colLocation.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getLocation()));

        loadDepartments();

        tblDepartments.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) populateForm(newSelection);
        });
    }

    private void loadDepartments() {
        departmentList.clear();
        departmentList.addAll(departmentService.getAllDepartments());
        tblDepartments.setItems(departmentList);
    }

    private void populateForm(Department dept) {
        txtDepartmentName.setText(dept.getName());
        txtLocation.setText(dept.getLocation());
    }

    private boolean validateForm() {
        if (txtDepartmentName.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Department name cannot be empty.");
            return true;
        }
        if (txtLocation.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Location cannot be empty.");
            return true;
        }
        return false;
    }

    @FXML
    private void handleAddDepartment() {
        if (validateForm()) return;

        Department dept = new Department();
        dept.setName(txtDepartmentName.getText().trim());
        dept.setLocation(txtLocation.getText().trim());

        departmentService.addDepartment(dept);
        loadDepartments();
        clearFields();
    }

    @FXML
    private void handleUpdateDepartment() {
        Department selected = tblDepartments.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Selection Error", "Please select a department to update");
            return;
        }
        if (validateForm()) return;

        selected.setName(txtDepartmentName.getText().trim());
        selected.setLocation(txtLocation.getText().trim());

        departmentService.updateDepartment(selected);
        loadDepartments();
        clearFields();
    }

    @FXML
    private void handleDeleteDepartment() {
        Department selected = tblDepartments.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Selection Error", "Please select a department to delete");
            return;
        }

        departmentService.deleteDepartment(selected.getDepartmentId());
        loadDepartments();
        clearFields();
    }

    private void clearFields() {
        txtDepartmentName.clear();
        txtLocation.clear();
        tblDepartments.getSelectionModel().clearSelection();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
