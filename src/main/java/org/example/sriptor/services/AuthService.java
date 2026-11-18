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
                    logger.debug("Account registration rejected because the account already exists");
                    return SignupController.SignupStatus.ALREADY_EXISTS;
                }
                else{
                    String hash = hashArgon.registerPasswordPHC(password);
                    UUID newUuid = uniqueIdentification.generateUID();
                    user newuser = new user(newUuid, fname, lname, email, hash, ADMIN, phoneNumber);
                    userDao.save(newuser, connection);
                    logger.info("New account registered");
                    return SignupController.SignupStatus.SUCCESS;
                }
            } catch (Exception e) {
                logger.error("An error occurred while registering an account", e);
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
                    String storedPassword = userDao.getStoredPHC(email, connection);
                    if(hashArgon.verifyLogin(password, storedPassword)){
                        logger.debug("Account authentication succeeded");
                        return loginController.LoginStatus.AUTHENTIC;
                    }else{
                        logger.debug("Account authentication failed");
                        return loginController.LoginStatus.INAUTHENTIC;
                    }
                }
                else{
                    logger.debug("Account authentication failed");
                    return loginController.LoginStatus.ACCOUNT_DOESNT_EXIST;
                }
            } catch(Exception e){
                logger.error("An error occurred while authenticating an account", e);
                throw new RuntimeException(e);

            }
        }
        return loginController.LoginStatus.FAILURE;

    }


}
