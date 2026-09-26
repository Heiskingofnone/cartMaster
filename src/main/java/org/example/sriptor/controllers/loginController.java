package org.example.sriptor.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.sriptor.models.user;
import org.example.sriptor.services.AuthService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

//Controls verification at the front end level. Takes input from form the form and stores it in variables for later use for confirmation
public class loginController {
    @FXML private TextField loginUsername;// an instance of a textfield is acknowledged
    @FXML private PasswordField loginPassword;//an instance of a textfield is acknowledged


    @FXML private Text errorlabel;

    //Services declarations
    private final AuthService authService = new AuthService();

    public loginController() throws SQLException {
    }


    public enum LoginStatus{
        AUTHENTIC(0),
        INAUTHENTIC(1),
        ACCOUNT_DOESNT_EXIST(2),
        FAILURE(3);


        private final int code;
        LoginStatus(int code){
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }



    public void initialize(){
        errorlabel.setText("");
    }

    public void onSubmit(ActionEvent event){
        String username = loginUsername.getText();
        String password = loginPassword.getText();
        LoginStatus status = authService.loginValidation(username, password);
        switch (status){
            case AUTHENTIC -> {
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                //TODO: DIRECT TO LANDING PAGE
            }
            case ACCOUNT_DOESNT_EXIST, INAUTHENTIC -> {
                errorlabel.setText("Invalid username or password, try again.");
                loginPassword.clear();
                loginUsername.clear();
            }
            case FAILURE -> errorlabel.setText("An Error occurred, try again later.");


        }



    }


    //Event that takes us to signup page
    @FXML public void toSignUpPage(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        try{
            signupSetpage(stage);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void signupSetpage(Stage stage) throws IOException{
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/example/sriptor/views/signup.fxml")));
            String iconPath = "/org/example/sriptor/assets/Cartmasterpng";
            String cssStyle = Objects.requireNonNull(getClass().getResource("/org/example/sriptor/views/signup.css")).toExternalForm();
            //stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream(iconPath))));
            root.getStylesheets().clear();
            root.getStylesheets().add(cssStyle);
            stage.setTitle("CartMaster");
            stage.getScene().setRoot(root);
            stage.setMaximized(true);//Sets the window to maximized on default
            stage.show();
            root.requestFocus();
            stage.setOnCloseRequest( event -> {event.consume(); logout(stage);});

    }

    //Closes our application
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
