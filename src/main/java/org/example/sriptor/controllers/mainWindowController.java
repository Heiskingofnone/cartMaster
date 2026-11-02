package org.example.sriptor.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.util.Objects;

public class mainWindowController {
    @FXML private AnchorPane centerPanel;
    @FXML private AnchorPane mainWindow;
    @FXML private VBox navBar;

    public void initialize(){
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/org/example/sriptor/views/dashboard.fxml")));
            AnchorPane dashboard = loader.load();
            if(dashboard != null) {
                centerPanel.getChildren().setAll(dashboard);
            }
            if(mainWindow != null){
                mainWindow.requestLayout();
                mainWindow.applyCss();
                mainWindow.layout();
            }
            if(navBar != null){
                navBar.requestLayout();
                navBar.applyCss();
                navBar.layout();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
