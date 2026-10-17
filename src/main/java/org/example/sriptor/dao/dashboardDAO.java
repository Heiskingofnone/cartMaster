package org.example.sriptor.dao;

import org.example.sriptor.dto.kpiSummaryDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class dashboardDAO {
    public kpiSummaryDTO getDailySummary(Connection connection) throws SQLException {
        String sql = "SELECT " +
                "COALESCE (SUM (Total_amount),0.0) AS daily_revenue, " +
                "COUNT (sale_id) AS transactions_count " +
                "FROM SALE WHERE DATE(sale_date) = DATE('now')";
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            try(ResultSet rs = stmt.executeQuery()){
                double revenue = rs.getDouble("daily_revenue");
                int transactions = rs.getInt("transactions_count");
                return new kpiSummaryDTO(revenue, transactions);
            } catch (SQLException e){
                e.printStackTrace();
            }
            return new kpiSummaryDTO(0.0, 0);
        }
    }

    public kpiSummaryDTO getWeeklySummary(Connection connection) throws SQLException{
        String sql = "SELECT " +
                "COALESCE (SUM (Total_amount),0.0) AS daily_revenue, " +
                "COUNT (sale_id) AS transactions_count " +
                "FROM SALE WHERE DATE(sale_date) >= DATE('now', 'weekday 1', '-7 days')";
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            try(ResultSet rs = stmt.executeQuery()){
                double revenue = rs.getDouble("daily_revenue");
                int transactions = rs.getInt("transactions_count");
                return new kpiSummaryDTO(revenue, transactions);
            } catch (SQLException e){
                e.printStackTrace();
            }
            return new kpiSummaryDTO(0.0, 0);
        }
    }

    public kpiSummaryDTO getMonthlySummary(Connection connection) throws SQLException{
        String sql = "SELECT " +
                "COALESCE (SUM (Total_amount),0.0) AS daily_revenue, " +
                "COUNT (sale_id) AS transactions_count " +
                "FROM SALE WHERE DATE(sale_date) >= DATE('now', 'start of month')";
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            try(ResultSet rs = stmt.executeQuery()){
                double revenue = rs.getDouble("daily_revenue");
                int transactions = rs.getInt("transactions_count");
                return new kpiSummaryDTO(revenue, transactions);
            } catch (SQLException e){
                e.printStackTrace();
            }
            return new kpiSummaryDTO(0.0, 0);
        }
    }

}
