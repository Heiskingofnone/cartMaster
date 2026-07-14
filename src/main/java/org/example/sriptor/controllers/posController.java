package org.example.sriptor.controllers;

import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Objects;

public class posController {
    private Stage stage;
    private Parent root;
    private Scene scene;
    public void posSetWindow(ActionEvent event) throws IOException {
        Parent root =  FXMLLoader.load(Objects.requireNonNull(getClass().getResource("pox.fxml")));
        Scene scene = new Scene(root);
        String iconPath = "/org/example/sriptor/assets/logo.png";
        String cssStyle = Objects.requireNonNull(getClass().getResource("/org/example/sriptor/views/style.css")).toExternalForm();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream(iconPath))));
        scene.getStylesheets().clear();
        scene.getStylesheets().add(cssStyle);
        stage.setTitle("CartMaster");
        stage.setScene(scene);
        stage.show();
    }
}
