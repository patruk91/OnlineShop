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
            viewer.displayQuestion("Chose data to edit");
            String option = reader.getNotEmptyString();
            switch (option) {
                case "name":
                    String newName = reader.getNotEmptyString();
                    user.setName(newName);
                    break;
                case "last name":
                    String newLastName = reader.getNotEmptyString();
                    user.setLastName(newLastName);
                    break;
                case "street":
                    String newStreet = reader.getNotEmptyString();
                    user.getAddres().setStreet(newStreet);
                    break;
                case "country":
                    String newCountry = reader.getNotEmptyString();
                    user.getAddres().setCountry(newCountry);
                    break;
                case "city":
                    String newCity = reader.getNotEmptyString();
                    user.getAddres().setCity(newCity);
                    break;
                case "zip code":
                    String newZipCodet = reader.getNotEmptyString();
                    user.getAddres().setZipCode(newZipCodet);
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

    private void displayMyOrders() {
        List<Order> myOrders = orderDao.readOrder(user.getId());
        viewer.displayOrders(myOrders);
    }

    private void commitChanges() {
        userDao.updateUser(user);
    }
}
