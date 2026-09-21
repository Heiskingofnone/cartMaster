package org.example.sriptor.dao;

import org.example.sriptor.models.user;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class userDAO {

    private final Logger logger = LoggerFactory.getLogger(userDAO.class);
    public void save(user user, Connection connection) throws SQLException {
        logger.debug("Attempting to write User: {} to database", user.getUserId());
        String sql = "INSERT INTO USERS (user_id, first_name, last_name, email, password, role, phone_number)" +
                "VALUES(?, ?, ?, ?, ?, ?, ?);";
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, user.getUserId());
            stmt.setString(2, user.getFirstName());
            stmt.setString(3, user.getLastName());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getPassword());
            stmt.setString(6, user.getRole());
            stmt.setString(7, user.getPhoneNumber());

            stmt.executeUpdate();

            logger.debug("Successfully saved User: {} to database", user.getUserId());

        } catch(SQLException e){
            logger.error("Failed to write User: {} to offline database", user.getUserId(), e);
            throw e;
        }
    }
    public boolean checkByEmail(String email, Connection connection) throws SQLException{
        String sql = "SELECT 1 FROM USERS WHERE LOWER(email) = LOWER(?);";
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            logger.debug("Attempting search for User by email: {} within database", email);
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()){
                return rs.next();
            }
        }
    }
}
