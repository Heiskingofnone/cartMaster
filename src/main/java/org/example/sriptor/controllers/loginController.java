package org.example.sriptor.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.sriptor.models.user;

import java.io.IOException;
import java.util.Objects;

//Controls verification at the front end level. Takes input from form the form and stores it in variables for later use for confirmation
public class loginController {
    @FXML
    private TextField loginUsername;// an instance of a textfield is acknowledged
    @FXML //Connects loginController + fxml in order to acknowledge object instance
    private TextField loginPassword;//an instance of a textfield is acknowledged
    public void initialize(){
        errorlabel.setText("");
    }


    public void onSubmit(ActionEvent event){
        String username = loginUsername.getText();
        String password = loginPassword.getText();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        try{
            posSetWindow(stage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
    public void posSetWindow(Stage stage) throws IOException {
        if(authenticateUser()) {
        Parent root =  FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/example/sriptor/views/pos.fxml")));
        Scene scene = new Scene(root);
        String iconPath = "/org/example/sriptor/assets/logo.png";
        String cssStyle = Objects.requireNonNull(getClass().getResource("/org/example/sriptor/views/style.css")).toExternalForm();

        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream(iconPath))));
        scene.getStylesheets().clear();
        scene.getStylesheets().add(cssStyle);
        stage.setTitle("CartMaster");
        stage.setScene(scene);
        stage.setMaximized(true);//Sets the window to maximized on default
        stage.show();
        stage.setOnCloseRequest( event -> {event.consume(); logout(stage);});

        } else{
            errorlabel.setText("username or password is incorrect");

        }
    }
    private static user registeredUser;
    public void setRegisteredUser(user user){
        registeredUser = user;
    }
    public boolean authenticateUser(){
        String enteredUser = loginUsername.getText();
        String enteredPassword = loginPassword.getText();
        return registeredUser != null && registeredUser.getUsername().equals(enteredUser) && registeredUser.getPassword().equals(enteredPassword);
    }
    @FXML
    public void toSignUpPage(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        try{
            signupSetpage(stage);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    @FXML private Text errorlabel;
    public void signupSetpage(Stage stage) throws IOException{


            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/example/sriptor/views/signup.fxml")));
            Scene scene = new Scene(root);
            String iconPath = "/org/example/sriptor/assets/logo.png";
            String cssStyle = Objects.requireNonNull(getClass().getResource("/org/example/sriptor/views/style.css")).toExternalForm();
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream(iconPath))));
            scene.getStylesheets().clear();
            scene.getStylesheets().add(cssStyle);
            stage.setTitle("CartMaster");
            stage.setScene(scene);
            //stage.setMaximized(true);//Sets the window to maximized on default
            stage.show();
            stage.setOnCloseRequest( event -> {event.consume(); logout(stage);});

    }

    public void logout(Stage stage){
        Alert closeAlert = new Alert(Alert.AlertType.CONFIRMATION);
        closeAlert.setTitle("Closing Cart Master");
        closeAlert.setHeaderText("You are about to logout,");
        closeAlert.setContentText("Do you want to save?");
        if(closeAlert.showAndWait().get() == ButtonType.OK){
            stage.close();
        }
    }
}
