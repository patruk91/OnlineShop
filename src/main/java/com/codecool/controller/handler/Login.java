package com.codecool.controller.handler;

import com.codecool.dao.UserDao;
import com.codecool.model.Basket;
import com.codecool.model.User;
import com.codecool.view.reader.Reader;
import com.codecool.view.viewer.View;

import java.sql.SQLException;

public class Login {
    private Reader reader;
    private View viewer;
    private UserDao userDao;

    public Login(Reader reader, View viewer, UserDao userDao) {
        this.reader = reader;
        this.viewer = viewer;
        this.userDao = userDao;
    }

    public void controller(User user, Basket basket) {
        if(user.getType().equals("anonymous")) {
            loginUser(user, basket);
        } else {
            logOutUser(user, basket);
        }
    }

    private void logOutUser(User user, Basket basket) {
        user.setId(0);
        user.setType("anonymous");
        basket.setUserId(0);
    }

    private void loginUser(User user, Basket basket) {
        String login = getLoginFromUser();
        String userPassword = getPasswordFromUser();
        if (userDao.isUserInDatabase(login)) {
            checkIfPasswordIsCorrect(user, basket, login, userPassword);
        } else {
            viewer.displayError("No user by that login");
        }
    }

    private void checkIfPasswordIsCorrect(User user, Basket basket, String login, String userPassword) {
        try {
            if (userDao.isPasswordCorrect(login, userPassword)) {
                loginUserWithCorrectData(user, basket, login);
            } else {
                viewer.displayError("Incorrect password");
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }
    }

    private void loginUserWithCorrectData(User user, Basket basket, String login) {
        int userId = getUserId(login);
        user.setId(userId);
        user.setType(getUserType(login));
        basket.setUserId(userId);
    }

    private int getUserId(String login) {
        for(User user: userDao.readUser()) {
            if(user.getLogin().equals(login)) {
                return user.getId();
            }
        }
        throw new IllegalArgumentException("No user by that login.");
    }

    private String getUserType(String login) {
        for(User user: userDao.readUser()) {
            if(user.getLogin().equals(login)) {
                return user.getType();
            }
        }
        throw new IllegalArgumentException("No user by that login.");
    }

    private String getLoginFromUser() {
        viewer.displayQuestion("Enter your login");
        return reader.getNotEmptyString();
    }

    private String getPasswordFromUser() {
        viewer.displayQuestion("Enter your password");
        return reader.getNotEmptyString();
    }
}
