package org.example.sriptor.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.example.sriptor.dto.kpiSummaryDTO;
import org.example.sriptor.services.AnalyticsService;

import java.sql.SQLException;


public class dashboardController {
    @FXML
    private Label revenue;
    @FXML private Label transactions;
    @FXML private Label netProfit;
    @FXML private Label supplierDues;

    public void initialize() throws SQLException {
        AnalyticsService analyticsService = new AnalyticsService();
        kpiSummaryDTO kpiSummaryDTO = analyticsService.dailySummary();
        revenue.setText(String.valueOf(kpiSummaryDTO.getDailyRevenue()));
        transactions.setText(String.valueOf(kpiSummaryDTO.getTransactionCount()));
        netProfit.setText("0.00");//TODO: Connect net Profit to figures that represents it.
        supplierDues.setText("0.00");//TODO: Connect Supplier dues to figures that represents it

    }
}
