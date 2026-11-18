package org.example.sriptor.controllers;

import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.converter.NumberStringConverter;
import org.example.sriptor.dto.kpiSummaryDTO;
import org.example.sriptor.dto.TopSellerDTO;
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

    @FXML private AreaChart<String, Number> revenueChart;
    @FXML private VBox topSellersChart;

    public void initialize() throws SQLException {
        logger.debug("Initializing analytics dashboard");
        NumberAxis revenueAxis = (NumberAxis) revenueChart.getYAxis();
        revenueAxis.setTickLabelFormatter(new NumberStringConverter("#,##0.##") {
            @Override
            public String toString(Number value) {
                if (value == null || value.doubleValue() == 0) {
                    return "";
                }
                return super.toString(value);
            }
        });
        periodComboBox.setItems(FXCollections.observableArrayList(AnalyticsService.TimePeriod.values()));
        periodComboBox.setValue(AnalyticsService.TimePeriod.DAILY);
        AnalyticsService analyticsService = new AnalyticsService();
        kpiSummaryDTO kpiSummaryDTO = analyticsService.summaryFor(AnalyticsService.TimePeriod.DAILY);
        revenue.setText(String.valueOf(kpiSummaryDTO.getDailyRevenue()));
        transactions.setText(String.valueOf(kpiSummaryDTO.getTransactionCount()));
        netProfit.setText("0.00");//TODO: Connect net Profit to figures that represents it.
        supplierDues.setText("0.00");//TODO: Connect Supplier dues to figures that represents it
        refreshCharts(analyticsService, AnalyticsService.TimePeriod.DAILY);
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
        refreshCharts(analyticsService, period);
        logger.debug("Refreshed analytics dashboard for {}", period);
    }

    private void refreshCharts(AnalyticsService analyticsService, AnalyticsService.TimePeriod period)
            throws SQLException {
        updateRevenueChart(analyticsService, period);
        updateTopSellersChart(analyticsService, period);
    }

    private void updateRevenueChart(AnalyticsService analyticsService, AnalyticsService.TimePeriod period)
            throws SQLException {
        XYChart.Series<String, Number> processedSeries = analyticsService.revenueSeriesFor(period);
        revenueChart.getData().clear();
        revenueChart.getData().add(processedSeries);
    }

    private void updateTopSellersChart(AnalyticsService analyticsService, AnalyticsService.TimePeriod period)
            throws SQLException {
        var topSellers = analyticsService.topSellersFor(period);
        topSellersChart.getChildren().clear();
        if (topSellers.isEmpty()) {
            Label emptyState = new Label("No sales in this period");
            emptyState.getStyleClass().add("top-sellers-empty");
            topSellersChart.getChildren().add(emptyState);
            return;
        }

        int maxUnitsSold = topSellers.stream()
                .mapToInt(TopSellerDTO::getUnitsSold)
                .max()
                .orElse(1);
        for (TopSellerDTO seller : topSellers) {
            double relativeWidth = (double) seller.getUnitsSold() / maxUnitsSold;
            StackPane bar = new StackPane();
            bar.getStyleClass().add("top-seller-track");
            bar.setMaxWidth(Double.MAX_VALUE);
            bar.setPrefHeight(28);

            Region fill = new Region();
            fill.getStyleClass().add("top-seller-fill");
            fill.setMaxHeight(Double.MAX_VALUE);
            fill.maxWidthProperty().bind(bar.widthProperty().multiply(relativeWidth));
            fill.prefWidthProperty().bind(fill.maxWidthProperty());

            Label name = new Label(seller.getProductName());
            name.getStyleClass().add("top-seller-name");
            name.setMaxWidth(Double.MAX_VALUE);
            name.setTextOverrun(OverrunStyle.ELLIPSIS);

            Label units = new Label(seller.getUnitsSold() + " sold");
            units.getStyleClass().add("top-seller-units");

            bar.getChildren().setAll(fill, name, units);
            StackPane.setAlignment(fill, Pos.CENTER_LEFT);
            StackPane.setAlignment(name, Pos.CENTER_LEFT);
            StackPane.setAlignment(units, Pos.CENTER_RIGHT);
            topSellersChart.getChildren().add(bar);
        }
    }


}
