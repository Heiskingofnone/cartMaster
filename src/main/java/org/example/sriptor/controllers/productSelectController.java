package org.example.sriptor.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import org.example.sriptor.models.product;

import java.io.IOException;

public class productSelectController {

    private posController posController;
    private product currentProduct;

    @FXML private Text productNameText;
    @FXML private Text productPriceText;
    @FXML private Text productQuantityText;
    @FXML private Text saleItemPrice;

    // Connects this child controller to the master POS controller
    public void setPosController(posController posController) {
        this.posController = posController;
    }

    /**
     * Binds the product domain model to the UI labels
     */
    public void setData(product product) {
        this.currentProduct = product;

        if (productNameText != null && product != null) {
            productNameText.setText(product.getProductName());
        }

        if (productPriceText != null && product != null) {
            productPriceText.setText(String.format("$%.2f", product.getProductPrice()));
        }
    }

    @FXML
    public void productSelect(MouseEvent event) throws IOException {
        AnchorPane clickedCard = (AnchorPane) event.getSource();
        product selectedProduct = (product) clickedCard.getUserData();

        // Safe check for both object presence and controller initialization
        if (selectedProduct != null && posController != null) {
            posController.addItemToCart(selectedProduct);
        }
    }

    @FXML
    public void increaseQuantity(ActionEvent event) {
        if (productQuantityText != null) {
            try {
                int quantity = Integer.parseInt(productQuantityText.getText());
                quantity++;
                productQuantityText.setText(String.valueOf(quantity));
            } catch (NumberFormatException e) {
                productQuantityText.setText("1");
            }
        }
    }

    @FXML
    public void decreaseQuantity(ActionEvent event) {
        if (productQuantityText != null) {
            try {
                int quantity = Integer.parseInt(productQuantityText.getText());
                if (quantity > 1) {
                    quantity--;
                    productQuantityText.setText(String.valueOf(quantity));
                }
            } catch (NumberFormatException e) {
                productQuantityText.setText("1");
            }
        }
    }
}