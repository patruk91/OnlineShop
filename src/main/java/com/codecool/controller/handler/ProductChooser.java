package com.codecool.controller.handler;

import com.codecool.dao.ProductDao;
import com.codecool.model.Basket;
import com.codecool.model.Product;
import com.codecool.reader.Reader;
import com.codecool.validator.InputValidator;
import com.codecool.viewer.View;

import java.util.List;
import java.util.TreeMap;

public class ProductChooser {
    private Reader reader;
    private View view;
    private InputValidator inputValidator;
    private ProductDao productDao;
    private Basket basket;
    private List<Product> products;

    public ProductChooser(Reader reader, View view, InputValidator inputValidator, ProductDao productDao) {
        this.reader = reader;
        this.view = view;
        this.inputValidator = inputValidator;
        this.productDao = productDao;
    }

    public ProductChooser(Reader reader, View view, InputValidator inputValidator, ProductDao productDao, Basket basket) {
        this(reader, view, inputValidator, productDao);
        this.basket = basket;
    }

    public void productController(String userType) {
        String unloggedMenu = "1. Back to main menu"
                                + "\n2. By category"
                                + "\n3. By name"
                                + "\n4. By price";

        String userMenu = "1. Back to main menu"
                                + "\n2. By category"
                                + "\n3. By name"
                                + "\n4. By price"
                                + "\n5. Add product to basket";

        switch (userType) {
            case "anonymous":
                view.displayMenu(unloggedMenu);
                break;
            case "customer":
                view.displayMenu(userMenu);
                break;
            default:
                view.displayError("No option available");
                break;
        }
        boolean backToMenu = false;
        final int START = 1;
        int end = userType.equals("anonymous") ?  4 :  5;
        while (!backToMenu) {
            view.displayQuestion("Choose option");
            int option = reader.getNumberInRange(START, end);
            switch (option) {
                case 1:
                    backToMenu = true;
                    break;
                case 2:
                    view.clearScreen();
                    TreeMap<String, Integer> categories = productDao.getCategories();
                    displayCategories(categories);
                    String category = reader.getCategoryFromUser(categories.keySet());
                    products = productDao.readProduct("categoryName", category);
                    displayProducts();
                    break;

            }
        }

    }

    private void displayCategories(TreeMap<String, Integer> categories){
        StringBuilder sb = new StringBuilder();
        for (String category : categories.keySet()) {
            sb.append(category);
            sb.append("\n");
        }
        view.displayTable(sb.toString());
    }

    private void displayProducts() {
        StringBuilder sb = new StringBuilder();
        for (Product product : products) {
            sb.append(product.toString());
            sb.append("\n");
        }
        view.displayTable(sb.toString());
    }
}
