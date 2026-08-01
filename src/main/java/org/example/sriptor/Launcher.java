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
                out.println("All done");
                dbConnect.closeConnection();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Application.launch(posApplication.class, args);
    }
}
