package com.codecool.dao.SQL;

import com.codecool.dao.UserDao;

import com.codecool.model.Address;
import com.codecool.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoSQL implements UserDao {
    @Override
    public void createUser(String login, String password) {
        if (!isUserInDatabase(login)) {
            try (Connection connection = DatabaseConnection.getConntectionToDatabase();
                 PreparedStatement insertUser = connection.prepareStatement(
                         "INSERT INTO usersCredentials(userLogin, userPassword) VALUES(?, ?)");
                 PreparedStatement insertDetails = connection.prepareStatement(
                         "INSERT INTO usersDetails(name, lastName) VALUES(NULL, NULL)");
                 PreparedStatement insertUserBase = connection.prepareStatement(
                         "INSERT INTO users(typeId, credentialsId, detailsId) VALUES(2, ?, ?)")){
                insertUserData(insertUser, login, password);
                insertDetails.executeUpdate();

                insertUserBase.setInt(1, getLastCredentialsId(connection));
                insertUserBase.setInt(2, getLastDetailsId(connection));
                insertUserBase.executeUpdate();

                
            } catch (SQLException e) {
                System.out.println("SQLException: " + e.getMessage()
                        + "\nSQLState: " + e.getSQLState()
                        + "\nVendorError: " + e.getErrorCode());
            }
        }

    }

    private int getLastCredentialsId(Connection connection) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT ucid FROM usersCredentials ORDER BY ucid DESC limit 1")) {
            try (ResultSet rs = stmt.executeQuery()) {
                int orderId = 1;
                while (rs.next()) {
                    orderId = rs.getInt("ucid");
                }

                return orderId;
            }
        }
    }

    private int getLastDetailsId(Connection connection) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT udid FROM usersDetails ORDER BY udid DESC limit 1")) {
            try (ResultSet rs = stmt.executeQuery()) {
                int orderId = 1;
                while (rs.next()) {
                    orderId = rs.getInt("udid");
                }

                return orderId;
            }
        }
    }

    public boolean isUserInDatabase(String login) {
        List<User> users = readUser("customer");
        for (User user : users) {
            if (user.getLogin().equalsIgnoreCase(login)) {
                return true;
            }
        }
        return false;
    }

    private void insertUserData(PreparedStatement stmt, String login, String password) throws SQLException {
        stmt.setString(1, login);
        stmt.setString(2, password);
        stmt.executeUpdate();
    }




    @Override
    public List<User> readUser(String userType) {
        List<User> users = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConntectionToDatabase();
             PreparedStatement stmt = connection.prepareStatement(
                     "SELECT uid, userLogin, name, lastName, userType, street, country, zipCode, city" +
                             " FROM users JOIN usersCredentials ON credentialsId = ucid " +
                             "LEFT JOIN usersDetails ON detailsId = udid " +
                             "JOIN usersTypes ON users.typeId = usersTypes.utid LEFT " +
                             "JOIN addresses ON users.uid = addresses.userId WHERE userType = ?")) {
            stmt.setString(1, userType);
            createUser(stmt, users);
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }
        return users;
    }

    private void createUser(PreparedStatement stmt, List<User> users) throws SQLException {
        try (ResultSet resultSet = stmt.executeQuery()) {
            while (resultSet.next()) {
                int userId = resultSet.getInt("uid");
                String userLogin = resultSet.getString("userLogin");
                String userType = resultSet.getString("userType");
                User user = new User(userId, userLogin, userType);
                users.add(user);
                createUserDetails(user, resultSet);
                createAddress(user, resultSet);
            }
        }
    }

    private void createUserDetails(User user, ResultSet resultSet) throws SQLException {
        String userName = resultSet.getString("name");
        if (userName != null) {
            String lastName = resultSet.getString("lastName");
            user.setName(userName);
            user.setLastName(lastName);
        }
    }

    private void createAddress(User user, ResultSet resultSet) throws SQLException{
        String street = resultSet.getString("street");
        if (street != null) {
            String country = resultSet.getString("country");
            String zipCode = resultSet.getString("zipCode");
            String city = resultSet.getString("city");
            user.setAddresses(new Address(street, country, zipCode, city));
        }
    }

    @Override
    public void updateUser(User user, String column) {

    }

    @Override
    public void deleteUser(User user) {

    }
}
