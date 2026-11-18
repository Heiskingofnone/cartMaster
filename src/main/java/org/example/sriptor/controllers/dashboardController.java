package org.example.sriptor.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import org.example.sriptor.dto.kpiSummaryDTO;
import org.example.sriptor.services.AnalyticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;


public class dashboardController {
    private static final Logger logger = LoggerFactory.getLogger(dashboardController.class);

    @FXML
    private Label revenue;
    @FXML private Label transactions;
    @FXML private Label netProfit;
    @FXML private Label supplierDues;
    @FXML private StackPane filterButton;
    @FXML private ComboBox<AnalyticsService.TimePeriod> periodComboBox;

    @FXML private AreaChart<String, Double> revenueChart;

    public void initialize() throws SQLException {
        logger.debug("Initializing analytics dashboard");
        periodComboBox.setItems(FXCollections.observableArrayList(AnalyticsService.TimePeriod.values()));
        periodComboBox.setValue(AnalyticsService.TimePeriod.DAILY);
        AnalyticsService analyticsService = new AnalyticsService();
        kpiSummaryDTO kpiSummaryDTO = analyticsService.summaryFor(AnalyticsService.TimePeriod.DAILY);
        revenue.setText(String.valueOf(kpiSummaryDTO.getDailyRevenue()));
        transactions.setText(String.valueOf(kpiSummaryDTO.getTransactionCount()));
        netProfit.setText("0.00");//TODO: Connect net Profit to figures that represents it.
        supplierDues.setText("0.00");//TODO: Connect Supplier dues to figures that represents it
        updateRevenueChart(analyticsService, AnalyticsService.TimePeriod.DAILY);
        logger.info("Analytics dashboard initialized");
    }

    @FXML public void refreshFilter(MouseEvent event) throws SQLException {
        AnalyticsService analyticsService = new AnalyticsService();
        AnalyticsService.TimePeriod period = periodComboBox.getValue();
        if (period == null) {
            logger.warn("Ignoring analytics refresh because no time period is selected");
            return;
        }
        kpiSummaryDTO kpiSummaryDTO = analyticsService.summaryFor(period);
        revenue.setText(String.valueOf(kpiSummaryDTO.getDailyRevenue()));
        transactions.setText(String.valueOf(kpiSummaryDTO.getTransactionCount()));
        updateRevenueChart(analyticsService, period);
        logger.debug("Refreshed analytics dashboard for {}", period);
    }

    private void updateRevenueChart(AnalyticsService analyticsService, AnalyticsService.TimePeriod period)
            throws SQLException {
        XYChart.Series<String, Double> processedSeries = analyticsService.revenueSeriesFor(period);
        revenueChart.getData().clear();
        revenueChart.getData().add(processedSeries);
    }




}
