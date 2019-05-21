package com.codecool.controller;

import com.codecool.controller.handler.BasketOperator;
import com.codecool.controller.handler.ProductChooser;
import com.codecool.controller.handler.Register;
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
        user = new User(0, "customer");
        basket = new Basket(user.getId());        // Temporary solution before log in handler is coded
    }

    public void runner() {
        boolean exitApp = false;
        while(!exitApp) {
            viewer.clearScreen();

            if(user.getId() == 0) {
                viewer.displayMenu("e. Exit, s. Show products, li. Login, r. Register");
            } else {
                viewer.displayMenu("e. Exit, s. Place order, lo. Log out, b. Basket");
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
                    BasketOperator basketOperator = new BasketOperator(reader, viewer, orderDao);
                    basketOperator.controller(basket);
                    break;
                case "li":
                case "lo":
//                    Login login = new Login(reader, viewer, inputValidator, userDao);
//                    login.controller(user, basket);
//                    break;
                case "r":
                    Register register = new Register(viewer, reader, userDao);
                    register.controller();
                    break;
                default:
                    viewer.displayError("Please, provide correct data");
            }
        }
    }
}


