package org.example.sriptor.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.sriptor.models.cart;
import org.example.sriptor.models.product;
import org.example.sriptor.models.saleItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class posController {

    @FXML public GridPane productGrid;

    @FXML public GridPane saleItemList;
    @FXML public Text totalPrice;

    public String imagePath = "org/example/sriptor/assets/DefaultImage.png";

    private cart cartItem = new cart();

    public void initialize() {
        int columns = 5;
        List<product> productList = new ArrayList<>();

        // Clear existing children/constraints on the main product grid
        productGrid.getChildren().clear();
        productGrid.getColumnConstraints().clear();
        productGrid.getRowConstraints().clear();


        for (int i = 0; i < productList.size(); ++i) {
            product currentProduct = productList.get(i);

            int col = i % columns;
            int r = i / columns;
            try {
                FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(
                        getClass().getResource("/org/example/sriptor/views/product.fxml")
                ));
                AnchorPane productCard = loader.load();

                productSelectController childController = loader.getController();

                if (childController != null) {
                    childController.setPosController(this);
                    childController.setData(currentProduct);
                }

                productGrid.add(productCard, col, r);
                productGrid.setHgap(25);
                productGrid.setVgap(25);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addItemToCart(product product) {
        cartItem.addProduct(product, 1);
        updateTotal();

        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(
                    getClass().getResource("/org/example/sriptor/views/saleItem.fxml")
            ));
            AnchorPane saleItemCard = loader.load();

            productSelectController cardController = loader.getController();
            if (cardController != null) {
                cardController.setPosController(this);
                cardController.setData(product);
            }

            // Stack naturally at Column 0, next available row
            int currentRow = saleItemList.getChildren().size();
            saleItemList.add(saleItemCard, 0, currentRow);
            saleItemList.setVgap(5);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void updateTotal(){
        double total = cartItem.calculateTotal();
        totalPrice.setText(Double.toString(total));

    }
    @FXML
    public void clearCart(){
        cartItem.clear();
        saleItemList.getChildren().clear();
        saleItemList.getRowConstraints().clear();
        updateTotal();
    }
    @FXML
    public void handleCheckout(){
        if(cartItem.getItems().isEmpty()){
            showAlert("Checkout Failed", "Your cart is empty! Add items before checking out");
            return;
        }
        double grandTotal = 0.0;
        for(saleItem items: cartItem.getItems()){
            Double Total = items.getProduct().getProductPrice() * items.getSaleItemQuantity();
            grandTotal += Total;
        }
        showAlert("Successful Checkout", "checkout completed! Total: GHS " + grandTotal);
        clearCart();

    }
    private void showAlert(String title, String content){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        String iconPath = "/org/example/sriptor/assets/logo.png"; //stores the path of our app icon into a string
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream(iconPath))));
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}