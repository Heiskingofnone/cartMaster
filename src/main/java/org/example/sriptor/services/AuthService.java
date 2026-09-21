package org.example.sriptor.services;

import org.example.sriptor.config.connectDB;
import org.example.sriptor.dao.userDAO;
import org.example.sriptor.models.user;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;

import static org.example.sriptor.models.user.Role.ADMIN;

public class AuthService {
    private  user user;
    private connectDB connectDB;
    private final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private final userDAO userDao = new userDAO();

    public void signupNewUser(String fname, String lname, String email, String password, String phoneNumber) throws Exception{
        try(Connection connection = connectDB.getConnection()) {
            if(userDao.checkByEmail(email, connection)){
                logger.debug("Account by email: {} already exists", email);
            }
            else{
                logger.debug("Account by email: {} does not exist", email);
                //Hash password
                //Generate UUID
                user = new user("UUID", fname, lname, email, "Hash", ADMIN, phoneNumber);
                userDao.save(user, connection);
            }
        }
    }
}
