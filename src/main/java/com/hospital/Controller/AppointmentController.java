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

    // ================= CONTROLS =================
    @FXML private ComboBox<Patient> cmbPatient;
    @FXML private ComboBox<Doctor> cmbDoctor;
    @FXML private ComboBox<String> cmbStatus;

    @FXML private TextField txtPatient;
    @FXML private TextField txtDoctor;
    @FXML private TextField txtStatus;

    @FXML private DatePicker dpDate;
    @FXML private TextField txtTime;

    @FXML private TableView<FullAppointmentReport> tblAppointments;
    @FXML private TableColumn<FullAppointmentReport, Integer> colId;
    @FXML private TableColumn<FullAppointmentReport, String> colPatient;
    @FXML private TableColumn<FullAppointmentReport, String> colDoctor;
    @FXML private TableColumn<FullAppointmentReport, String> colDept;
    @FXML private TableColumn<FullAppointmentReport, LocalDate> colDate;
    @FXML private TableColumn<FullAppointmentReport, String> colTime;
    @FXML private TableColumn<FullAppointmentReport, String> colStatus;

    @FXML private Button btnPlus;
    @FXML private Button btnAdd;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;
    @FXML private Button btnCancel;

    private final AppointmentService appointmentService = new AppointmentService();
    private final PatientService patientService = new PatientService();
    private final DoctorService doctorService = new DoctorService();

    private final ObservableList<Patient> patientList = FXCollections.observableArrayList();
    private final ObservableList<Doctor> doctorList = FXCollections.observableArrayList();
    private final ObservableList<FullAppointmentReport> appointmentList = FXCollections.observableArrayList();

    // ================= INITIALIZE =================
    @FXML
    public void initialize() {
        // ---------- Table bindings ----------
        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getAppointmentId()).asObject());
        colPatient.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getPatientName()));
        colDoctor.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDoctorName()));
        colDept.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDepartmentName()));
        colDate.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getAppointmentDate().toLocalDate()));
        colTime.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getAppointmentDate().toLocalTime().toString()));
        colStatus.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getStatus()));

        // ---------- Status ----------
        cmbStatus.setItems(FXCollections.observableArrayList("Pending", "Completed", "Cancelled"));

        // ---------- ComboBox converters ----------
        cmbPatient.setConverter(new StringConverter<>() {
            @Override
            public String toString(Patient p) { return p == null ? "" : p.getFirstName() + " " + p.getLastName(); }
            @Override public Patient fromString(String s) { return null; }
        });

        cmbDoctor.setConverter(new StringConverter<>() {
            @Override
            public String toString(Doctor d) { return d == null ? "" : d.getFirstName() + " " + d.getLastName(); }
            @Override public Doctor fromString(String s) { return null; }
        });

        loadPatients();
        loadDoctors();
        loadAppointments();

        // Start in table-only view
        setViewMode();

        // Table row selection → update mode
        tblAppointments.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, selected) -> {
            if (selected != null) enterUpdateMode(selected);
        });
    }

    // ================= VIEW MODES =================
    private void setViewMode() {
        // Hide form fields
        cmbPatient.setVisible(false); cmbPatient.setManaged(false);
        cmbDoctor.setVisible(false); cmbDoctor.setManaged(false);
        cmbStatus.setVisible(false); cmbStatus.setManaged(false);
        dpDate.setVisible(false); dpDate.setManaged(false);
        txtTime.setVisible(false); txtTime.setManaged(false);

        txtPatient.setVisible(false); txtPatient.setManaged(false);
        txtDoctor.setVisible(false); txtDoctor.setManaged(false);
        txtStatus.setVisible(false); txtStatus.setManaged(false);

        // Buttons
        btnAdd.setVisible(false); btnAdd.setManaged(false);
        btnUpdate.setVisible(false); btnUpdate.setManaged(false);
        btnCancel.setVisible(false); btnCancel.setManaged(false);

        btnPlus.setVisible(true); btnPlus.setManaged(true);
        btnDelete.setVisible(true); btnDelete.setManaged(true);

        clearFields();
        tblAppointments.getSelectionModel().clearSelection();
    }

    private void enterAddMode() {
        cmbPatient.setVisible(true); cmbPatient.setManaged(true);
        cmbDoctor.setVisible(true); cmbDoctor.setManaged(true);
        dpDate.setVisible(true); dpDate.setManaged(true);
        txtTime.setVisible(true); txtTime.setManaged(true);

        btnAdd.setVisible(true); btnAdd.setManaged(true);
        btnCancel.setVisible(true); btnCancel.setManaged(true);

        // Hide unnecessary buttons
        btnPlus.setVisible(false); btnPlus.setManaged(false);
        btnUpdate.setVisible(false); btnUpdate.setManaged(false);
        cmbStatus.setVisible(false); cmbStatus.setManaged(false);
        txtPatient.setVisible(false); txtPatient.setManaged(false);
        txtDoctor.setVisible(false); txtDoctor.setManaged(false);
        txtStatus.setVisible(false); txtStatus.setManaged(false);

        clearFields();
    }

    private void enterUpdateMode(FullAppointmentReport a) {
        cmbPatient.setVisible(true); cmbPatient.setManaged(true);
        cmbDoctor.setVisible(true); cmbDoctor.setManaged(true);
        dpDate.setVisible(true); dpDate.setManaged(true);
        txtTime.setVisible(true); txtTime.setManaged(true);
        cmbStatus.setVisible(true); cmbStatus.setManaged(true);

        txtPatient.setVisible(false); txtPatient.setManaged(false);
        txtDoctor.setVisible(false); txtDoctor.setManaged(false);
        txtStatus.setVisible(false); txtStatus.setManaged(false);

        // Buttons
        btnUpdate.setVisible(true); btnUpdate.setManaged(true);
        btnCancel.setVisible(true); btnCancel.setManaged(true);

        btnAdd.setVisible(false); btnAdd.setManaged(false);
        btnPlus.setVisible(false); btnPlus.setManaged(false);

        // Fill fields
        cmbPatient.setValue(patientList.stream()
                .filter(p -> (p.getFirstName() + " " + p.getLastName()).equals(a.getPatientName()))
                .findFirst().orElse(null));
        cmbDoctor.setValue(doctorList.stream()
                .filter(d -> (d.getFirstName() + " " + d.getLastName()).equals(a.getDoctorName()))
                .findFirst().orElse(null));

        dpDate.setValue(a.getAppointmentDate().toLocalDate());
        txtTime.setText(a.getAppointmentDate().toLocalTime().toString());
        cmbStatus.setValue(a.getStatus());
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

    // ================= BUTTON HANDLERS =================
    @FXML
    private void handlePlus() { enterAddMode(); }

    @FXML
    private void handleCancelEdit() { setViewMode(); }

    @FXML
    private void handleAddAppointment() {
        if (cmbPatient.getValue() == null || cmbDoctor.getValue() == null ||
                dpDate.getValue() == null || txtTime.getText().isEmpty()) {
            showAlert("Validation Error", "Please fill all fields");
            return;
        }

        LocalDateTime dateTime;
        try {
            String timeText = txtTime.getText().trim();
            if (timeText.matches("\\d:\\d{2}")) timeText = "0" + timeText;
            dateTime = LocalDateTime.of(dpDate.getValue(), LocalTime.parse(timeText));
        } catch (Exception e) {
            showAlert("Validation Error", "Time must be in HH:mm format");
            return;
        }

        // Prevent past appointments
        if (dateTime.isBefore(LocalDateTime.now())) {
            showAlert("Validation Error", "Appointment cannot be in the past");
            return;
        }

        // Prevent double booking
        boolean conflict = appointmentList.stream().anyMatch(a ->
                a.getDoctorName().equals(cmbDoctor.getValue().getFirstName() + " " + cmbDoctor.getValue().getLastName()) &&
                        a.getAppointmentDate().equals(dateTime)
        );
        if (conflict) {
            showAlert("Validation Error", "This doctor already has an appointment at this time");
            return;
        }

        Appointment a = new Appointment();
        a.setPatientId(cmbPatient.getValue().getPatientId());
        a.setDoctorId(cmbDoctor.getValue().getDoctorId());
        a.setAppointmentDate(dateTime);
        a.setStatus("Pending");

        appointmentService.addAppointment(a);
        showInfo("Appointment has been successfully saved.");
        loadAppointments();
        setViewMode();
    }

    @FXML
    private void handleUpdateAppointment() {
        FullAppointmentReport selected = tblAppointments.getSelectionModel().getSelectedItem();
        if (selected == null) { showAlert("Selection Error", "Please select an appointment"); return; }

        LocalDateTime dateTime;
        try { dateTime = LocalDateTime.of(dpDate.getValue(), LocalTime.parse(txtTime.getText().trim())); }
        catch (Exception e) { showAlert("Validation Error", "Time must be in HH:mm format"); return; }

        Appointment a = appointmentService.getAppointmentById(selected.getAppointmentId());
        a.setAppointmentDate(dateTime);
        if (cmbPatient.getValue() != null) a.setPatientId(cmbPatient.getValue().getPatientId());
        if (cmbDoctor.getValue() != null) a.setDoctorId(cmbDoctor.getValue().getDoctorId());
        a.setStatus(cmbStatus.getValue());

        appointmentService.updateAppointment(a);
        showInfo("Appointment has been successfully updated.");
        loadAppointments();
        setViewMode();
    }


    @FXML
    private void handleDeleteAppointment() {
        FullAppointmentReport selected = tblAppointments.getSelectionModel().getSelectedItem();
        if (selected == null) { showAlert("Selection Error", "Please select an appointment"); return; }

        appointmentService.deleteAppointment(selected.getAppointmentId());
        loadAppointments();
        setViewMode();
    }

    // ================= UTIL =================
    private void clearFields() {
        cmbPatient.getSelectionModel().clearSelection();
        cmbDoctor.getSelectionModel().clearSelection();
        cmbStatus.getSelectionModel().clearSelection();
        dpDate.setValue(null);
        txtTime.clear();
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
    private void showInfo(String msg) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText(msg);
            alert.showAndWait();

    }
}
