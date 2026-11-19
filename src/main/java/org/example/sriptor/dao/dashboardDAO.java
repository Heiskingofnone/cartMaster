package org.example.sriptor.dao;

import org.example.sriptor.dto.kpiSummaryDTO;
import org.example.sriptor.dto.TopSellerDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class dashboardDAO {
    private static final Logger logger = LoggerFactory.getLogger(dashboardDAO.class);
    private static final int TOP_SELLERS_LIMIT = 5;

    public List<TopSellerDTO> getTopSellers(Connection connection, LocalDate startDate,
                                            LocalDate endDateExclusive) throws SQLException {
        String sql = "SELECT p.product_name, " +
                "SUM(si.quantity) AS units_sold, " +
                "SUM(si.quantity * si.unit_price) AS revenue " +
                "FROM SALE_ITEMS si " +
                "JOIN SALE s ON s.sale_id = si.sale_id " +
                "JOIN PRODUCT p ON p.product_id = si.product_id " +
                "WHERE DATE(s.sale_date) >= DATE(?) AND DATE(s.sale_date) < DATE(?) " +
                "GROUP BY p.product_id, p.product_name " +
                "ORDER BY units_sold DESC, revenue DESC, p.product_name ASC " +
                "LIMIT ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, startDate.toString());
            stmt.setString(2, endDateExclusive.toString());
            stmt.setInt(3, TOP_SELLERS_LIMIT);

            List<TopSellerDTO> topSellers = new ArrayList<>();
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    topSellers.add(new TopSellerDTO(
                            rs.getString("product_name"),
                            rs.getInt("units_sold"),
                            rs.getDouble("revenue")
                    ));
                }
            }
            logger.debug("Retrieved {} top-selling products for {} through {}",
                    topSellers.size(), startDate, endDateExclusive.minusDays(1));
            return topSellers;
        } catch (SQLException e) {
            logger.error("Failed to retrieve top-selling products", e);
            throw e;
        }
    }

    public kpiSummaryDTO getDailySummary(Connection connection) throws SQLException {
        String sql = "SELECT " +
                "COALESCE (SUM (Total_amount),0.0) AS daily_revenue, " +
                "COUNT (sale_id) AS transactions_count, " +
                estimatedProfitSubquery("DATE(s.sale_date) = DATE('now')") +
                " AS estimated_profit " +
                "FROM SALE WHERE DATE(sale_date) = DATE('now')";
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            try(ResultSet rs = stmt.executeQuery()){
                double revenue = rs.getDouble("daily_revenue");
                int transactions = rs.getInt("transactions_count");
                return new kpiSummaryDTO(revenue, transactions, rs.getDouble("estimated_profit"));
            }
        } catch (SQLException e) {
            logger.error("Failed to retrieve daily dashboard summary", e);
            throw e;
        }
    }

    public kpiSummaryDTO getWeeklySummary(Connection connection) throws SQLException{
        String sql = "SELECT " +
                "COALESCE (SUM (Total_amount),0.0) AS daily_revenue, " +
                "COUNT (sale_id) AS transactions_count, " +
                estimatedProfitSubquery("DATE(s.sale_date) >= DATE('now', 'weekday 1', '-7 days') " +
                        "AND DATE(s.sale_date) <= DATE('now')") +
                " AS estimated_profit " +
                "FROM SALE WHERE DATE(sale_date) >= DATE('now', 'weekday 1', '-7 days')";
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            try(ResultSet rs = stmt.executeQuery()){
                double revenue = rs.getDouble("daily_revenue");
                int transactions = rs.getInt("transactions_count");
                return new kpiSummaryDTO(revenue, transactions, rs.getDouble("estimated_profit"));
            }
        } catch (SQLException e) {
            logger.error("Failed to retrieve weekly dashboard summary", e);
            throw e;
        }
    }

    public kpiSummaryDTO getMonthlySummary(Connection connection) throws SQLException{
        String sql = "SELECT " +
                "COALESCE (SUM (Total_amount),0.0) AS daily_revenue, " +
                "COUNT (sale_id) AS transactions_count, " +
                estimatedProfitSubquery("DATE(s.sale_date) >= DATE('now', 'start of month') " +
                        "AND DATE(s.sale_date) <= DATE('now')") +
                " AS estimated_profit " +
                "FROM SALE WHERE DATE(sale_date) >= DATE('now', 'start of month')";
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            try(ResultSet rs = stmt.executeQuery()){
                double revenue = rs.getDouble("daily_revenue");
                int transactions = rs.getInt("transactions_count");
                return new kpiSummaryDTO(revenue, transactions, rs.getDouble("estimated_profit"));
            }

        } catch (SQLException e) {
            logger.error("Failed to retrieve monthly dashboard summary", e);
            throw e;
        }
    }

    private String estimatedProfitSubquery(String dateCondition) {
        return "COALESCE((SELECT SUM(si.quantity * (si.unit_price - " +
                "COALESCE(si.cost_price_at_sale, p.cost_price, 0))) " +
                "FROM SALE_ITEMS si " +
                "JOIN SALE s ON s.sale_id = si.sale_id " +
                "JOIN PRODUCT p ON p.product_id = si.product_id " +
                "WHERE " + dateCondition + "), 0.0)";
    }

    public Map<LocalDate, Double> dailyComparisonData(Connection connection) throws SQLException{
        String sql =
                "SELECT " +
                "DATE(sale_date) AS sale_date, " +
                "SUM(total_amount) AS daily_revenue " +
                "FROM SALE " +
                "WHERE date(sale_date) >= date('now', 'localtime', '-6 days') " +
                "AND date(sale_date) <= date('now', 'localtime') " +
                "GROUP BY date(sale_date) " +
                "ORDER BY sale_date ASC ";
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            Map<LocalDate, Double> dailyRevenue = new LinkedHashMap<>();
            try(ResultSet rs  = stmt.executeQuery()){
                while(rs.next()){
                    LocalDate date = LocalDate.parse(rs.getString("sale_date"));
                    double revenue = rs.getDouble("daily_revenue");
                    dailyRevenue.put(date, revenue);
                }
            }
            return dailyRevenue;
        } catch (SQLException e) {
            logger.error("Failed to retrieve daily revenue comparison data", e);
            throw e;
        }
    }

    public Map<String, Double> weeklyComparisonData(Connection connection) throws SQLException{
        String sql =
                "SELECT * FROM (" +
                        "SELECT " +
                        "date(sale_date, '-' || ((CAST(strftime('%w', sale_date) AS INTEGER) + 6) % 7) || ' days') AS week_start, " +
                        "SUM(total_amount) AS weekly_revenue " +
                        "FROM SALE " +
                        "WHERE date(sale_date) >= date('now', 'localtime', '-6 days', 'weekday 1', '-21 days') " +
                        "AND date(sale_date) <= date('now', 'localtime') " +
                        "GROUP BY week_start " +
                        "ORDER BY week_start DESC " +
                        "LIMIT 4" +
                        ") sub " +
                        "ORDER BY week_start ASC;";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            Map<String, Double> weeklyRevenue = new LinkedHashMap<>();
            try(ResultSet rs = pstmt.executeQuery()){
                while(rs.next()){
                    String weekStart = rs.getString("week_start");
                    double revenue = rs.getDouble("weekly_revenue");
                    weeklyRevenue.put(weekStart, revenue);
                }
            }
            return weeklyRevenue;
        } catch (SQLException e) {
            logger.error("Failed to retrieve weekly revenue comparison data", e);
            throw e;
        }
    }

    public Map<String, Double> monthlyComparisonData(Connection connection) throws SQLException {
        String sql ="SELECT " +
                "strftime('%Y-%m', sale_date) AS month_key, " +
                "SUM(total_amount) AS monthly_revenue " +
                "FROM SALE " +
                "WHERE strftime('%Y', sale_date) = strftime('%Y', 'now', 'localtime') " +
                "GROUP BY month_key " +
                "ORDER BY month_key ASC;";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            Map<String, Double> monthlyRevenue = new LinkedHashMap<>();
            try(ResultSet rs = pstmt.executeQuery()){
                while(rs.next()){
                    String month = rs.getString("month_key");
                    double revenue = rs.getDouble("monthly_revenue");
                    monthlyRevenue.put(month, revenue);
                }
            }
            return monthlyRevenue;
        } catch (SQLException e) {
            logger.error("Failed to retrieve monthly revenue comparison data", e);
            throw e;
        }
    }

}
