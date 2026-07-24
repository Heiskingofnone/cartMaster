package org.example.sriptor.models;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.util.Objects;

public class product {
    private int id;
    private String productName;
    private Double productPrice;
    private String imagePath;




    public product(String productName, Double productPrice, int id, String imagePath){
        this.productPrice = productPrice;
        this.productName = productName;
        this.id = id;
        this.imagePath = imagePath;
        //setImage(imagePath);
       //setProductName(productName);
        //setProductPrice(productPrice);
    }
    public void setImage(String imagePath){

        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        ImageView imageView = new ImageView();
        imageView.setImage(image);
    }
    public void setProductName(String productName){
        Text productNametext = new Text();
        productNametext.setText(productName);
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public Double getProductPrice() {
        return productPrice;
    }
    public String getImagePath(){return imagePath;}

    public int getId(){
        return id;
    }

}
