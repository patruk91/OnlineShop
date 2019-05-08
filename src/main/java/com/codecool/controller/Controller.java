package com.codecool.controller;

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

        }
    }
}


