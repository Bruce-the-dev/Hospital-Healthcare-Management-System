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
    private TextField txtLocation;

    @FXML
    private TableView<Department> tblDepartments;

    @FXML
    private TableColumn<Department, Integer> colId;

    @FXML
    private TableColumn<Department, String> colName;

    @FXML
    private TableColumn<Department, String> colLocation;

    private final DepartmentService departmentService = new DepartmentService();
    private final ObservableList<Department> departmentList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Bind table columns
        colId.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(
                        data.getValue().getDepartmentId()
                ).asObject());

        colName.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getName()
                ));

        colLocation.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getLocation()
                ));

        loadDepartments();

        // Fill form when table row is selected
        tblDepartments.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        txtDepartmentName.setText(newSelection.getName());
                        txtLocation.setText(newSelection.getLocation());
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
        String location = txtLocation.getText().trim();

        if (name.isEmpty() || location.isEmpty()) {
            showAlert("Validation Error", "Department name and location cannot be empty");
            return;
        }

        Department dept = new Department();
        dept.setName(name);
        dept.setLocation(location);

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
        String location = txtLocation.getText().trim();

        if (name.isEmpty() || location.isEmpty()) {
            showAlert("Validation Error", "Department name and location cannot be empty");
            return;
        }

        selected.setName(name);
        selected.setLocation(location);

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
