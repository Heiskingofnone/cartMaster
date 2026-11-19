package org.example.sriptor.dto;

public class kpiSummaryDTO {
    private double dailyRevenue;
    private int transactionCount;
    private double estimatedProfit;

    public kpiSummaryDTO(double dailyRevenue, int transactionCount){
        this(dailyRevenue, transactionCount, 0.0);
    }

    public kpiSummaryDTO(double dailyRevenue, int transactionCount, double estimatedProfit) {
        this.dailyRevenue = dailyRevenue;
        this.transactionCount = transactionCount;
        this.estimatedProfit = estimatedProfit;
    }

    public double getDailyRevenue() {
        return dailyRevenue;
    }

    public int getTransactionCount() {
        return transactionCount;
    }

    public double getEstimatedProfit() {
        return estimatedProfit;
    }

    public void setDailyRevenue(double dailyRevenue) {
        this.dailyRevenue = dailyRevenue;
    }

    public void setTransactionCount(int transactionCount) {
        this.transactionCount = transactionCount;
    }

    public void setEstimatedProfit(double estimatedProfit) {
        this.estimatedProfit = estimatedProfit;
    }
}
