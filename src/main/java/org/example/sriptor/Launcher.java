package org.example.sriptor;

import javafx.application.Application;
import org.example.sriptor.config.connectDB;

import java.sql.SQLException;

import static java.lang.System.out;

public class Launcher {
    public static void main(String[] args) {
        try {
            connectDB dbConnect = new connectDB();
            if(dbConnect.getConnection() != null){
                if(dbConnect.tableExists(dbConnect.getConnection())){
                    out.println("Tables Exist, Moving on >>>");
                }else {
                    dbConnect.createTableInstance(dbConnect.getConnection());
                }
                dbConnect.closeConnection();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Application.launch(posApplication.class, args);
    }
}
