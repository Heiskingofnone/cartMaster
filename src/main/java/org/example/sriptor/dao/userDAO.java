package org.example.sriptor.dao;

import org.example.sriptor.models.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.example.sriptor.utils.uniqueIdentification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class userDAO {

    private static final Logger logger = LoggerFactory.getLogger(userDAO.class);
    public void save(user user, Connection connection) throws SQLException {
        logger.debug("Attempting to save a user account");
        String sql = "INSERT INTO USERS (user_id, first_name, last_name, email, password, role, phone_number)" +
                "VALUES(?, ?, ?, ?, ?, ?, ?);";
        try(PreparedStatement stmt = connection.prepareStatement(sql)){

            stmt.setBytes(1, uniqueIdentification.uuidToBytes(user.getUserId()));
            stmt.setString(2, user.getFirstName());
            stmt.setString(3, user.getLastName());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getPassword());
            stmt.setString(6, user.getRole());
            stmt.setString(7, user.getPhoneNumber());

            stmt.executeUpdate();

            logger.debug("Successfully saved a user account");

        }
    }
    public boolean checkByEmail(String email, Connection connection) throws SQLException{
        String sql = "SELECT 1 FROM USERS WHERE LOWER(email) = LOWER(?);";
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            logger.debug("Checking whether a user account exists");
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()){
                return rs.next();
            }

        }
    }

    public String getStoredPHC(String email, Connection connection) throws SQLException{
        String sql = "SELECT password FROM USERS WHERE LOWER(email) = LOWER(?)";
        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
            logger.debug("Retrieving stored authentication credential");
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()){
                if(rs.next()) return rs.getString(1);
            }
        }
        logger.debug("No stored authentication credential was found");
        return null;
    }
}
