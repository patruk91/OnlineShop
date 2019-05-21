package com.codecool.dao;

import com.codecool.model.User;

import java.sql.SQLException;
import java.util.List;

public interface UserDao {
    void createUser(String login, String password);
    List<User> readUser(String userType);
    boolean isUserInDatabase(String login);
    boolean isPasswordCorrect(String login, String password) throws SQLException;
    void updateUser(User user, String column);
    void deleteUser(User user);

}
