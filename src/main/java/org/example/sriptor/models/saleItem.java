package org.example.sriptor.models;

public class saleItem {

    private int saleItemQuantity;
    private product product;

    public saleItem(product product, int saleItemQuantity) {
        this.product = product;
        this.saleItemQuantity = saleItemQuantity;

    }
    public double getSubtotal(){
        return product.getProductPrice() * saleItemQuantity;
    }

    public int getSaleItemQuantity() {
        return saleItemQuantity;
    }

    public product getProduct() {
        return product;
    }

    //Setters

    public void setSaleItemQuantity(int saleItemQuantity) {
        this.saleItemQuantity = saleItemQuantity;
    }

}
