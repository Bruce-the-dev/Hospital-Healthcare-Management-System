package com.hospital.Controller;

import com.hospital.Models.Department;
import com.hospital.Service.DepartmentService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class DepartmentController {

    @FXML
    private TextField txtDepartmentName;

    @FXML
    private TableView<Department> tblDepartments;

    @FXML
    private TableColumn<Department, Integer> colId;

    @FXML
    private TableColumn<Department, String> colName;

    private final DepartmentService departmentService = new DepartmentService();
    private final ObservableList<Department> departmentList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Bind table columns
        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getDepartmentId()).asObject());
        colName.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));

        loadDepartments();

        // Listen for row selection to fill text field
        tblDepartments.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtDepartmentName.setText(newSelection.getName());
            }
        });
    }

    private void loadDepartments() {
        departmentList.clear();
        departmentList.addAll(departmentService.getAllDepartments());
        tblDepartments.setItems(departmentList);
    }

    @FXML
    private void handleAddDepartment() {
        String name = txtDepartmentName.getText().trim();
        if (name.isEmpty()) {
            showAlert("Validation Error", "Department name cannot be empty");
            return;
        }

        Department dept = new Department();
        dept.setName(name);
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

        String name = txtDepartmentName.getText().trim();
        if (name.isEmpty()) {
            showAlert("Validation Error", "Department name cannot be empty");
            return;
        }

        selected.setName(name);
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
