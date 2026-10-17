package org.example.sriptor.services;

import org.example.sriptor.config.connectDB;
import org.example.sriptor.dao.dashboardDAO;
import org.example.sriptor.dto.kpiSummaryDTO;

import java.sql.SQLException;

public class AnalyticsService {
    private final connectDB dbConnect = new connectDB();

    public AnalyticsService() throws SQLException {
    }

    public kpiSummaryDTO dailySummary () throws SQLException {
        dashboardDAO dashboardDAO = new dashboardDAO();
        return dashboardDAO.getDailySummary(dbConnect.getConnection());
    }
}
