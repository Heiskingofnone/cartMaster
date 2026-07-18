package org.example.sriptor.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import org.example.sriptor.models.product;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class posController {
    private Stage stage;
    private Parent root;
    private Scene scene;
    @FXML
    public GridPane productGrid;
    public void initialize(){
        int columns = 5;
        List<product> productList = new ArrayList<>();
        productGrid.getChildren().clear();
        productGrid.getColumnConstraints().clear();
        productGrid.getRowConstraints().clear();
        for(int i = 0; i <= 50; ++i){
            productList.add(new product("Kivo Gari", "5.42"));
        }
        for(int i = 0; i <productList.size(); ++i) {

                product currentProduct = productList.get(i);


                int column = i % columns;
                int row = i / columns;
                try{
                    FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/org/example/sriptor/views/product.fxml")));
                    AnchorPane productCard = loader.load();
                    Text productNameText = (Text) productCard.lookup("#productNameText");
                    Text productPriceText = (Text) productCard.lookup("#productPriceText");
                    if(productNameText !=null){
                        productNameText.setText(currentProduct.getProductName());
                    }
                    if(productPriceText !=null){
                        productPriceText.setText("GHS " + currentProduct.getProductPrice());
                    }
                    productGrid.add(productCard, column, row);
                    productGrid.setHgap(25);
                    productGrid.setVgap(25);

                } catch (IOException e) {
                    e.printStackTrace();
                }

        }
    }
}


