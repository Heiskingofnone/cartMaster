package org.example.sriptor.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class mainWindowController {
    private static final Logger logger = LoggerFactory.getLogger(mainWindowController.class);

    @FXML private AnchorPane centerPanel;
    @FXML private VBox navbar;
    @FXML private HBox dashboardNavItem;
    @FXML private HBox inventoryNavItem;

    private AnchorPane dashboardView;
    private AnchorPane inventoryView;

    public void initialize(){
        try {
            configureNavigationSelection();
            logger.debug("Loading dashboard into the main window");
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/org/example/sriptor/views/dashboard.fxml")));
            dashboardView = loader.load();
            displayDashboard();
            logger.info("Main window initialized");

        } catch (Exception e) {
            logger.error("Failed to initialize the main window", e);
            throw new RuntimeException(e);
        }
    }

    private void configureNavigationSelection() {
        dashboardNavItem.getStyleClass().add("active");
        for (Node item : navbar.getChildren()) {
            if (!item.getStyleClass().contains("navbar-item")) {
                continue;
            }
            item.setOnMouseClicked(event -> navigate(item));
        }
    }

    private void navigate(Node selectedItem) {
        for (Node navItem : navbar.getChildren()) {
            if (navItem.getStyleClass().contains("navbar-item")) {
                navItem.getStyleClass().remove("active");
            }
        }
        selectedItem.getStyleClass().add("active");

        if (selectedItem == dashboardNavItem) {
            displayDashboard();
        } else if (selectedItem == inventoryNavItem) {
            try {
                displayInventory();
            } catch (Exception e) {
                logger.error("Failed to load inventory view", e);
                throw new RuntimeException(e);
            }
        } else {
            centerPanel.getChildren().clear();
        }
    }

    private void displayDashboard() {
        AnchorPane.setTopAnchor(dashboardView, 0.0);
        AnchorPane.setRightAnchor(dashboardView, 0.0);
        AnchorPane.setBottomAnchor(dashboardView, 0.0);
        AnchorPane.setLeftAnchor(dashboardView, 0.0);
        centerPanel.getChildren().setAll(dashboardView);
    }

    private void displayInventory() throws java.io.IOException {
        if (inventoryView == null) {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(
                    getClass().getResource("/org/example/sriptor/views/inventory.fxml")));
            inventoryView = loader.load();
        }
        AnchorPane.setTopAnchor(inventoryView, 0.0);
        AnchorPane.setRightAnchor(inventoryView, 0.0);
        AnchorPane.setBottomAnchor(inventoryView, 0.0);
        AnchorPane.setLeftAnchor(inventoryView, 0.0);
        centerPanel.getChildren().setAll(inventoryView);
    }
}
