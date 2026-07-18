package org.example.sriptor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.util.Objects;

public class posApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        String iconPath = "/org/example/sriptor/assets/logo.png"; //stores the path of our app icon into a string
        String cssStyle = Objects.requireNonNull(getClass().getResource("/org/example/sriptor/views/style.css")).toExternalForm();//We also get the path of our stylesheet into a string


        //FXMLLoader loads our fxml file onto the window we are about to instantiate
        FXMLLoader fxmlLoader = new FXMLLoader(posApplication.class.getResource("/org/example/sriptor/views/login.fxml"));//linked to the path of fxml file
        Scene scene = new Scene(fxmlLoader.load()); //Loads our fxml file into the scene from the fxml loader above


        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream(iconPath))));//We load our icon path into the getIcons() function
        scene.getStylesheets().clear(); //We load our css stylesheet into the scene
        scene.getStylesheets().add(cssStyle);//We inject our stylesheet into the
        stage.setTitle("CartMaster");
        stage.setScene(scene);
        stage.show();


    }

}
