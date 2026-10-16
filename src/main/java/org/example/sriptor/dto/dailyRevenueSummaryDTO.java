package org.example.sriptor.dto;

public class dailyRevenueSummaryDTO {
    private double dailyRevenue;
    private int transactionCount;

    public dailyRevenueSummaryDTO(double dailyRevenue, int transactionCount){
        this.dailyRevenue = dailyRevenue;
        this.transactionCount = transactionCount;
    }

    public double getDailyRevenue() {
        return dailyRevenue;
    }

    public int getTransactionCount() {
        return transactionCount;
    }

    public void setDailyRevenue(double dailyRevenue) {
        this.dailyRevenue = dailyRevenue;
    }

    public void setTransactionCount(int transactionCount) {
        this.transactionCount = transactionCount;
    }
}
