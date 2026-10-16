package org.example.sriptor.dao;

import org.example.sriptor.dto.dailyRevenueSummaryDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class dashboardDAO {
    public dailyRevenueSummaryDTO getDailySummary(Connection connection) throws SQLException {
        String sql = "SELECT " +
                "COALESCE (SUM (Total_amount),0.0) AS daily_revenue, " +
                "COUNT (sale_id) AS transactions_count " +
                "FROM SALE WHERE DATE(sale_date) = DATE('now')";
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            try(ResultSet rs = stmt.executeQuery()){
                double revenue = rs.getDouble("daily_revenue");
                int transactions = rs.getInt("transactions_count");
                return new dailyRevenueSummaryDTO(revenue, transactions);
            } catch (SQLException e){
                e.printStackTrace();
            }
            return new dailyRevenueSummaryDTO(0.0, 0);
        }
    }

}
