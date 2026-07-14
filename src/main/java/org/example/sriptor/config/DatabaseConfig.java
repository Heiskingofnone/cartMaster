package org.example.sriptor.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConfig {
    private String user;
    private String password;
    private String internalLogon;
    private int port;
    private String host;
    private String serviceName;


    //Constructor and destructor

    public DatabaseConfig(String user, String password, String internalLogon, int port, String host, String serviceName) {
        this.user = user;
        this.password = password;
        this.internalLogon = internalLogon;
        this.port = port;
        this.host = host;
        this.serviceName = serviceName;



        String url = "jdbc:oracle:thin:@"+host+":"+port+"/"+serviceName;
        //String url = "jdbc:"

        Properties props = new Properties();
        props.setProperty("user", user);
        props.setProperty("password", password);
        props.setProperty("internal_logon", internalLogon);
        System.out.println("Credentials Set...");
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            Connection connection = DriverManager.getConnection(url, props);
            System.out.println("Database Connected Successfully.");
        } catch (ClassNotFoundException e){
            System.err.println("Driver class not found!");
            e.printStackTrace();
        }
        catch (SQLException e) {
            System.err.println("Connection failed: "+ e.getMessage());
            throw new RuntimeException(e);
        }

    }

    //Setters
    public void setUser(String user) {
        this.user = user;
    }
    public void setPassword(String password) {

    }
    public void setInternalLogon(String internalLogon) {
        this.internalLogon = internalLogon;
    }
    public void setPort(int port){
        this.port = port;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }


}
