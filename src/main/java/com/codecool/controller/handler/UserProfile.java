package com.codecool.controller.handler;

import com.codecool.dao.UserDao;
import com.codecool.model.User;
import com.codecool.view.reader.Reader;
import com.codecool.view.viewer.View;

public class UserProfile {
    User user;
    Reader reader;
    View viewer;
    UserDao userDao;

    public UserProfile(User user, Reader reader, View viewer, UserDao userDao) {
        this.user = user;
        this.reader = reader;
        this.viewer = viewer;
        this.userDao = userDao;
    }

    public User getUser() {
        return user;
    }

    public void controller() {
        boolean backToMenu = false;
        while (!backToMenu) {
            viewer.displayMenu("1. Back to main menu 2. By category 3. By name");
            viewer.displayQuestion("Choose menu option");
            int option = reader.getNumberInRange(1, 3);
            switch (option) {
                case 1:
                    backToMenu = true;
                    break;
                case 2:
                    editUserData();
                    break;
                case 3:
//                    displayMyOrders();
            }
        }
    }
}
