package com.hospital;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Start extends Application {
    @Override

    public void start(Stage stage) throws Exception {
        System.out.println(getClass().getResource("LandingPage.fxml"));

        FXMLLoader fxmlLoader = new FXMLLoader(Start.class.getResource("/com/hospital/UI/LandingPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),  700, 300);
        stage.setTitle("Patient");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {

        Application.launch(args);
    }
}
