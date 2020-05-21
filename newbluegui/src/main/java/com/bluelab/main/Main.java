package com.bluelab.main;


import com.bluelab.database.DBConnection;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    private static MainPanelCtrl mainCtrl;

    @Override
    public void start(Stage primaryStage) {
        try {
            DBConnection.init();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainPanel.fxml"));
            Parent root = loader.load();
            mainCtrl = loader.getController();

            Scene scene = new Scene(root, 1020, 600);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setMaximized(true);
            primaryStage.show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
