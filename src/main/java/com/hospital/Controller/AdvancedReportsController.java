package com.hospital.Controller;

import com.hospital.Models.DTO.FullAppointmentReport;
import com.hospital.Models.DTO.PrescriptionReportDTO;
import com.hospital.Models.Inventory;
import com.hospital.Service.AppointmentService;
import com.hospital.Service.PrescriptionService;
import com.hospital.Service.InventoryService;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.time.format.DateTimeFormatter;

public class AdvancedReportsController {

    @FXML
    private TextField txtApptSearch;
    @FXML
    private TextField txtPrescSearch;
    @FXML
    private TextField txtInvSearch;

    @FXML
    private TableView<FullAppointmentReport> tblAppointments;
    @FXML
    private TableColumn<FullAppointmentReport, String> colApptPatient;
    @FXML
    private TableColumn<FullAppointmentReport, String> colApptDoctor;
    @FXML
    private TableColumn<FullAppointmentReport, String> colApptDept;
    @FXML
    private TableColumn<FullAppointmentReport, String> colApptDate;
    @FXML
    private TableColumn<FullAppointmentReport, String> colApptTime;
    @FXML
    private TableColumn<FullAppointmentReport, String> colApptStatus;

    @FXML
    private TableView<PrescriptionReportDTO> tblPrescriptions;
    @FXML
    private TableColumn<PrescriptionReportDTO, String> colPrescPatient;
    @FXML
    private TableColumn<PrescriptionReportDTO, String> colPrescDoctor;
    @FXML
    private TableColumn<PrescriptionReportDTO, String> colPrescMedication;
    @FXML
    private TableColumn<PrescriptionReportDTO, String> colPrescDosage;
    @FXML
    private TableColumn<PrescriptionReportDTO, String> colPrescDate;

    @FXML
    private TableView<Inventory> tblInventory;
    @FXML
    private TableColumn<Inventory, String> colInvMedication;
    @FXML
    private TableColumn<Inventory, Integer> colInvQty;
    @FXML
    private TableColumn<Inventory, String> colInvLastUpdated;

    private final AppointmentService appointmentService = new AppointmentService();
    private final PrescriptionService prescriptionService = new PrescriptionService();
    private final InventoryService inventoryService = new InventoryService();

    private FilteredList<FullAppointmentReport> filteredAppointments;
    private FilteredList<PrescriptionReportDTO> filteredPrescriptions;
    private FilteredList<Inventory> filteredInventory;

    @FXML
    public void initialize() {
        setupAppointmentTab();
        setupPrescriptionTab();
        setupInventoryTab();
        loadData();
        setupSearch();
    }

    private void setupAppointmentTab() {
        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm");

        colApptPatient.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getPatientName()));
        colApptDoctor.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getDoctorName()));
        colApptDept.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getDepartmentName()));
        colApptDate.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getAppointmentDate().toLocalDate().format(dateFmt)));
        colApptTime.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getAppointmentDate().toLocalTime().format(timeFmt)));
        colApptStatus.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getStatus()));
    }

    private void setupPrescriptionTab() {
        colPrescPatient.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getPatientName()));
        colPrescDoctor.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getDoctorName()));
        colPrescMedication.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getMedicationName()));
        colPrescDosage.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getDosage()));
        colPrescDate.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getIssuedDate().toString()));
    }

    private void setupInventoryTab() {
        colInvMedication.setCellValueFactory(d ->
                new SimpleStringProperty(
                        inventoryService.getMedicationNameById(d.getValue().getMedicationId())
                ));
        colInvQty.setCellValueFactory(d ->
                new SimpleIntegerProperty(d.getValue().getQuantity()).asObject());
        colInvLastUpdated.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getLastUpdated().toString()));
    }

    private void loadData() {

        filteredAppointments = new FilteredList<>(
                FXCollections.observableArrayList(
                        appointmentService.getFullAppointmentReport()
                ), p -> true
        );
        tblAppointments.setItems(filteredAppointments);

        filteredPrescriptions = new FilteredList<>(
                FXCollections.observableArrayList(
                        prescriptionService.getAllPrescriptionReports()
                ), p -> true
        );
        tblPrescriptions.setItems(filteredPrescriptions);

        filteredInventory = new FilteredList<>(
                FXCollections.observableArrayList(
                        inventoryService.getAllInventory()
                ), p -> true
        );
        tblInventory.setItems(filteredInventory);
    }

    private void setupSearch() {

        txtApptSearch.textProperty().addListener((obs, o, q) -> {
            String s = q.toLowerCase().trim();
            filteredAppointments.setPredicate(a ->
                    s.isEmpty()
                            || a.getPatientName().toLowerCase().contains(s)
                            || a.getDoctorName().toLowerCase().contains(s)
            );
        });

        txtPrescSearch.textProperty().addListener((obs, o, q) -> {
            String s = q.toLowerCase().trim();
            filteredPrescriptions.setPredicate(p ->
                    s.isEmpty()
                            || p.getPatientName().toLowerCase().contains(s)
                            || p.getDoctorName().toLowerCase().contains(s)
                            || p.getMedicationName().toLowerCase().contains(s)
            );
        });

        txtInvSearch.textProperty().addListener((obs, o, q) -> {
            String s = q.toLowerCase().trim();
            filteredInventory.setPredicate(i ->
                    s.isEmpty()
                            || inventoryService
                            .getMedicationNameById(i.getMedicationId())
                            .toLowerCase()
                            .contains(s)
            );
        });
    }
}
