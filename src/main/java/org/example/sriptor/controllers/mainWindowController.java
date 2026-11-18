package org.example.sriptor.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class mainWindowController {
    private static final Logger logger = LoggerFactory.getLogger(mainWindowController.class);

    @FXML private AnchorPane centerPanel;

    public void initialize(){
        try {
            logger.debug("Loading dashboard into the main window");
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/org/example/sriptor/views/dashboard.fxml")));
            AnchorPane dashboard = loader.load();
            AnchorPane.setTopAnchor(dashboard, 0.0);
            AnchorPane.setRightAnchor(dashboard, 0.0);
            AnchorPane.setBottomAnchor(dashboard, 0.0);
            AnchorPane.setLeftAnchor(dashboard, 0.0);
            centerPanel.getChildren().setAll(dashboard);
            logger.info("Main window initialized");

        } catch (Exception e) {
            logger.error("Failed to initialize the main window", e);
            throw new RuntimeException(e);
        }
    }
}
