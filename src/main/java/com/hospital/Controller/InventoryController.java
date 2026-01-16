package com.hospital.Controller;

import com.hospital.Models.DTO.InventoryViewDTO;
import com.hospital.Models.Inventory;
import com.hospital.Models.Medication;
import com.hospital.Service.InventoryService;
import com.hospital.Service.MedicationService; // optional if you wrap DAO
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;


public class InventoryController {

    @FXML private TableView<InventoryViewDTO> tblInventory;
    @FXML private TableColumn<InventoryViewDTO, String> colMedication;
    @FXML private TableColumn<InventoryViewDTO, Integer> colQuantity;
    @FXML private TableColumn<InventoryViewDTO, String> colLastUpdated;

    @FXML private ComboBox<Medication> cmbMedication;
    @FXML private TextField txtQuantity;
    @FXML private Button btnAddStock;
    @FXML private Button btnUpdateStock;
    @FXML private Button btnReduceStock;

    private final InventoryService inventoryService = new InventoryService();
    private final ObservableList<Inventory> inventoryList = FXCollections.observableArrayList();
    private final ObservableList<Medication> medicationList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Bind TableView columns
        colMedication.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getMedicationName()));
        colQuantity.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getQuantity()).asObject());
        colLastUpdated.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getLastUpdated().toString()
                ));

        cmbMedication.setConverter(new StringConverter<>() {
            @Override
            public String toString(Medication medication) {
                return medication == null ? "" : medication.getName();
            }

            @Override
            public Medication fromString(String string) {
                return null; // not needed unless editable
            }
        });


        // Load data
        loadMedications();
        loadInventory();

        // Button actions
        btnAddStock.setOnAction(e -> handleAddStock());
        btnUpdateStock.setOnAction(e -> handleUpdateStock());
        btnReduceStock.setOnAction(e -> handleReduceStock());
    }

    private void loadMedications() {
        MedicationService medicationService = new MedicationService();

        // Load all medications once
        ObservableList<Medication> medications =
                FXCollections.observableArrayList(medicationService.getAllMedications());

        // Keep a local reference if needed elsewhere
        medicationList.setAll(medications);

        // Populate ComboBox
        cmbMedication.setItems(medications);
    }


    private void loadInventory() {
        inventoryList.clear();
        inventoryList.addAll(inventoryService.getAllInventory());
        tblInventory.setItems(FXCollections.observableArrayList(inventoryService.getInventoryView()));
    }

    private void handleUpdateStock() {
        if (cmbMedication.getValue() == null || txtQuantity.getText().isEmpty()) {
            showAlert("Validation Error", "Select medication and enter quantity");
            return;
        }

        int qty = Integer.parseInt(txtQuantity.getText().trim());
        inventoryService.updateStock(cmbMedication.getValue().getMedicationId(), qty);
        showAlert("Success", "Stock Updated successfully");
        loadInventory();
        txtQuantity.clear();
    }

    private void handleAddStock() {
        if (cmbMedication.getValue() == null || txtQuantity.getText().isEmpty()) {
            showAlert("Validation Error", "Select medication and enter quantity");
            return;
        }

        int qty = Integer.parseInt(txtQuantity.getText().trim());
        inventoryService.addStock(cmbMedication.getValue().getMedicationId(), qty);
        showAlert("Success", "Stock saved successfully");
        loadInventory();
        txtQuantity.clear();
    }


    private void handleReduceStock() {
        if (!inventoryService.getLowStockItems().isEmpty()) {
            showAlert("Warning", "Some medications are low on stock!");
        }
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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
