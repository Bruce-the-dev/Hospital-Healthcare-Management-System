package com.hospital.Controller;

import com.hospital.Models.DTO.InventoryViewDTO;
import com.hospital.Models.Medication;
import com.hospital.Service.InventoryService;
import com.hospital.Service.MedicationService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;

public class InventoryController {

    @FXML
    private TableView<InventoryViewDTO> tblInventory;
    @FXML
    private TableColumn<InventoryViewDTO, String> colMedication;
    @FXML
    private TableColumn<InventoryViewDTO, Integer> colQuantity;
    @FXML
    private TableColumn<InventoryViewDTO, String> colLastUpdated;

    @FXML
    private ComboBox<Medication> cmbMedication;
    @FXML
    private TextField txtQuantity;
    @FXML
    private Button btnAddStock;
    @FXML
    private Button btnUpdateStock;
    @FXML
    private Button btnReduceStock;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnPlus;
    @FXML
    private Label lblLowStock;

    private final InventoryService inventoryService = new InventoryService();
    private final MedicationService medicationService = new MedicationService();
    private final ObservableList<Medication> medicationList = FXCollections.observableArrayList();

    private final int LOW_STOCK_THRESHOLD = 5; // anything <= 5 is low stock

    @FXML
    public void initialize() {
        colMedication.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMedicationName()));
        colQuantity.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getQuantity()).asObject());
        colLastUpdated.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLastUpdated().toString()));

        cmbMedication.setConverter(new StringConverter<>() {
            @Override
            public String toString(Medication medication) {
                return medication == null ? "" : medication.getName();
            }

            @Override
            public Medication fromString(String string) {
                return null;
            }
        });

        loadMedications();
        loadInventory();

        showInputFields(false);

        tblInventory.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, selected) -> {
            btnReduceStock.setDisable(selected == null);
            if (selected != null) enterUpdateMode(selected);
            else clearInputFields();
        });

        txtQuantity.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                txtQuantity.setText(newVal.replaceAll("\\D", ""));
            }
        });

        // ---------- Row color for low stock ----------
        tblInventory.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(InventoryViewDTO item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else if (item.getQuantity() <= LOW_STOCK_THRESHOLD) {
                    setStyle("-fx-background-color: #ffcccc;"); // light red
                } else {
                    setStyle("");
                }
            }
        });
    }

    private void showInputFields(boolean show) {
        cmbMedication.setVisible(show);
        cmbMedication.setManaged(show);
        txtQuantity.setVisible(show);
        txtQuantity.setManaged(show);
        btnAddStock.setVisible(show);
        btnAddStock.setManaged(show);
        btnUpdateStock.setVisible(show);
        btnUpdateStock.setManaged(show);
        btnCancel.setVisible(show);
        btnCancel.setManaged(show);

        btnPlus.setVisible(!show);
    }

    private void enterUpdateMode(InventoryViewDTO item) {
        showInputFields(true);
        cmbMedication.getSelectionModel().select(
                medicationList.stream()
                        .filter(m -> m.getName().equals(item.getMedicationName()))
                        .findFirst()
                        .orElse(null)
        );
        txtQuantity.setText(String.valueOf(item.getQuantity()));
        btnAddStock.setVisible(false);
        btnUpdateStock.setVisible(true);
        btnCancel.setVisible(true);
    }

    @FXML
    private void handlePlus() {
        showInputFields(true);
        btnAddStock.setVisible(true);
        btnUpdateStock.setVisible(false);
        btnCancel.setVisible(true);
        clearInputFields();
    }

    @FXML
    private void handleCancel() {
        showInputFields(false);
        clearInputFields();
    }

    @FXML
    private void handleAddStock() {
        if (validateInput()) return;

        int qty = Integer.parseInt(txtQuantity.getText().trim());
        inventoryService.addStock(cmbMedication.getValue().getMedicationId(), qty);
        showAlert("Success", "Stock added successfully");
        loadInventory();
        handleCancel();
    }

    @FXML
    private void handleUpdateStock() {
        if (validateInput()) return;

        int qty = Integer.parseInt(txtQuantity.getText().trim());
        inventoryService.updateStock(cmbMedication.getValue().getMedicationId(), qty);
        showAlert("Success", "Stock updated successfully");
        loadInventory();
        handleCancel();
    }

    @FXML
    private void handleReduceStock() {
        InventoryViewDTO selected = tblInventory.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        TextInputDialog dialog = new TextInputDialog("1");
        dialog.setTitle("Reduce Stock");
        dialog.setHeaderText("Enter quantity to reduce:");
        dialog.setContentText("Quantity:");

        dialog.showAndWait().ifPresent(qtyText -> {
            try {
                int qty = Integer.parseInt(qtyText.trim());
                if (qty <= 0) {
                    showAlert("Error", "Quantity must be greater than zero");
                    return;
                }
                boolean success = inventoryService.deductStock(selected.getMedicationId(), qty);
                if (success) showAlert("Success", "Stock reduced successfully");
                else showAlert("Error", "Insufficient stock");
                loadInventory();
            } catch (NumberFormatException e) {
                showAlert("Error", "Quantity must be a number");
            }
        });
    }

    private void loadMedications() {
        medicationList.setAll(medicationService.getAllMedications());
        cmbMedication.setItems(medicationList);
    }

    private void loadInventory() {
        ObservableList<InventoryViewDTO> inventory = FXCollections.observableArrayList(inventoryService.getInventoryView());
        tblInventory.setItems(inventory);

        long lowCount = inventory.stream().filter(i -> i.getQuantity() <= LOW_STOCK_THRESHOLD).count();
        lblLowStock.setText(lowCount > 0 ? "⚠ Low stock items: " + lowCount : "");
    }

    private boolean validateInput() {
        if (cmbMedication.getValue() == null) {
            showAlert("Validation Error", "Please select a medication");
            return true;
        }
        String qtyText = txtQuantity.getText().trim();
        if (qtyText.isEmpty()) {
            showAlert("Validation Error", "Quantity cannot be empty");
            return true;
        }
        try {
            int qty = Integer.parseInt(qtyText);
            if (qty <= 0) {
                showAlert("Validation Error", "Quantity must be greater than zero");
                return true;
            }
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Quantity must be a valid number");
            return true;
        }
        return false;
    }

    private void clearInputFields() {
        cmbMedication.getSelectionModel().clearSelection();
        txtQuantity.clear();
        tblInventory.getSelectionModel().clearSelection();
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
