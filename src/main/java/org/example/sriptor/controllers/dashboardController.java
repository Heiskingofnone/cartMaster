package org.example.sriptor.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import org.example.sriptor.dto.kpiSummaryDTO;
import org.example.sriptor.services.AnalyticsService;

import java.sql.SQLException;


public class dashboardController {
    @FXML
    private Label revenue;
    @FXML private Label transactions;
    @FXML private Label netProfit;
    @FXML private Label supplierDues;
    @FXML private StackPane filterButton;
    @FXML private ComboBox<TimePeriod> periodComboBox;



    public enum TimePeriod{
        DAILY("Daily"),
        Weekly("Weekly"),
        MONTHLY("Monthly");

        private final String displayName;
        TimePeriod(String displayName){this.displayName = displayName;}
        @Override
        public String toString(){return this.displayName;}
    }

    public void initialize() throws SQLException {
        periodComboBox.setItems(FXCollections.observableArrayList(TimePeriod.values()));
        periodComboBox.setValue(TimePeriod.DAILY);
        AnalyticsService analyticsService = new AnalyticsService();
        kpiSummaryDTO kpiSummaryDTO = analyticsService.dailySummary();
        revenue.setText(String.valueOf(kpiSummaryDTO.getDailyRevenue()));
        transactions.setText(String.valueOf(kpiSummaryDTO.getTransactionCount()));
        netProfit.setText("0.00");//TODO: Connect net Profit to figures that represents it.
        supplierDues.setText("0.00");//TODO: Connect Supplier dues to figures that represents it

    }

    @FXML public void refreshFilter(MouseEvent event) throws SQLException {
        switch (periodComboBox.getValue()){
            case DAILY -> {
                AnalyticsService analyticsService = new AnalyticsService();
                kpiSummaryDTO kpiSummaryDTO = analyticsService.dailySummary();
                revenue.setText(String.valueOf(kpiSummaryDTO.getDailyRevenue()));
                transactions.setText(String.valueOf(kpiSummaryDTO.getTransactionCount()));
            }
            case Weekly -> {
                AnalyticsService analyticsService = new AnalyticsService();
                kpiSummaryDTO kpiSummaryDTO = analyticsService.weeklySummary();
                revenue.setText(String.valueOf(kpiSummaryDTO.getDailyRevenue()));
                transactions.setText(String.valueOf(kpiSummaryDTO.getTransactionCount()));
            }
            case MONTHLY -> {
                AnalyticsService analyticsService = new AnalyticsService();
                kpiSummaryDTO kpiSummaryDTO = analyticsService.monthlySummary();
                revenue.setText(String.valueOf(kpiSummaryDTO.getDailyRevenue()));
                transactions.setText(String.valueOf(kpiSummaryDTO.getTransactionCount()));
            }
        }
    }


}
