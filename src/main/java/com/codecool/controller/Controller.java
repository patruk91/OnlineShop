package com.codecool.controller;

import com.codecool.controller.handler.BasketOperator;
import com.codecool.controller.handler.ProductChooser;
import com.codecool.dao.UserDao;
import com.codecool.dao.OrderDao;
import com.codecool.dao.ProductDao;
import com.codecool.dao.SQL.OrderDaoSQL;
import com.codecool.dao.SQL.ProductDaoSQL;
import com.codecool.dao.SQL.UserDaoSQL;
import com.codecool.model.Basket;
import com.codecool.model.Product;
import com.codecool.model.User;
import com.codecool.view.reader.Reader;
import com.codecool.view.validator.InputValidator;
import com.codecool.view.viewer.View;
import com.codecool.view.viewer.textViewer.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        user = new User(2, "customer");
        basket = new Basket(user.getId());        // Temporary solution before log in handler is coded
    }

    public void runner() {

//        User user1 = new User(1, "customer", "paul", "paul213434", "jackson");
//        User user2 = new User(2, "customer", "emma", "emma2137", "smith");
//        User user3 = new User(3, "customer", "john", "john587435", "black");
//        List<User> users = new ArrayList<>();
//        users.add(user1);
//        users.add(user2);
//        users.add(user3);

        Product product1 = new Product(1,"gra szczelanka", 45, 23.43, true, 2);
        Product product2 = new Product(1,"gra ścigałka", 11, 123.3, false, 1);
        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);

        viewer.displayProductsForUser(products);







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
//                    Login login = new Login(reader, view, inputValidator, userDao);
//                    login.controller(user, basket);
//                    break;
                case "r":
//                    Register register = new Register(reader, view, inputValidator, userDao);
//                    register.controller();
//                    break;
                default:
                    viewer.displayError("Please, provide correct data");
            }
        }
    }
}


