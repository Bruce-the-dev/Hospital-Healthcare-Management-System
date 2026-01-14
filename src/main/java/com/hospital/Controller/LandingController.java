package com.hospital.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LandingController {

    @FXML
    private void openDepartmentPage(ActionEvent event) throws IOException {
        loadPage("/com/hospital/UI/Department.fxml", "Department Management");
    }

    @FXML
    private void openDoctorPage(ActionEvent event) throws IOException {
        loadPage("/com/hospital/UI/Doctor.fxml", "Doctor Management");
    }

    @FXML
    private void openPatientPage(ActionEvent event) throws IOException {
        loadPage("/com/hospital/UI/PatientView.fxml", "Patient Management");
    }

    @FXML
    private void openAppointmentPage(ActionEvent event) throws IOException {
        loadPage("/com/hospital/UI/Appointment.fxml", "Appointment Management");
    }

    private void loadPage(String fxmlFile, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.show();
    }
}

