package org.example.sriptor.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class mainWindowController {
    private static final Logger logger = LoggerFactory.getLogger(mainWindowController.class);

    @FXML private AnchorPane centerPanel;
    @FXML private VBox navbar;

    public void initialize(){
        try {
            configureNavigationSelection();
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

    private void configureNavigationSelection() {
        Node dashboardItem = null;
        for (Node item : navbar.getChildren()) {
            if (!item.getStyleClass().contains("navbar-item")) {
                continue;
            }

            if (dashboardItem == null) {
                dashboardItem = item;
            }

            item.setOnMouseClicked(event -> {
                for (Node navItem : navbar.getChildren()) {
                    if (navItem.getStyleClass().contains("navbar-item")) {
                        navItem.getStyleClass().remove("active");
                    }
                }
                item.getStyleClass().add("active");
            });
        }

        if (dashboardItem != null) {
            dashboardItem.getStyleClass().add("active");
        }
    }
}
