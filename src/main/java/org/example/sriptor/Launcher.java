package org.example.sriptor;

import javafx.application.Application;
import org.example.sriptor.config.DatabaseConfig;

public class Launcher {
    public static void main(String[] args) {
        //DatabaseConfig config = new DatabaseConfig("sys", "Heisamsterdam123456", "sysdba", 1521, "localhost", "XEPDB1");
        Application.launch(posApplication.class, args);
    }
}
