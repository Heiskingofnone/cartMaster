package org.example.sriptor.controllers;
//import org.example.sriptor.models.product;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.IOException;



public class productSelectController {
    public String saleItemName;
    posController posController;
    //above we created an instance of posController
    //below we created a setter that takes the controller as a parameter and sets the controller as the parameter
    //Next part of the code is in the posController file
    public void setPosController(posController posController){
        this.posController = posController;
    }
    @FXML
    public void productSelect(MouseEvent event) throws IOException {
        //System.out.println("item selected");
        AnchorPane clickedCard = (AnchorPane) event.getSource();

        Text selectedProductName = (Text) clickedCard.lookup("#productNameText");
        Text selectedProductPrice = (Text) clickedCard.lookup("#productPriceText");

        if(selectedProductName != null) {
            String saleItemName = selectedProductName.getText();
            String saleItemPrice = selectedProductPrice.getText();

            System.out.println(saleItemName);
            System.out.println(saleItemPrice);
            posController.addItemToCart(saleItemName,saleItemPrice);
        }

    }


}
