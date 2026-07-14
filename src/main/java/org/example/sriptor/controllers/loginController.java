package org.example.sriptor.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

//Controls verification at the front end level. Takes input from form the form and stores it in variables for later use for confirmation
public class loginController {
    @FXML
    private TextField loginUsername;// an instance of a textfield is acknowledged
    @FXML //Connects loginController + fxml in order to acknowledge object instance
    private TextField loginPassword;//an instance of a textfield is acknowledged

    private void onSubmit(ActionEvent event){
        String username = loginUsername.getText();
        String password = loginPassword.getText();
    }
}
