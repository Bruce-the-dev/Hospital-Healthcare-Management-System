package com.hospital.Controller;

import com.hospital.Models.Inventory;
import com.hospital.Models.Medication;
import com.hospital.Service.InventoryService;
//import com.hospital.Service.MedicationService; // optional if you wrap DAO
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;


public class InventoryController {

    @FXML private TableView<Inventory> tblInventory;
    @FXML private TableColumn<Inventory, String> colMedication;
    @FXML private TableColumn<Inventory, Integer> colQuantity;
    @FXML private TableColumn<Inventory, String> colLastUpdated;

    @FXML private ComboBox<Medication> cmbMedication;
    @FXML private TextField txtQuantity;
    @FXML private Button btnAddStock;
    @FXML private Button btnReduceStock;

    private final InventoryService inventoryService = new InventoryService();
    private final ObservableList<Inventory> inventoryList = FXCollections.observableArrayList();
    private final ObservableList<Medication> medicationList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Bind TableView columns
        colMedication.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        getMedicationName(data.getValue().getMedicationId())
                ));
        colQuantity.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(data.getValue().getQuantity()).asObject());
        colLastUpdated.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getLastUpdated().toString()
                ));

        // Load data
        loadMedications();
        loadInventory();

        // Button actions
        btnAddStock.setOnAction(e -> handleAddStock());
        btnReduceStock.setOnAction(e -> handleReduceStock());
    }

    private void loadMedications() {
        medicationList.clear();
        medicationList.addAll(inventoryService.getAllInventory().stream()
                .map(inv -> new Medication(inv.getMedicationId(), "", ""))
                .toList()); // simple placeholder names; ideally fetch from MedicationDAO
        cmbMedication.setItems(medicationList);
    }

    private void loadInventory() {
        inventoryList.clear();
        inventoryList.addAll(inventoryService.getAllInventory());
        tblInventory.setItems(inventoryList);
    }

    private void handleAddStock() {
        if (cmbMedication.getValue() == null || txtQuantity.getText().isEmpty()) {
            showAlert("Validation Error", "Select medication and enter quantity");
            return;
        }

        int qty = Integer.parseInt(txtQuantity.getText().trim());
        inventoryService.addStock(cmbMedication.getValue().getMedicationId(), qty);
        showAlert("Success", "Stock added successfully");
        loadInventory();
        txtQuantity.clear();
    }

    private void handleReduceStock() {
        if (cmbMedication.getValue() == null || txtQuantity.getText().isEmpty()) {
            showAlert("Validation Error", "Select medication and enter quantity");
            return;
        }

        int qty = Integer.parseInt(txtQuantity.getText().trim());
        boolean success = inventoryService.deductStock(cmbMedication.getValue().getMedicationId(), qty);
        if (success) {
            showAlert("Success", "Stock reduced successfully");
            loadInventory();
        } else {
            showAlert("Error", "Insufficient stock");
        }
        txtQuantity.clear();
    }

    private String getMedicationName(int medicationId) {
        // In a full system, you would fetch from MedicationDAO
        return "Med #" + medicationId;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
