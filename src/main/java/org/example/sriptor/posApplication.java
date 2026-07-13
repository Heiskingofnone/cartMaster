package org.example.sriptor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class posApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(posApplication.class.getResource("/org/example/sriptor/views/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        String iconPath = "/org/example/sriptor/assets/logo.png";
        String cssStyle = Objects.requireNonNull(getClass().getResource("/org/example/sriptor/views/style.css")).toExternalForm();
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream(iconPath))));
        scene.getStylesheets().clear();
        scene.getStylesheets().add(cssStyle);
        stage.setTitle("CartMaster");

        stage.setScene(scene);
        stage.show();
    }
}
