package com.hospital.Controller;

import com.hospital.Models.DTO.MedicationUsageDTO;
import com.hospital.Models.DTO.PatientPrescriptionDTO;
import com.hospital.Service.ReportService;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.TextField;

public class ReportsController {

    @FXML private TextField txtMedicationSearch;
    @FXML private TextField txtPatientSearch;


    private FilteredList<MedicationUsageDTO> filteredMedicationUsage;
    private FilteredList<PatientPrescriptionDTO> filteredPatientPrescriptions;

    @FXML private TableView<MedicationUsageDTO> tblMedicationUsage;
    @FXML private TableColumn<MedicationUsageDTO, String> colMedicationName;
    @FXML private TableColumn<MedicationUsageDTO, Integer> colTimesUsed;

    @FXML private TableView<PatientPrescriptionDTO> tblPatientPrescriptions;
    @FXML private TableColumn<PatientPrescriptionDTO, String> colPatientName;
    @FXML private TableColumn<PatientPrescriptionDTO, Integer> colPrescriptionCount;

    private final ReportService reportService = new ReportService();

    @FXML
    public void initialize() {
        // Medication Usage table setup
        colMedicationName.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getMedicationName()));
        colTimesUsed.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getTimesUsed()).asObject());

        filteredMedicationUsage = new FilteredList<>(FXCollections.observableArrayList(
                reportService.getMedicationUsage()
        ), p -> true);

        tblMedicationUsage.setItems(filteredMedicationUsage);

        // Live search for medication
        txtMedicationSearch.textProperty().addListener((obs, oldVal, newVal) -> {
            String lower = newVal.toLowerCase().trim();
            filteredMedicationUsage.setPredicate(dto -> dto.getMedicationName().toLowerCase().contains(lower));
        });

        // Prescriptions per Patient table setup
        colPatientName.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getPatientName()));
        colPrescriptionCount.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getTotalPrescriptions()).asObject());

        filteredPatientPrescriptions = new FilteredList<>(FXCollections.observableArrayList(
                reportService.getPrescriptionsPerPatient()
        ), p -> true);

        tblPatientPrescriptions.setItems(filteredPatientPrescriptions);

        // Live search for patient
        txtPatientSearch.textProperty().addListener((obs, oldVal, newVal) -> {
            String lower = newVal.toLowerCase().trim();
            filteredPatientPrescriptions.setPredicate(dto -> dto.getPatientName().toLowerCase().contains(lower));
        });
    }
    // Clear buttons
    @FXML
    private void clearMedicationSearch() {
        txtMedicationSearch.clear();
    }

    @FXML
    private void clearPatientSearch() {
        txtPatientSearch.clear();
    }
}
