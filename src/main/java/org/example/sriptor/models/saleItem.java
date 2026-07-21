package org.example.sriptor.models;

public class saleItem {
    private String saleItemName;
    private String saleItemPrice;
    private String saleItemQuantity;

    public saleItem(String saleItemName, String saleItemPrice, String saleItemQuantity) {
        this.saleItemName = saleItemName;
        this.saleItemPrice = saleItemPrice;
        this.saleItemQuantity = saleItemQuantity;

    }
//Setters
    public void setSaleItemName(String saleItemName) {
        this.saleItemName = saleItemName;
    }

    public void setSaleItemPrice(String saleItemPrice) {
        this.saleItemPrice = saleItemPrice;
    }

    public void setSaleItemQuantity(String saleItemQuantity) {
        this.saleItemQuantity = saleItemQuantity;
    }
    //Getters

    public String getSaleItemName() {
        return saleItemName;
    }
    public String getSaleItemPrice() {
        return saleItemPrice;
    }

    public String getSaleItemQuantity() {
        return saleItemQuantity;
    }

}
