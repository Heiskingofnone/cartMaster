package org.example.sriptor.models;

public class sale {
    private String saleId;
    private int userId;
    private double totalAmount;

    //setters

    public void setSaleId(String saleId) {
        this.saleId = saleId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    //getters

    public String getSaleId() {
        return saleId;
    }

    public int getUserId() {
        return userId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }
}
