package com.codecool.dao;

import com.codecool.model.User;

import java.util.List;

public interface UserDao {
    void createUser(User user);
    List<User> readUser(int userId);
    void updateUser(User user, String column);
    void deleteUser(User user);
}
