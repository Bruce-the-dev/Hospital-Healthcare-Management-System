package com.hospital.Controller;

import com.hospital.Models.Appointment;
import com.hospital.Models.DTO.FullAppointmentReport;
import com.hospital.Models.Doctor;
import com.hospital.Models.Patient;
import com.hospital.Service.AppointmentService;
import com.hospital.Service.DoctorService;
import com.hospital.Service.PatientService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class AppointmentController {

    // ================= ADD MODE CONTROLS =================
    @FXML private ComboBox<Patient> cmbPatient;
    @FXML private ComboBox<Doctor> cmbDoctor;
    @FXML private ComboBox<String> cmbStatus;

    // ================= UPDATE MODE CONTROLS =================
    @FXML private TextField txtPatient;
    @FXML private TextField txtDoctor;
    @FXML private TextField txtStatus;

    // ================= SHARED CONTROLS =================
    @FXML private DatePicker dpDate;
    @FXML private TextField txtTime;

    // ================= TABLE =================
    @FXML private TableView<FullAppointmentReport> tblAppointments;
    @FXML private TableColumn<FullAppointmentReport, Integer> colId;
    @FXML private TableColumn<FullAppointmentReport, String> colPatient;
    @FXML private TableColumn<FullAppointmentReport, String> colDoctor;
    @FXML private TableColumn<FullAppointmentReport, String> colDept;
    @FXML private TableColumn<FullAppointmentReport, LocalDate> colDate;
    @FXML private TableColumn<FullAppointmentReport, String> colTime;
    @FXML private TableColumn<FullAppointmentReport, String> colStatus;

    private final AppointmentService appointmentService = new AppointmentService();
    private final PatientService patientService = new PatientService();
    private final DoctorService doctorService = new DoctorService();

    private final ObservableList<Patient> patientList = FXCollections.observableArrayList();
    private final ObservableList<Doctor> doctorList = FXCollections.observableArrayList();
    private final ObservableList<FullAppointmentReport> appointmentList = FXCollections.observableArrayList();

    // ======================================================
    @FXML
    public void initialize() {

        // ---------- Table bindings ----------
        colId.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(
                        data.getValue().getAppointmentId()
                ).asObject());

        colPatient.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getPatientName()
                ));

        colDoctor.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getDoctorName()
                ));

        colDept.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getDepartmentName()
                ));

        colDate.setCellValueFactory(data ->
                new javafx.beans.property.SimpleObjectProperty<>(
                        data.getValue().getAppointmentDate().toLocalDate()
                ));

        colTime.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getAppointmentDate().toLocalTime().toString()
                ));

        colStatus.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getStatus()
                ));

        // ---------- Status ----------
        cmbStatus.setItems(FXCollections.observableArrayList(
                "Pending", "Completed", "Cancelled"
        ));

        // ---------- ComboBox converters ----------
        cmbPatient.setConverter(new StringConverter<>() {
            @Override
            public String toString(Patient p) {
                return p == null ? "" : p.getFirstName() + " " + p.getLastName();
            }
            @Override public Patient fromString(String s) { return null; }
        });

        cmbDoctor.setConverter(new StringConverter<>() {
            @Override
            public String toString(Doctor d) {
                return d == null ? "" : d.getFirstName() + " " + d.getLastName();
            }
            @Override public Doctor fromString(String s) { return null; }
        });

        loadPatients();
        loadDoctors();
        loadAppointments();

        // ---------- Start in ADD MODE ----------
        showUpdateFields(false);

        // ---------- Table row selection ----------
        tblAppointments.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldSel, selected) -> {
                    if (selected != null) {
                        enterUpdateMode(selected);
                    } else {
                        exitUpdateMode();
                    }
                });
    }

    // ================= MODE SWITCHING =================
    private void showUpdateFields(boolean show) {
        txtPatient.setVisible(show);
        txtPatient.setManaged(show);

        txtDoctor.setVisible(show);
        txtDoctor.setManaged(show);

        txtStatus.setVisible(show);
        txtStatus.setManaged(show);
    }

    private void enterUpdateMode(FullAppointmentReport a) {
        showUpdateFields(true);

        txtPatient.setText(a.getPatientName());
        txtDoctor.setText(a.getDoctorName());
        txtStatus.setText(a.getStatus());

        dpDate.setValue(a.getAppointmentDate().toLocalDate());
        txtTime.setText(a.getAppointmentDate().toLocalTime().toString());
    }

    private void exitUpdateMode() {
        showUpdateFields(false);
        clearFields();
    }


    // ================= LOADERS =================
    private void loadPatients() {
        patientList.setAll(patientService.getAllPatients());
        cmbPatient.setItems(patientList);
    }

    private void loadDoctors() {
        doctorList.setAll(doctorService.getAllDoctors());
        cmbDoctor.setItems(doctorList);
    }

    private void loadAppointments() {
        appointmentList.setAll(appointmentService.getFullAppointmentReport());
        tblAppointments.setItems(appointmentList);
    }
    @FXML
    private void handleCancelEdit() {
        exitUpdateMode();
    }

    // ================= ACTIONS =================
    @FXML
    private void handleAddAppointment() {
        if (cmbPatient.getValue() == null || cmbDoctor.getValue() == null ||
                dpDate.getValue() == null || txtTime.getText().isEmpty() ||
                cmbStatus.getValue() == null) {

            showAlert("Validation Error", "Please fill all fields");
            return;
        }

        LocalDateTime dateTime = LocalDateTime.of(
                dpDate.getValue(),
                LocalTime.parse(txtTime.getText().trim())
        );

        Appointment a = new Appointment();
        a.setPatientId(cmbPatient.getValue().getPatientId());
        a.setDoctorId(cmbDoctor.getValue().getDoctorId());
        a.setAppointmentDate(dateTime);
        a.setStatus(cmbStatus.getValue());

        appointmentService.addAppointment(a);
        loadAppointments();
        clearFields();
    }

    @FXML
    private void handleUpdateAppointment() {
        FullAppointmentReport selected = tblAppointments.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Selection Error", "Please select an appointment to update");
            return;
        }

        LocalDateTime dateTime = LocalDateTime.of(
                dpDate.getValue(),
                LocalTime.parse(txtTime.getText().trim())
        );

        Appointment a = appointmentService.getAppointmentById(selected.getAppointmentId());
        a.setAppointmentDate(dateTime);
        a.setPatientId(cmbPatient.getValue().getPatientId());
        a.setDoctorId(cmbDoctor.getValue().getDoctorId());
        a.setStatus(cmbStatus.getValue());


        appointmentService.updateAppointment(a);
        loadAppointments();
        exitUpdateMode();
    }

    @FXML
    private void handleDeleteAppointment() {
        FullAppointmentReport selected = tblAppointments.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Selection Error", "Please select an appointment to delete");
            return;
        }

        appointmentService.deleteAppointment(selected.getAppointmentId());
        loadAppointments();
        exitUpdateMode();
    }

    // ================= UTIL =================
    private void clearFields() {
        cmbPatient.getSelectionModel().clearSelection();
        cmbDoctor.getSelectionModel().clearSelection();
        cmbStatus.getSelectionModel().clearSelection();
        dpDate.setValue(null);
        txtTime.clear();
        tblAppointments.getSelectionModel().clearSelection();
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
