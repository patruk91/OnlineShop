package com.codecool.controller;

import com.codecool.controller.handler.BasketOperator;
import com.codecool.controller.handler.Login;
import com.codecool.controller.handler.ProductChooser;
import com.codecool.controller.handler.Register;
import com.codecool.dao.UserDao;
import com.codecool.dao.OrderDao;
import com.codecool.dao.ProductDao;
import com.codecool.dao.SQL.OrderDaoSQL;
import com.codecool.dao.SQL.ProductDaoSQL;
import com.codecool.dao.SQL.UserDaoSQL;
import com.codecool.model.Basket;
import com.codecool.reader.Reader;
import com.codecool.validator.InputValidator;
import com.codecool.viewer.View;
import com.codecool.viewer.textViewer.TextView;

public class Controller {
    private UserDao userDao = new UserDaoSQL();
    private ProductDao productDao = new ProductDaoSQL();
    private OrderDao orderDao = new OrderDaoSQL();
    private View viewer = new TextView();
    private InputValidator inputValidator = new InputValidator();
    private Reader reader = new Reader(viewer, inputValidator);
    private int userId;
    private Basket basket;

    public Controller() {
        userId = 0;
    }

    public void runner() {
        boolean exitApp = false;
        while(!exitApp) {
            viewer.clearScrean();

            if(userId == 0) {
                viewer.displayMenu("e. Exit, s. Show products, li. Login, r. Register");
            } else {
                viewer.displayMenu("e. Exit, s. Place order, lo. Log out, b. Basket");
            }

            String option = reader.getStringFromUser();
            switch(option) {
                case "e":
                    exitApp = true;
                    break;
                case "s":
                    ProductChooser productChooser = null;

                    if(userId == 0) {
                        productChooser = new ProductChooser(reader, viewer, inputValidator, productDao);
                    } else {
                        productChooser = new ProductChooser(reader, viewer, inputValidator, productDao, basket);
                    }
                    productChooser.productController();
                case "b":
                    BasketOperator basketOperator = new BasketOperator(reader, viewer, inputValidator, orderDao, productDao);
                    basketOperator.controller(basket);
                case "li":
                case "lo":
                    Login login = new Login(reader, viewer, inputValidator, userDao);
                    login.controller(userId, basket);
                case "r":
                    Register register = new Register(reader, viewer, inputValidator, userDao);
            }
        }
    }
}


