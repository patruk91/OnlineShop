package com.codecool.controller;

import com.codecool.controller.handler.*;
import com.codecool.dao.UserDao;
import com.codecool.dao.OrderDao;
import com.codecool.dao.ProductDao;
import com.codecool.dao.SQL.OrderDaoSQL;
import com.codecool.dao.SQL.ProductDaoSQL;
import com.codecool.dao.SQL.UserDaoSQL;
import com.codecool.model.Basket;
import com.codecool.model.User;
import com.codecool.view.reader.Reader;
import com.codecool.view.validator.InputValidator;
import com.codecool.view.viewer.View;
import com.codecool.view.viewer.textViewer.TextView;

public class Controller {

    private UserDao userDao = new UserDaoSQL();
    private ProductDao productDao = new ProductDaoSQL();
    private OrderDao orderDao = new OrderDaoSQL();
    private View viewer = new TextView();
    private InputValidator inputValidator = new InputValidator();
    private Reader reader = new Reader(viewer, inputValidator);
    private User user;
    private Basket basket;

    public Controller() {
        user = new User(0, "anonymous", "anonymous");
        basket = new Basket(0);
    }

    public void runner() {
        boolean exitApp = false;
        while(!exitApp) {
            viewer.clearScreen();
            viewer.displayMessage("User: " + user.getLogin());
            if(user.getId() == 0) {
                viewer.displayMenu("e. Exit, s. Show products, li. Login, r. Register");
            } else if (user.getId() == 1) {
                viewer.displayMenu("e. Exit, s. Edit order, lo. Log out, t. Admin Tools");
            } else {
                viewer.displayMenu("e. Exit, s. Place order, lo. Log out, b. Basket, p. Profile");
            }

            viewer.displayQuestion("Choose menu option");
            String option = reader.getNotEmptyString();
            switch(option) {
                case "e":
                    exitApp = true;
                    break;
                case "s":
                    viewer.clearScreen();
                    ProductChooser productChooser = null;

                    if(user.getId() == 0) {
                        productChooser = new ProductChooser(reader, viewer, productDao);
                    } else {
                        productChooser = new ProductChooser(reader, viewer, productDao, basket);
                    }
                    productChooser.productController(user.getType());
                    break;
                case "b":
                    if(user.getType().equals("customer")) {
                        BasketOperator basketOperator = new BasketOperator(reader, viewer, orderDao);
                        basketOperator.controller(basket);
                    } else {
                        viewer.displayError("Please, provide correct data");
                    }
                    break;
                case "li":
                case "lo":
                    Login login = new Login(user, reader, viewer, userDao);
                    login.controller(basket);
                    this.user = login.getUser();
                    break;
                case "r":
                    if (user.getType().equals("anonymous")) {
                        Register register = new Register(viewer, reader, userDao);
                        register.controller();
                    } else {
                        viewer.displayError("Please, provide correct data");
                    }
                    break;
                case "p":
                    if(user.getType().equals("customer")) {
                        UserProfile userProfile = new UserProfile(user, reader, viewer, userDao, orderDao);
                        userProfile.controller();
                        this.user = userProfile.getUser();
                    } else {
                        viewer.displayError("Please, provide correct data");
                    }
                    break;
                case "t":
                    if (user.getType().equals("admin")) {
                        AdminTools adminTools = new AdminTools(viewer, reader, productDao, orderDao);
                        adminTools.adminController();
                    } else {
                        viewer.displayError("Please, provide correct data");
                    }
                    break;
                default:
                    viewer.displayError("Please, provide correct data");
            }
        }
    }
}


