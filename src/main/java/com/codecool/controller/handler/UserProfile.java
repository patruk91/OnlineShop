package com.codecool.controller.handler;

import com.codecool.dao.OrderDao;
import com.codecool.dao.UserDao;
import com.codecool.model.Order;
import com.codecool.model.User;
import com.codecool.view.reader.Reader;
import com.codecool.view.viewer.View;

import java.util.List;

public class UserProfile {
    private User user;
    private Reader reader;
    private View viewer;
    private UserDao userDao;
    private OrderDao orderDao;

    public UserProfile(User user, Reader reader, View viewer, UserDao userDao, OrderDao orderDao) {
        this.user = user;
        this.reader = reader;
        this.viewer = viewer;
        this.userDao = userDao;
        this.orderDao = orderDao;
    }

    public User getUser() {
        return user;
    }

    public void controller() {
        boolean backToMenu = false;
        while (!backToMenu) {
            viewer.displayMenu("1. Back to main menu 2. Edit my data 3. My orders");
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
                    displayMyOrders();
            }
        }
    }

    private void editUserData() {
        boolean endEdition = false;
        while(!endEdition) {
            viewer.displayUserData(user);
            viewer.displayQuestion("Chose data to edit [name, last name, street, country, city, zip code]");
            String option = reader.getNotEmptyString();
            switch (option) {
                case "name":
                    editName();
                    break;
                case "last name":
                    editLastName();
                    break;
                case "street":
                    editStreet();
                    break;
                case "country":
                    editCountry();
                    break;
                case "city":
                    editCity();
                    break;
                case "zip code":
                    editZipCode();
                    break;
                default:
                    viewer.displayError("Incorrect option");
                    break;
            }
            viewer.displayQuestion("Edit more data");
            String editMore = reader.getNotEmptyString();
            switch (editMore) {
                case "yes":
                    break;
                case "no":
                    commitChanges();
                    endEdition = true;
                    break;
                default :
                    viewer.displayError("Incorrect option [yes/no]");
            }

        }
    }

    private void editZipCode() {
        viewer.displayQuestion("Enter zip code");
        String newZipCode = reader.getNotEmptyString();
        user.getAddres().setZipCode(newZipCode);
    }

    private void editCity() {
        viewer.displayQuestion("Enter city name");
        String newCity = reader.getNotEmptyString();
        user.getAddres().setCity(newCity);
    }

    private void editCountry() {
        viewer.displayQuestion("Enter country name");
        String newCountry = reader.getNotEmptyString();
        user.getAddres().setCountry(newCountry);
    }

    private void editStreet() {
        viewer.displayQuestion("Enter street name");
        String newStreet = reader.getNotEmptyString();
        user.getAddres().setStreet(newStreet);
    }

    private void editLastName() {
        viewer.displayQuestion("Enter last name");
        String newLastName = reader.getNotEmptyString();
        user.setLastName(newLastName);
    }

    private void editName() {
        viewer.displayQuestion("Enter name");
        String newName = reader.getNotEmptyString();
        user.setName(newName);
    }

    private void displayMyOrders() {
        List<Order> myOrders = orderDao.readOrder(user.getId());
        viewer.displayOrders(myOrders);
    }

    private void commitChanges() {
        userDao.updateUser(user);
    }
}
