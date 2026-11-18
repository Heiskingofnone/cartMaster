package org.example.sriptor.services;

import javafx.scene.chart.XYChart;
import org.example.sriptor.config.connectDB;
import org.example.sriptor.dao.dashboardDAO;
import org.example.sriptor.dto.kpiSummaryDTO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnalyticsService {
    private static final Logger logger = LoggerFactory.getLogger(AnalyticsService.class);
    private final connectDB dbConnect = new connectDB();

    public enum TimePeriod {
        DAILY("Daily"),
        WEEKLY("Weekly"),
        MONTHLY("Monthly");

        private final String displayName;

        TimePeriod(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

    public AnalyticsService() throws SQLException {
    }

    public kpiSummaryDTO summaryFor(TimePeriod period) throws SQLException {
        if (period == null) {
            throw new IllegalArgumentException("Time period must not be null");
        }
        dashboardDAO dashboardDAO = new dashboardDAO();
        kpiSummaryDTO summary = switch (period) {
            case DAILY -> dashboardDAO.getDailySummary(dbConnect.getConnection());
            case WEEKLY -> dashboardDAO.getWeeklySummary(dbConnect.getConnection());
            case MONTHLY -> dashboardDAO.getMonthlySummary(dbConnect.getConnection());
        };
        logger.debug("Generated {} analytics summary", period);
        return summary;
    }

    public XYChart.Series<String, Double> revenueSeriesFor(TimePeriod period) throws SQLException {
        if (period == null) {
            throw new IllegalArgumentException("Time period must not be null");
        }
        dashboardDAO dashboardDAO = new dashboardDAO();
        Map<String, Double> revenueByPeriod = new LinkedHashMap<>();
        switch (period) {
            case DAILY -> dashboardDAO.dailyComparisonData(dbConnect.getConnection())
                    .forEach((date, revenue) ->
                            revenueByPeriod.put(date.format(DateTimeFormatter.ISO_LOCAL_DATE), revenue));
            case WEEKLY -> revenueByPeriod.putAll(dashboardDAO.weeklyComparisonData(dbConnect.getConnection()));
            case MONTHLY -> revenueByPeriod.putAll(dashboardDAO.monthlyComparisonData(dbConnect.getConnection()));
        }
        XYChart.Series<String, Double> series = new XYChart.Series<>();
        revenueByPeriod.forEach((label, amount) ->
                series.getData().add(new XYChart.Data<>(label, amount)));
        logger.debug("Generated {} revenue chart series with {} data points", period, revenueByPeriod.size());
        return series;
    }


}
