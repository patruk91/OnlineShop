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
    private User user;

    public Login(User user, Reader reader, View viewer, UserDao userDao) {
        this.reader = reader;
        this.viewer = viewer;
        this.userDao = userDao;
        this.user = user;
    }

    public void controller(Basket basket) {
        if(user.getType().equals("anonymous")) {
            loginUser(basket);
        } else {
            logOutUser(basket);
        }
    }

    public User getUser() {
        return user;
    }

    private void logOutUser(Basket basket) {
        user = new User(0, "anonymous", "anonymous");
        basket.setUserId(0);
        basket.getOrderDetails().clear();
    }

    private void loginUser(Basket basket) {
        String login = getLoginFromUser();
        String userPassword = getPasswordFromUser();
        if (userDao.isUserInDatabase(login)) {
            checkIfPasswordIsCorrect(basket, login, userPassword);
        } else {
            viewer.displayError("No user by that login");
            reader.promptEnterKey();
        }
    }

    private void checkIfPasswordIsCorrect(Basket basket, String login, String userPassword) {
        try {
            if (userDao.isPasswordCorrect(login, userPassword)) {
                loginUserWithCorrectData(basket, login);
            } else {
                viewer.displayError("Incorrect password");
                reader.promptEnterKey();
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }
    }

    private void loginUserWithCorrectData(Basket basket, String login) {
        user = getUser(login);
        basket.setUserId(user.getId());
    }

    private User getUser(String login) {
        for(User user: userDao.readUser()) {
            if(user.getLogin().equals(login)) {
                return user;
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
