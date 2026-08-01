package org.example.sriptor.config;

import java.sql.*;

import static java.lang.System.err;
import static java.lang.System.out;


public class connectDB {


private final Connection connection;



    public connectDB() throws SQLException{
        String url = "jdbc:sqlite:pos_store.db";//database url
        try {
            connection = DriverManager.getConnection(url);
            out.println("connection successful");
        } catch (SQLException e) {
            err.println("Error connecting to database");
            throw new RuntimeException(e);
        }

    }
    public Connection getConnection(){
        return connection;
    }
    public void closeConnection(){
        try{
            if(connection != null){
                out.println("Connection closed");
                connection.close();
            }
        } catch (SQLException e) {
            err.println("Cannot closed non-existent connection");
            throw new RuntimeException(e);
        }
    }

    public boolean tableExists(Connection getConnection) throws SQLException{
        DatabaseMetaData meta = getConnection.getMetaData();

        try(ResultSet rs = meta.getTables(null, null, "USERS", new String[]{"TABLE"})){

            return rs.next();
        }
    }

    public void createTableInstance(Connection getConnection) throws SQLException {

        String sql = "CREATE TABLE IF NOT EXISTS USERS ("
                    +"id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    +"first_name TEXT NOT NULL,"
                    +"last_name TEXT NOT NULL,"
                    +"username TEXT NOT NULL,"
                    +"password TEXT NOT NULL,"
                    +"role TEXT NOT NULL CHECK(role IN ('ADMIN', 'CASHIER')),"
                    +"is_active INTEGER NOT NULL CHECK(is_active IN(0,1)),"
                    +"created_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,"
                    +"updated_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP); " +
                "CREATE TABLE IF NOT EXISTS SALE(" +
                "sale_id TEXT PRIMARY KEY DEFAULT (hex(randomblob(4)))," +
                "user_id INTEGER REFERENCES USERS(id)," +
                "sale_date TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                "total_amount DECIMAL(10,2) NOT NULL ); " +
                "CREATE TABLE IF NOT EXISTS CATEGORY(" +
                "category_id TEXT PRIMARY KEY DEFAULT (hex(randomblob(4)))," +
                "category_name TEXT NOT NULL ); " +
                "CREATE TABLE IF NOT EXISTS PRODUCT(" +
                "product_id TEXT PRIMARY KEY DEFAULT (hex(randomblob(6)))," +
                "product_name TEXT NOT NULL," +
                "category_id TEXT REFERENCES CATEGORY(category_id)," +
                "price DECIMAL(10,2) NOT NULL," +
                "quantity INTEGER NOT NULL," +
                "image_path TEXT); " +
                "CREATE TABLE IF NOT EXISTS SALE_ITEMS(" +
                "sale_item_id TEXT PRIMARY KEY DEFAULT (hex(randomblob(4)))," +
                "sale_id TEXT REFERENCES SALE(sale_id)," +
                "product_id TEXT REFERENCES PRODUCT(product_id)," +
                "quantity INTEGER NOT NULL," +
                "unit_price DECIMAL(10,2) NOT NULL ); ";
        try(Statement stmt = getConnection.createStatement()){
            stmt.executeUpdate(sql);
            out.println("Users Table Created Successfully");
        } catch (SQLException e) {
            err.println("Users Table already exists");
            throw new RuntimeException(e);
        }
    }





}
