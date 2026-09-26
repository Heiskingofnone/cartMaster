package org.example.sriptor.services;

import org.example.sriptor.config.connectDB;
import org.example.sriptor.dao.userDAO;
import org.example.sriptor.models.user;
import org.example.sriptor.utils.hashArgon;
import org.example.sriptor.utils.uniqueIdentification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

import static org.example.sriptor.models.user.Role.ADMIN;




public class AuthService {
    private final connectDB dbConnect = new connectDB();
    private final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private final userDAO userDao = new userDAO();

    public AuthService() throws SQLException {
    }


    public void signupNewUser(String fname, String lname, String email, String password, String phoneNumber) throws Exception{
        if(dbConnect.getConnection() != null){
            try(Connection connection = dbConnect.getConnection()) {
                if(userDao.checkByEmail(email, connection)){
                    logger.debug("Account by email: {} already exists", email);
                }
                else{
                    logger.debug("Account by email: {} does not exist", email);
                    String hash = hashArgon.registerPasswordPHC(password);
                    UUID newUuid = uniqueIdentification.generateUID();
                    user newuser = new user(newUuid, fname, lname, email, hash, ADMIN, phoneNumber);
                    if(!userDao.checkByEmail(email, connection)) {
                        logger.info("User {} does not exist, creating new user for user {}", email, email);
                        userDao.save(newuser, connection);
                    }else logger.info("User {} already exists within database", email);
                }
            } catch (Exception e) {
                logger.error("An error occurred whiles singing up, try again later");
                throw new RuntimeException(e);
            }
        }
    }



}
