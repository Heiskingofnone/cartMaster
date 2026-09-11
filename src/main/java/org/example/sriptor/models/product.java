package org.example.sriptor.models;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.util.Objects;

public class product {
    private String productId;
    private String productName;
    private String categoryId;
    private int productQuantity;
    private Double productPrice;
    private String imagePath;




    public product(String productName, Double productPrice, String productId, String imagePath, String categoryId){
        this.productPrice = productPrice;
        this.productName = productName;
        this.categoryId = categoryId;
        this.productId = productId;
        this.imagePath = imagePath;
    }

    //setters
    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }
    public void setImagePath(String imagePath){

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
    public void setProductId(String productId) {
        this.productId = productId;
    }
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
    //Getters

    public int getProductQuantity() {return productQuantity;}
    public String getProductName() {
        return productName;
    }
    public Double getProductPrice() {
        return productPrice;
    }
    public String getImagePath(){return imagePath;}
    public String getProductId(){
        return productId;
    }
    public String getCategoryId() {
        return categoryId;
    }
}
