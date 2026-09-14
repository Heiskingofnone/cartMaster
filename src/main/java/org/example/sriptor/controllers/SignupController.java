package org.example.sriptor.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.sriptor.models.user;
import java.io.IOException;
import java.util.Objects;
import java.util.regex.Pattern;

import static java.lang.System.out;


public class SignupController {

    //First stack
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    //@FXML private ComboBox<user.Role> roleComboBox;
    String firstName;
    String lastName;
    String username;
    String password;

    //Second Stack
    @FXML private TextField phoneNumberField;






    @FXML
    private Button accountCreateButton;
    @FXML private Text errorlabel;
    public void initialize(){
        //roleComboBox.setItems(FXCollections.observableArrayList(user.Role.values()));
        //roleComboBox.setValue(user.Role.CASHIER);
        errorlabel.setText("");
        accountCreateButton.setDisable(true);
        enterpriseDetailsSlide.setVisible(false);
        firstNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            ensureAllInput();
            enableAccountCreateButton();
        });
        lastNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            ensureAllInput();
            enableAccountCreateButton();
        });
        emailField.textProperty().addListener((observable, oldValue, newValue) -> {
            ensureAllInput();
            validateEmail(emailField.getText());
            enableAccountCreateButton();
        });
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            passwordStrength(passwordField.getText());
            enableAccountCreateButton();
        });
        confirmPasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
            ensureAllInput();
            enableAccountCreateButton();
        });

    }
    private boolean passwordValidated = false;
    private boolean allInputEnsured = false;
    private boolean passwordIsStrong = false;
    private boolean emailIsValid = false;
    public void validatePassword(){
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if(confirmPassword.isEmpty() || password.isEmpty() || !password.equals(confirmPassword)){
            errorlabel.setText("");
            accountCreateButton.setDisable(true);
            passwordValidated = false;
            if(!password.equals(confirmPassword)) errorlabel.setText("Passwords Do Not Match");
        }
        else {
            errorlabel.setText("");
            passwordValidated = true;


        }


    }
    public void ensureAllInput(){
        String fname = firstNameField.getText();
        String lname = lastNameField.getText();
        String email = emailField.getText();
        if(fname.isEmpty()){
            errorlabel.setText("");
            accountCreateButton.setDisable(true);
            allInputEnsured = false;

        }
        else if(lname.isEmpty()){
            errorlabel.setText("");
            accountCreateButton.setDisable(true);
            allInputEnsured = false;

        }
        else if(email.isEmpty()){
            errorlabel.setText("");
            accountCreateButton.setDisable(true);
            allInputEnsured = false;

        }
        else{
            allInputEnsured = true;
        }
        validatePassword();

    }
    public user createUser(String firstName, String lastName, String username, String password, user.Role Role, String phoneNumber){
       return new user(firstName, lastName, username, password, Role, phoneNumber);
    }
    @FXML
    public void signUp (ActionEvent event){


        user.Role selectedRole = user.Role.ADMIN;
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        try{
            //user newUser = createUser(firstName, lastName, username, password, selectedRole, phoneNumber);
            //toLogInPage(stage, newUser);
        } catch (Exception e){
            e.printStackTrace();
        }

    }
    @FXML public void signIn(MouseEvent event){
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        try {
            toLogInPage(stage);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void toLogInPage(Stage stage)throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/example/sriptor/views/Login.fxml")));
        //String iconPath = "/org/example/sriptor/assets/Cartmasterpng";
        String cssStyle = Objects.requireNonNull(getClass().getResource("/org/example/sriptor/views/Login.css")).toExternalForm();
        //stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream(iconPath))));
        root.getStylesheets().clear();
        root.getStylesheets().add(cssStyle);
        stage.setTitle("CartMaster");
        stage.getScene().setRoot(root);
        stage.setMaximized(true);//Sets the window to maximized on default
        stage.show();
    }
    @FXML private VBox accountCreationSlide ;
    @FXML private VBox enterpriseDetailsSlide;

    @FXML private void accountCreationNext(ActionEvent event){
        accountCreationSlide.setVisible(false);
        enterpriseDetailsSlide.setVisible(true);

    }





    @FXML public Label errorLabel1;
    @FXML public Label errorLabel2;
    @FXML public Label errorLabel3;
    @FXML public Label errorLabel4;

    public static final Pattern UPPER = Pattern.compile("[A-Z]");
    public static final Pattern LOWER = Pattern.compile("[a-z]");
    public static final Pattern  DIGIT = Pattern.compile("[0-9]");

    public static final Pattern EMAILSYMBOLS = Pattern.compile("[A-Za-z0-9]+@+[A-Za-z0-9]+\\.+[A-z]{2,}");
    public void passwordStrength(String password){
        //Eight-character limit
        if(password.length() < 8){
            errorLabel1.setStyle("-fx-text-fill: red");
        }
        else if(password.length() >= 8) {
            errorLabel1.setStyle("-fx-text-fill: green");
        }
        else {
            errorLabel1.setStyle("-fx-text-fill: #979797");

        }
        //Upper case requirement
        if(!UPPER.matcher(password).find()){
            errorLabel2.setStyle("-fx-text-fill: red");

        }
        else if (UPPER.matcher(password).find()) {
            errorLabel2.setStyle("-fx-text-fill: green");

        }
        else {
            errorLabel2.setStyle("-fx-text-fill: #979797");

        }
        //Lower case requirement
        if(!LOWER.matcher(password).find()){
            errorLabel3.setStyle("-fx-text-fill: red");

        }
        else if (LOWER.matcher(password).find()) {
            errorLabel3.setStyle("-fx-text-fill: green");

        }
        else {
            errorLabel3.setStyle("-fx-text-fill: #979797");

        }
        //Number requirement
        if(!DIGIT.matcher(password).find()){
            errorLabel4.setStyle("-fx-text-fill: red");

        }
        else if(DIGIT.matcher(password).find()){
            errorLabel4.setStyle("-fx-text-fill: green");

        }
        else {
            errorLabel4.setStyle("-fx-text-fill: #979797");

        }
        if(LOWER.matcher(password).find() && UPPER.matcher(password).find() && DIGIT.matcher(password).find() && (password.length() >= 8) && !password.isEmpty()){
            passwordIsStrong = true;
        }
        else passwordIsStrong = false;

    }
    public void validateEmail(String email){
        if(!EMAILSYMBOLS.matcher(email).find()){
            errorlabel.setText("Your email is invalid");
            errorlabel.setVisible(true);
            accountCreateButton.setDisable(true);
            emailIsValid = false;
        }
        else {
            emailIsValid = true;
        }
    }
    public void enableAccountCreateButton(){
        out.println("email is validated "+ emailIsValid);
        out.println("password is strong "+passwordIsStrong);
        out.println("password is validated "+passwordValidated);
        out.println("All fields are inputted "+allInputEnsured);
        if(passwordIsStrong && passwordValidated && allInputEnsured && emailIsValid){
            accountCreateButton.setDisable(false);
        }
        else{
            accountCreateButton.setDisable(true);
        }
    }
}
