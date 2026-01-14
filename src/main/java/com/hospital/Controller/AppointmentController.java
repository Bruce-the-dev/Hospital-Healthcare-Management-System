package com.hospital.Controller;

import com.hospital.Models.Appointment;
import com.hospital.Models.DTO.FullAppointmentReport;
import com.hospital.Models.Doctor;
import com.hospital.Models.Patient;
import com.hospital.Service.AppointmentService;
import com.hospital.Service.DepartmentService;
import com.hospital.Service.DoctorService;
import com.hospital.Service.PatientService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class AppointmentController {

    @FXML private ComboBox<Patient> cmbPatient;

    @FXML private ComboBox<Doctor> cmbDoctor;
    @FXML private DatePicker dpDate;
    @FXML private TextField txtTime;
    @FXML private ComboBox<String> cmbStatus;



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
    private final DepartmentService departmentService = new DepartmentService();

    private final ObservableList<Patient> patientList = FXCollections.observableArrayList();
    private final ObservableList<Doctor> doctorList = FXCollections.observableArrayList();
    private final ObservableList<FullAppointmentReport> appointmentList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // TableView bindings
        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getAppointmentId()).asObject());
        colPatient.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getPatientName()));
        colDoctor.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDoctorName()));
        colDept.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDepartmentName()));
        colDate.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getAppointmentDate().toLocalDate()));
        colTime.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getAppointmentDate().toLocalTime().toString()));
        colStatus.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getStatus()));
        cmbStatus.setItems(
                FXCollections.observableArrayList(
                        "Pending",
                        "Completed",
                        "Cancelled"
                )
        );
        //Extract only the name from query result
        cmbPatient.setConverter(new StringConverter<>() {
            @Override
            public String toString(Patient patient) {
                return patient == null ? "" : patient.getFirstName()+" "+ patient.getLastName();
            }

            @Override
            public Patient fromString(String string) {
                return null; // not needed
            }

        });
        cmbDoctor.setConverter(new StringConverter<>() {
            @Override
            public String toString(Doctor doctor) {
                return doctor == null ? "" : doctor.getFirstName()+" "+doctor.getLastName();
            }

            @Override
            public Doctor fromString(String string) {
                return null;
            }
        });

        loadPatients();
        loadDoctors();
        loadAppointments();

        // Populate fields on row selection
        tblAppointments.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                // Set patient
                Patient p = null;
                try {
                    p = patientService.getPatientById(newSel.getPatientId());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                cmbPatient.setValue(p);
                // Set doctor
                Doctor d = doctorService.getDoctorById(newSel.getDoctorId());
                cmbDoctor.setValue(d);
                // Set date & time
                dpDate.setValue(newSel.getAppointmentDate().toLocalDate());
                txtTime.setText(newSel.getAppointmentDate().toLocalTime().toString());
                cmbStatus.setValue(newSel.getStatus());
            }
        });
    }

    private void loadPatients() {
        patientList.clear();
        patientList.addAll(patientService.getAllPatients());
        cmbPatient.setItems(patientList);
    }

    private void loadDoctors() {
        doctorList.clear();
        doctorList.addAll(doctorService.getAllDoctors());
        cmbDoctor.setItems(doctorList);
    }

    private void loadAppointments() {
        appointmentList.clear();
        appointmentList.addAll(appointmentService.getFullAppointmentReport());
        tblAppointments.setItems(appointmentList);
    }

    @FXML
    private void handleAddAppointment() {
        if (cmbPatient.getValue() == null || cmbDoctor.getValue() == null || dpDate.getValue() == null || txtTime.getText().isEmpty() || cmbStatus.getValue() == null) {
            showAlert("Validation Error", "Please fill all fields");
            return;
        }

        LocalDate date = dpDate.getValue();
        LocalTime time = LocalTime.parse(txtTime.getText().trim());
        LocalDateTime dateTime = LocalDateTime.of(date, time);

        Appointment appointment = new Appointment();
        appointment.setPatientId(cmbPatient.getValue().getPatientId());
        appointment.setDoctorId(cmbDoctor.getValue().getDoctorId());
        appointment.setAppointmentDate(dateTime);
        appointment.setStatus(cmbStatus.getValue());

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

        if (cmbPatient.getValue() == null || cmbDoctor.getValue() == null || dpDate.getValue() == null || txtTime.getText().isEmpty() || cmbStatus.getValue() == null) {
            showAlert("Validation Error", "Please fill all fields");
            return;
        }

        LocalDate date = dpDate.getValue();
        LocalTime time = LocalTime.parse(txtTime.getText().trim());
        LocalDateTime dateTime = LocalDateTime.of(date, time);

        Appointment a = appointmentService.getAppointmentById(selected.getAppointmentId());
        a.setPatientId(cmbPatient.getValue().getPatientId());
        a.setDoctorId(cmbDoctor.getValue().getDoctorId());
        a.setAppointmentDate(dateTime);
        a.setStatus(cmbStatus.getValue());

        appointmentService.updateAppointment(a);
        loadAppointments();
        clearFields();
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
        clearFields();
    }

    private void clearFields() {
        cmbPatient.getSelectionModel().clearSelection();
        cmbDoctor.getSelectionModel().clearSelection();
        dpDate.setValue(null);
        txtTime.clear();
        cmbStatus.getSelectionModel().clearSelection();
        tblAppointments.getSelectionModel().clearSelection();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

