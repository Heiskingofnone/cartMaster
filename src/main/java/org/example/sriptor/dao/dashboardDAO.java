package org.example.sriptor.dao;

import org.example.sriptor.dto.kpiSummaryDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

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

    public Map<LocalDate, Double> dailyComparisonData(Connection connection) throws SQLException{
        String sql =
                "SELECT " +
                "DATE(sale_date) AS sale_date, " +
                "SUM(total_amount) AS daily_revenue " +
                "FROM SALE " +
                        //using date(sale_date) instead of juz sale_date ensures proper filtering which would include today
                "WHERE date(sale_date) > date('now', 'weekday 1', '-6 days', 'localtime') " +
                "AND date(sale_date) <= date('now', 'localtime') " +
                "GROUP BY date(sale_date) " +
                "ORDER BY sale_date ASC ";
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            Map<LocalDate, Double> dailyRevenue = new LinkedHashMap<>();
            try(ResultSet rs  = stmt.executeQuery()){
                while(rs.next()){
                    //String dateStr = rs.getString("sale_date");
                    LocalDate date = LocalDate.parse(rs.getString("sale_date"));
                    double revenue = rs.getDouble("daily_revenue");
                    dailyRevenue.put(date, revenue);
                }
            } catch(SQLException e){
                e.printStackTrace();
            }
            return dailyRevenue;

        }
    }

    public Map<LocalDate, Double> weeklyComparisonData(Connection connection) throws SQLException{
        String sql =
                "SELECT * FROM (" +
                        "SELECT " +
                        "date('sale_date', 'weekday 1', '-7 days') AS week_start, " +
                        "date('sale_date', 'weekday 1', '-1 days') AS week_end, " +
                        "SUM(total_amount) AS weekly_revenue " +
                        "FROM SALE " +
                        "WHERE date(sale_date) <= date('now', 'localtime') " +
                        "GROUP BY week_start, week_end " +
                        "ORDER BY week_start DESC " +
                        "LIMIT 4" +
                        ") sub " +
                        "ORDER BY week_start ASC;";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            Map<LocalDate, Double> weeklyRevenue = new LinkedHashMap<>();
            try(ResultSet rs = pstmt.executeQuery()){
                while(rs.next()){
                    LocalDate date = LocalDate.parse(rs.getString("sale_date"));
                    double revenue = rs.getDouble("weekly_revenue");
                    weeklyRevenue.put(date, revenue);
                }
            } catch(SQLException e){
                e.printStackTrace();
            }
            return weeklyRevenue;
        }
    }

    public Map<LocalDate, Double> monthlyComparisonData(Connection connection) throws SQLException {
        String sql ="SELECT " +
                "strftime('%Y-%m', sale_date) AS month_key, " +
                "SUM(total_amount) AS monthly_revenue " +
                "FROM SALE " +
                "WHERE strftime('%Y', sale_date) = strftime('%Y', 'now', 'localtime') " +
                "GROUP BY month_key " +
                "ORDER BY month_key ASC;";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            Map<LocalDate, Double> monthlyRevenue = new LinkedHashMap<>();
            try(ResultSet rs = pstmt.executeQuery()){
                while(rs.next()){
                    LocalDate date = LocalDate.parse(rs.getString("sale_date"));
                    double revenue = rs.getDouble("monthly_revenue");
                    monthlyRevenue.put(date, revenue);
                }
            }
            catch(SQLException e){
                e.printStackTrace();
            }
            return  monthlyRevenue;
        }
    }

}
