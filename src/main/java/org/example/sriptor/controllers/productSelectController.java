package org.example.sriptor.controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import java.io.IOException;



public class productSelectController {
    //public String saleItemName;
    //public String saleItemQuantity;
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

            posController.addItemToCart(saleItemName,saleItemPrice);
        }

    }
    public String getPriceForItem(String priceOfSelectedItem){
        return priceOfSelectedItem;
    }
    @FXML
    private Text productQuantityText;
    @FXML
    private Text saleItemPrice;
    @FXML
    public void increaseQuantity(ActionEvent event){

        int quantity = 1;

        try{
            if (productQuantityText != null) {
                String saleItemQuantity = productQuantityText.getText();
                quantity = Integer.parseInt(saleItemQuantity);
                ++quantity;
                saleItemQuantity = Integer.toString(quantity);
                productQuantityText.setText(saleItemQuantity);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void decreaseQuantity(ActionEvent event){
        int quantity = 1;
        try {

            if (productQuantityText != null){
                String saleItemQuantity = productQuantityText.getText();
                quantity = Integer.parseInt(saleItemQuantity);
                if(quantity > 1){
                --quantity;}
                saleItemQuantity = Integer.toString(quantity);
                productQuantityText.setText(saleItemQuantity);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
