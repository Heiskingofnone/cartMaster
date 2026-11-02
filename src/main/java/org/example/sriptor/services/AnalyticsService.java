package org.example.sriptor.services;

import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import org.example.sriptor.config.connectDB;
import org.example.sriptor.dao.dashboardDAO;
import org.example.sriptor.dto.kpiSummaryDTO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

public class AnalyticsService {
    private final connectDB dbConnect = new connectDB();

    public AnalyticsService() throws SQLException {
    }

    public kpiSummaryDTO dailySummary () throws SQLException {
        dashboardDAO dashboardDAO = new dashboardDAO();
        return dashboardDAO.getDailySummary(dbConnect.getConnection());
    }

    public kpiSummaryDTO weeklySummary() throws SQLException{
        dashboardDAO dashboardDAO = new dashboardDAO();
        return dashboardDAO.getWeeklySummary(dbConnect.getConnection());
    }

    public kpiSummaryDTO monthlySummary() throws SQLException{
        dashboardDAO dashboardDAO = new dashboardDAO();
        return dashboardDAO.getMonthlySummary(dbConnect.getConnection());
    }


    public XYChart.Series<String, Double> parseMapeToSeries() throws SQLException {
        dashboardDAO dashboardDAO = new dashboardDAO();
        Map<LocalDate, Double> inputMap = dashboardDAO.revenueData(dbConnect.getConnection());
        XYChart.Series<String, Double> series = new XYChart.Series<>();
        inputMap.forEach((dateKey, doubleValue)->{
            String formattedDate = dateKey.format(DateTimeFormatter.ISO_LOCAL_DATE);

            series.getData().add(new XYChart.Data<>(formattedDate, doubleValue));
        });
        return series;
    }


}
