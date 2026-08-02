package org.example.sriptor.models;

public class saleItem {
    private String saleItemId;
    private String saleId;
    private String productId;
    private int saleItemQuantity;
    private double saleItemUnitPrice;
    private product product;

    public saleItem(product product, int saleItemQuantity) {
        this.product = product;
        this.saleItemQuantity = saleItemQuantity;

    }


    //Setters

    public void setSaleItemId(String saleItemId) {
        this.saleItemId = saleItemId;
    }

    public void setSaleId(String saleId) {
        this.saleId = saleId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setSaleItemUnitPrice(double saleItemUnitPrice) {
        this.saleItemUnitPrice = saleItemUnitPrice;
    }

    public void setSaleItemQuantity(int saleItemQuantity) {
        this.saleItemQuantity = saleItemQuantity;
    }



    //getters
    public double getSubtotal(){
        return product.getProductPrice() * saleItemQuantity;
    }

    public String getSaleId() {
        return saleId;
    }

    public String getSaleItemId() {
        return saleItemId;
    }

    public String getProductId() {
        return productId;
    }

    public double getSaleItemUnitPrice() {
        return saleItemUnitPrice;
    }
    public int getSaleItemQuantity() {
        return saleItemQuantity;
    }

    public product getProduct() {
        return product;
    }

}
