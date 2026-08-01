package org.example.sriptor.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static java.lang.System.err;
import static java.lang.System.out;


public class connectDB {


private Connection connection;



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




}
