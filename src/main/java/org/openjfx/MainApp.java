package org.openjfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("org.openjfx/scene.fxml"));

        Scene scene = new Scene(root);


        scene.getStylesheets().add(getClass().getClassLoader().getResource("org.openjfx/styles.css").toExternalForm());

        stage.setTitle("JavaFX and Gradle");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        TabInput.executor.shutdown();
    }

    public static void main(String[] args) {
        launch(args);
    }

}