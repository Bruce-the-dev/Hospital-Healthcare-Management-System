package com.hospital.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LandingController {


    @FXML
    private void openDepartmentPage() throws IOException {
        loadPage("/com/hospital/UI/Department.fxml", "Department Management");
    }

    @FXML
    private void openDoctorPage() throws IOException {
        loadPage("/com/hospital/UI/Doctor.fxml", "Doctor Management");
    }

    @FXML
    private void openPatientPage() throws IOException {
        loadPage("/com/hospital/UI/PatientView.fxml", "Patient Management");
    }

    @FXML
    private void openAppointmentPage() throws IOException {
        loadPage("/com/hospital/UI/Appointment.fxml", "Appointment Management");
    }

    @FXML
    private void openInventoryPage() throws IOException {
        loadPage("/com/hospital/UI/Inventory.fxml", "Inventory Management");
    }

    @FXML
    private void openMedsPage() throws IOException {
        loadPage("/com/hospital/UI/Medication.fxml", "Medication Management");
    }

    @FXML
    private void openPrescriptionsPage() throws IOException {
        loadPage("/com/hospital/UI/Prescription.fxml", "Prescription Management");
    }

    @FXML
    private void goToReports() throws IOException {
        loadPage("/com/hospital/UI/Reports.fxml", "Reports Management");
    }

    @FXML
    private void btnAdvancedReports() throws IOException {
        loadPage("/com/hospital/UI/AdvancedReports.fxml", "Advanced Report");
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
