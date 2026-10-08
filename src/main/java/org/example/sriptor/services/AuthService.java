package org.example.sriptor.services;

import org.example.sriptor.config.connectDB;
import org.example.sriptor.controllers.SignupController;
import org.example.sriptor.controllers.loginController;
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

    public AuthService() throws SQLException {
    }

    //Returns a SignupStatus enum instead, makes code more readable and understandable
    public SignupController.SignupStatus signupNewUser(String fname, String lname, String email, String password, String phoneNumber) throws Exception{
        if(dbConnect.getConnection() != null){
            try(Connection connection = dbConnect.getConnection()) {
                userDAO userDao = new userDAO();
                if(userDao.checkByEmail(email, connection)){
                    logger.debug("Account by email: {} already exists", email);
                    return SignupController.SignupStatus.ALREADY_EXISTS;
                }
                else{
                    logger.debug("Account by email: {} does not exist", email);
                    String hash = hashArgon.registerPasswordPHC(password);
                    UUID newUuid = uniqueIdentification.generateUID();
                    user newuser = new user(newUuid, fname, lname, email, hash, ADMIN, phoneNumber);
                    userDao.save(newuser, connection);
                    return SignupController.SignupStatus.SUCCESS;
                }
            } catch (Exception e) {
                logger.error("An error occurred whiles singing up");
                throw new RuntimeException(e);
            }
        }
        return SignupController.SignupStatus.FAILURE;

    }

    public loginController.LoginStatus loginValidation(String email, String password){
        if(dbConnect.getConnection() != null){
            try(Connection connection = dbConnect.getConnection()) {
                userDAO userDao = new userDAO();
                if(userDao.checkByEmail(email, connection)){
                    logger.debug("Account by email {} exist", email);
                    String storedPassword = userDao.getStoredPHC(email, connection);
                    if(hashArgon.verifyLogin(password, storedPassword)){
                        logger.info("Password was a match, account {} verified and authenticated", email);
                        return loginController.LoginStatus.AUTHENTIC;
                    }else{
                        logger.info("Account not verified or Authentic");
                        return loginController.LoginStatus.INAUTHENTIC;
                    }
                }
                else{
                    logger.debug("Account by email {} does not exist", email);
                    return loginController.LoginStatus.ACCOUNT_DOESNT_EXIST;
                }
            } catch(Exception e){
                logger.error("An error occurred when logging in");
                throw new RuntimeException(e);

            }
        }
        return loginController.LoginStatus.FAILURE;

    }


}
