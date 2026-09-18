package org.example.sriptor.models;

public class sale {
    private String saleId;
    private String userId;
    private double totalAmount;
    private String saleDate;

    public sale(String saleId, String userId, double totalAmount, String saleDate){
        this.saleId = saleId;
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.saleDate = saleDate;
    }



    //setters
    public void setSaleId(String saleId) {
        this.saleId = saleId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
    public void setSaleDate(String saleDate) {
        this.saleDate = saleDate;
    }


    //getters
    public String getSaleId() {
        return saleId;
    }
    public String getUserId() {
        return userId;
    }
    public double getTotalAmount() {
        return totalAmount;
    }
    public String getSaleDate() {
        return saleDate;
    }
}
