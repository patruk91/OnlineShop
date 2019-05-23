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
                 PreparedStatement insertUserCredentials = connection.prepareStatement(
                         "INSERT INTO usersCredentials(userLogin, userPassword) VALUES(?, ?)");
                 PreparedStatement insertDetails = connection.prepareStatement(
                         "INSERT INTO usersDetails(name, lastName) VALUES('-', '-')");
                 PreparedStatement insertUserBase = connection.prepareStatement(
                         "INSERT INTO users(typeId, credentialsId, detailsId) VALUES(2, ?, ?)");
                 PreparedStatement getNewUserId = connection.prepareStatement(
                         "SELECT uid FROM users ORDER BY uid DESC LIMIT 1");
                 PreparedStatement insertEmptyAddress = connection.prepareStatement(
                         "INSERT INTO addresses(street, country, zipCode, city, userId) VALUES('-', '-', '-', '-', ?)")){
                insertUserData(insertUserCredentials, login, password);
                insertDetails.executeUpdate();
                insertUser(connection, insertUserBase);
                int newUserId = selectNewUserId(getNewUserId);
                insertAddress(insertEmptyAddress, newUserId);


            } catch (SQLException e) {
                System.out.println("SQLException: " + e.getMessage()
                        + "\nSQLState: " + e.getSQLState()
                        + "\nVendorError: " + e.getErrorCode());
            }
        }

    }

    private void insertAddress(PreparedStatement insertEmptyAddress, int newUserId) throws SQLException {
        insertEmptyAddress.setInt(1, newUserId);
        insertEmptyAddress.executeUpdate();
    }

    private int selectNewUserId(PreparedStatement getNewUserId) throws SQLException {
        int newUserId = -1;
        try(ResultSet rs = getNewUserId.executeQuery()) {
            while(rs.next()){
                newUserId = rs.getInt("uid");
            }
        }
        return newUserId;
    }

    private void insertUser(Connection connection, PreparedStatement insertUserBase) throws SQLException {
        insertUserBase.setInt(1, getLastCredentialsId(connection));
        insertUserBase.setInt(2, getLastDetailsId(connection));
        insertUserBase.executeUpdate();
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
        List<User> users = readUser();
        for (User user : users) {
            if (user.getLogin().equalsIgnoreCase(login)) {
                return true;
            }
        }
        return false;
    }

    public boolean isPasswordCorrect(String login, String password) throws SQLException {
        try (Connection connection = DatabaseConnection.getConntectionToDatabase();
             PreparedStatement getPassword = connection.prepareStatement(
                     "SELECT userPassword FROM usersCredentials WHERE userLogin = ?")) {
            getPassword.setString(1, login);

            try (ResultSet rs = getPassword.executeQuery()) {
                String passwordFromDatabase = "";
                while (rs.next()) {
                    passwordFromDatabase = rs.getString("userPassword");
                }

                if(!passwordFromDatabase.isBlank() && passwordFromDatabase.equals(password)) {
                    return true;
                }
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
    public List<User> readUser() {
        List<User> users = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConntectionToDatabase();
             PreparedStatement stmt = connection.prepareStatement(
                     "SELECT uid, userLogin, name, lastName, userType, street, country, zipCode, city" +
                             " FROM users JOIN usersCredentials ON credentialsId = ucid " +
                             "LEFT JOIN usersDetails ON detailsId = udid " +
                             "JOIN usersTypes ON users.typeId = usersTypes.utid LEFT " +
                             "JOIN addresses ON users.uid = addresses.userId")) {
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
    public void updateUser(User user) {
        try (Connection connection = DatabaseConnection.getConntectionToDatabase();
             PreparedStatement getUserDetailsId = connection.prepareStatement(
                     "SELECT detailsId FROM users WHERE uid = ?");
             PreparedStatement updateDetails = connection.prepareStatement(
                     "UPDATE usersDetails SET name = ?, lastName = ? WHERE udid = ?");
             PreparedStatement updateAddress = connection.prepareStatement(
                     "UPDATE addresses SET street = ?, country = ?, zipCode = ?, city = ? WHERE userId = ?");
             ){
            int userDetailsId = selectUserDetailsId(getUserDetailsId, user);
            UpdateUserDetails(user, updateDetails, userDetailsId);
            UpdateUserAddress(user, updateAddress);


        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }
    }

    private int selectUserDetailsId(PreparedStatement getUserDetailsId, User user) throws SQLException {
        getUserDetailsId.setInt(1, user.getId());
        int detailsId = -1;
        try(ResultSet rs = getUserDetailsId.executeQuery()) {
            while(rs.next()) {
                detailsId = rs.getInt("detailsId");
            }
        }
        return detailsId;
    }

    private void UpdateUserDetails(User user, PreparedStatement updateDetails, int userDetailsId) throws SQLException {
        updateDetails.setString(1, user.getName());
        updateDetails.setString(2, user.getLastName());
        updateDetails.setInt(3, userDetailsId);
        updateDetails.executeUpdate();
    }

    private void UpdateUserAddress(User user, PreparedStatement updateAddress) throws SQLException {
        updateAddress.setString(1, user.getAddres().getStreet());
        updateAddress.setString(2, user.getAddres().getCountry());
        updateAddress.setString(3, user.getAddres().getZipCode());
        updateAddress.setString(4, user.getAddres().getCity());
        updateAddress.setInt(5, user.getId());
        updateAddress.executeUpdate();
    }

    @Override
    public void deleteUser(User user) {

    }
}