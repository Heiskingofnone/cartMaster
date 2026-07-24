package org.example.sriptor.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import org.example.sriptor.models.product;
import org.example.sriptor.models.saleItem;

import java.io.IOException;

public class productSelectController {

    private posController posController;
    private product currentProduct;

    @FXML private Text productNameText;
    @FXML private Text productPriceText;
    @FXML private Text productQuantityText;


    // Connects this child controller to the master POS controller
    public void setPosController(posController posController) {
        this.posController = posController;
    }

    public void setData(product product) {
        this.currentProduct = product;

        if (productNameText != null && product != null) {
            productNameText.setText(product.getProductName());
        }

        if (productPriceText != null && product != null) {
            productPriceText.setText(Double.toString(product.getProductPrice()));
        }
    }


    @FXML
    public void productSelect(MouseEvent event) throws IOException {
        // Direct reference! No need to pull from UserData or cast the event source.
        if (this.currentProduct != null && this.posController != null) {
            posController.addItemToCart(this.currentProduct);

        } else {
            System.out.println("Click failed: currentProduct or posController is null.");
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