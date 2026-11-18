package org.example.sriptor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;

public class posApplication extends Application {
    private static final Logger logger = LoggerFactory.getLogger(posApplication.class);

    @Override
    public void start(Stage stage) throws IOException {
        logger.debug("Loading the login screen");
        String iconPath = "/org/example/sriptor/assets/Cartmaster.png"; //stores the path of our app icon into a string
        String cssStyle = Objects.requireNonNull(getClass().getResource("/org/example/sriptor/views/Login.css")).toExternalForm();//We also get the path of our stylesheet into a string


        //FXMLLoader loads our fxml file onto the window we are about to instantiate
        FXMLLoader fxmlLoader = new FXMLLoader(posApplication.class.getResource("/org/example/sriptor/views/Login.fxml"));//linked to the path of fxml file
        Parent root;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            logger.error("Failed to load the login screen", e);
            throw e;
        }
        Scene scene = new Scene(root); //Loads our fxml file into the scene from the fxml loader above

        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream(iconPath))));//We load our icon path into the getIcons() function
        scene.getStylesheets().clear(); //We load our css stylesheet into the scene
        scene.getStylesheets().add(cssStyle);//We inject our stylesheet into the
        stage.setTitle("CartMaster");
        stage.setScene(scene);
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.setMaximized(true);
        root.requestFocus();
        stage.show();
        logger.info("CartMaster login window displayed");

    }

}
