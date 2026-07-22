package org.example.sriptor.models;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.util.Objects;

public class product {
    private String productName;
    private String productPrice;




    public product(String productName, String productPrice){
        this.productPrice = productPrice;
        this.productName = productName;
        setImage();
        setProductName(productName);
        setProductPrice(productPrice);
    }
    public void setImage(){

        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/org/example/sriptor/assets/DefaultImage.png")));
        ImageView imageView = new ImageView();
        imageView.setImage(image);
    }
    public void setProductName(String productName){
        Text productNametext = new Text();
        productNametext.setText(productName);
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductPrice() {
        return productPrice;
    }
}
