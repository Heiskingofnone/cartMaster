package org.example.sriptor;

import javafx.application.Application;
import org.example.sriptor.config.connectDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class Launcher {
    private static final Logger logger = LoggerFactory.getLogger(Launcher.class);

    public static void main(String[] args) {
        try {
            connectDB dbConnect = new connectDB();
            dbConnect.createTableInstance(dbConnect.getConnection());
            dbConnect.closeConnection();
        } catch (SQLException e) {
            logger.error("Application startup failed while initializing the database", e);
            throw new RuntimeException(e);
        }
        logger.info("Starting CartMaster");
        Application.launch(posApplication.class, args);
    }
}
