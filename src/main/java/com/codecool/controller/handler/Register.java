package com.codecool.controller.handler;

import com.codecool.dao.UserDao;
import com.codecool.view.reader.Reader;
import com.codecool.view.viewer.View;

public class Register {
    private View viewer;
    private Reader reader;
    private UserDao userDao;

    public Register(View viewer, Reader reader, UserDao userDao) {
        this.viewer = viewer;
        this.reader = reader;
        this.userDao = userDao;
    }

    public void controller() {
        String login = getUserLogin();
        String password = getUserPassword();
        
        if(!userDao.isUserInDatabase(login)) {
            userDao.createUser(login, password);
        } else {
            viewer.displayError("Login already in use");
            reader.promptEnterKey();
        }
    }

    private String getUserLogin() {
        viewer.displayQuestion("Insert your login");
        return reader.getNotEmptyString();
    }

    private String getUserPassword() {
        viewer.displayQuestion("Insert your password");
        return reader.getNotEmptyString();
    }
}
