package org.example.sriptor.models;

import java.util.ArrayList;
import java.util.List;

public class product {
    private String productId;
    private String productName;
    private String categoryId;
    private String categoryName;
    private List<String> categoryIds = new ArrayList<>();
    private List<String> categoryNames = new ArrayList<>();
    private int productQuantity;
    private Double productPrice;
    private Double productCostPrice;
    private String sku;
    private String imagePath;




    public product(String productName, Double productPrice, String productId, String imagePath, String categoryId){
        this(productName, productPrice, 0.0, productId, imagePath, categoryId, null, null, 0);
    }

    public product(String productName, Double productPrice, Double productCostPrice, String productId,
                   String imagePath, String categoryId, String categoryName, String sku, int productQuantity) {
        this.productPrice = productPrice;
        this.productCostPrice = productCostPrice;
        this.productName = productName;
        this.categoryId = categoryId;
        if (categoryId != null) {
            this.categoryIds.add(categoryId);
        }
        this.categoryName = categoryName;
        if (categoryName != null && !categoryName.isBlank()) {
            this.categoryNames = new ArrayList<>(List.of(categoryName.split(", ")));
        }
        this.productId = productId;
        this.imagePath = imagePath;
        this.sku = sku;
        this.productQuantity = productQuantity;
    }

    //setters
    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }
    public void setImagePath(String imagePath){this.imagePath = imagePath;}
    public void setProductName(String productName){this.productName = productName;}
    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }
    public void setProductId(String productId) {
        this.productId = productId;
    }
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public void setCategoryIds(List<String> categoryIds) {
        this.categoryIds = new ArrayList<>(categoryIds);
    }
    public void setCategoryNames(List<String> categoryNames) {
        this.categoryNames = new ArrayList<>(categoryNames);
        this.categoryName = String.join(", ", categoryNames);
    }
    public void setProductCostPrice(Double productCostPrice) { this.productCostPrice = productCostPrice; }
    public void setSku(String sku) { this.sku = sku; }
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
    public String getCategoryName() { return categoryName; }
    public List<String> getCategoryIds() { return List.copyOf(categoryIds); }
    public List<String> getCategoryNames() { return List.copyOf(categoryNames); }
    public Double getProductCostPrice() { return productCostPrice; }
    public String getSku() { return sku; }
}
