package org.example.sriptor.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.sriptor.models.user;
import java.io.IOException;
import java.util.Objects;


public class SignupController {
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    //@FXML
    //private ComboBox<user.Role> roleComboBox;
    @FXML
    private Button signupButton;
    @FXML private Text errorlabel;
    public void initialize(){
        //roleComboBox.setItems(FXCollections.observableArrayList(user.Role.values()));
        //roleComboBox.setValue(user.Role.CASHIER);
        errorlabel.setText("");
        signupButton.setDisable(true);
        firstNameField.textProperty().addListener((observable, oldValue, newValue) -> ensureAllInput());
        lastNameField.textProperty().addListener((observable, oldValue, newValue) -> ensureAllInput());
        usernameField.textProperty().addListener((observable, oldValue, newValue) -> ensureAllInput());
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> validatePassword());
        confirmPasswordField.textProperty().addListener((observable, oldValue, newValue) -> validatePassword());

    }
    public void validatePassword(){
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if(confirmPassword.isEmpty()){
        errorlabel.setText("");
        signupButton.setDisable(true);
        }
        if(!password.equals(confirmPassword)){
            errorlabel.setText("Passwords Do Not Match");
            signupButton.setDisable(true);
        }
        else{
            errorlabel.setText("");
            signupButton.setDisable(false);
        }
    }
    public void ensureAllInput(){
        String fname = firstNameField.getText();
        String lname = lastNameField.getText();
        String uname = usernameField.getText();
        if(fname.isEmpty()){
            errorlabel.setText("");
            signupButton.setDisable(true);
        }
        if(lname.isEmpty()){
            errorlabel.setText("");
            signupButton.setDisable(true);
        }
        if(uname.isEmpty()){
            errorlabel.setText("");
            signupButton.setDisable(true);
        }

    }
    public user createUser(String firstName, String lastName, String username, String password, user.Role Role){
       return new user(firstName, lastName, username, password, Role);
    }
    @FXML
    public void signUp (ActionEvent event){
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        user.Role selectedRole = user.Role.ADMIN;
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        try{
            user newUser = createUser(firstName, lastName, username, password, selectedRole);
            toLogInPage(stage, newUser);
        } catch (Exception e){
            e.printStackTrace();
        }

    }
    public void toLogInPage(Stage stage, user registeredUser)throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/org/example/sriptor/views/login.fxml")));
        Parent root = loader.load();
        loginController controller = loader.getController();
        controller.setRegisteredUser(registeredUser);
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
    }
}
