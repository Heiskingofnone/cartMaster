package org.example.sriptor.dto;

public class TopSellerDTO {
    private final String productName;
    private final int unitsSold;
    private final double revenue;

    public TopSellerDTO(String productName, int unitsSold, double revenue) {
        this.productName = productName;
        this.unitsSold = unitsSold;
        this.revenue = revenue;
    }

    public String getProductName() {
        return productName;
    }

    public int getUnitsSold() {
        return unitsSold;
    }

    public double getRevenue() {
        return revenue;
    }
}
