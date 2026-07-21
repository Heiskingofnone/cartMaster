package org.example.sriptor.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.fxml.FXMLLoader;
import org.example.sriptor.models.product;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class posController {

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
                    //Here we created an instance of productSelectcontroller and named it childController and load the controls of this parent method
                    productSelectController childController = loader.getController();
                    //then we pass posController into our setPoscontroller function
                    childController.setPosController(this);

                } catch (IOException e) {
                    e.printStackTrace();
                }

        }
    }

    @FXML
    public GridPane saleItemList = new GridPane();

    private int column = 1;
    private int row = 1;
    public void addItemToCart(String saleItemName, String saleItemPrice){
        ++row;
        saleItemList.getColumnConstraints().clear();
        saleItemList.getRowConstraints().clear();
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/org/example/sriptor/views/saleItem.fxml")));
            AnchorPane saleItemCard = loader.load();
            Text saleItemNameText = (Text) saleItemCard.lookup("#saleItemName");
            Text saleItemPriceText = (Text) saleItemCard.lookup("#saleItemPrice");

            if(saleItemNameText != null){
                saleItemNameText.setText(saleItemName);
            }
            if(saleItemPriceText != null){
                saleItemPriceText.setText(saleItemPrice);
            }
            saleItemList.add(saleItemCard, column, row);
            saleItemList.setHgap(7.5);
            saleItemList.setVgap(5);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



}


