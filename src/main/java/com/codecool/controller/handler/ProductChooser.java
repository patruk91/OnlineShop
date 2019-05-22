package com.codecool.controller.handler;

import com.codecool.dao.ProductDao;
import com.codecool.model.Basket;
import com.codecool.model.OrderDetail;
import com.codecool.model.Product;
import com.codecool.view.reader.Reader;
import com.codecool.view.viewer.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ProductChooser {
    private Reader reader;
    private View view;
    private ProductDao productDao;
    private Basket basket;
    private List<Product> products;

    public ProductChooser(Reader reader, View view, ProductDao productDao) {
        this.reader = reader;
        this.view = view;
        this.productDao = productDao;
        products = productDao.readProduct("status", "active", "customer");
    }

    public ProductChooser(Reader reader, View view, ProductDao productDao, Basket basket) {
        this(reader, view, productDao);
        this.basket = basket;
    }

    public void productController(String userType) {
        boolean backToMenu = false;
        final int START = 1;
        int end = userType.equals("anonymous") ?  3 : 4;
        while (!backToMenu) {
            displayMenu(userType);
            view.displayQuestion("Choose menu option");
            int option = reader.getNumberInRange(START, end);
            switch (option) {
                case 1:
                    backToMenu = true;
                    break;
                case 2:
                    displayProductsByCategory(userType);
                    break;
                case 3:
                    displayProductsByName(userType);
                    break;
                case 4:
                    addProductToBasket(userType);
                    break;
            }
        }
    }

    private void displayMenu(String userType) {
        String unloggedMenu = "1. Back to main menu 2. By category 3. By name";
        String userMenu = "1. Back to main menu 2. By category 3. By name 4. Add product to basket";

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
    }

    private void displayProductsByCategory(String userType) {
        view.clearScreen();
        TreeMap<String, Integer> categories = productDao.getCategories();
        displayCategories(categories);
        String category = reader.getCategoryFromUser(categories.keySet());
        products = productDao.readProduct("categoryName", category, userType);
        displayProducts(userType);
    }

    private void displayProductsByName(String userType) {
        view.clearScreen();
        view.displayMessage("Enter product name: ");
        String productName = reader.getNotEmptyString();
        products = productDao.readProduct("name", productName, userType);
        displayProducts(userType);
    }

    private void addProductToBasket(String userType) {
        if (userType.equals("customer")) {
            view.displayMessage("Enter product name: ");
            String productNameInBasket = reader.getNotEmptyString();
            ifProductOnList(productNameInBasket);
        }
    }

    private void ifProductOnList(String productNameInBasket) {
        if (productOnList(productNameInBasket)) {
            int quantity = getQuantity(productNameInBasket);
            ifProductInBasket(productNameInBasket, quantity);
            view.clearScreen();
        } else {
            view.displayMessage("No product available by that name!");
        }
    }

    private int getQuantity(String productNameInBasket) {
        view.displayMessage("Enter product amount: ");
        return reader.getNumberInRange(1, getProductByName(productNameInBasket).getAmount());
    }

    private void ifProductInBasket(String productNameInBasket, int quantity) {
        if (basket.getOrderDetails().size() > 0 && checkIfProductIsInBasket(productNameInBasket)) {
            addProductIfEnoughAmountInStock(productNameInBasket, quantity);
        } else {
            basket.addOrderDetails(new OrderDetail(getProductByName(productNameInBasket), quantity));
        }
    }

    private void addProductIfEnoughAmountInStock(String productNameInBasket, int quantity) {
        if (isExceedStock(productNameInBasket, quantity)) {
            updateProductInBasket(productNameInBasket, quantity);
        } else {
            view.displayMessage("Not enough amount of product in our shop!");
        }
    }

    private boolean isExceedStock(String productNameInBasket, int quantity) {
        OrderDetail userOrder = getOrderDetailFromBasket(productNameInBasket);
        int maxAmount = userOrder.getProduct().getAmount();
        return quantity + userOrder.getQuantity() < maxAmount;
    }

    private OrderDetail getOrderDetailFromBasket(String productNameInBasket) throws IllegalArgumentException {
        for (OrderDetail orderDetail : basket.getOrderDetails()) {
            if (orderDetail.getProduct().getName().equalsIgnoreCase(productNameInBasket)) {
                return orderDetail;
            }
        }
        throw new IllegalArgumentException("Order detail doesn't exist in basket!");
    }

    private boolean checkIfProductIsInBasket(String productNameBasket) {
        for (int i = 0; i < basket.getOrderDetails().size(); i++) {
            if (basket.getOrderDetails().get(i).getProduct().equals(getProductByName(productNameBasket))) {
                return true;
            }
        }
        return false;
    }

    private void updateProductInBasket(String productNameBasket, int quantity) {
        for (int i = 0; i < basket.getOrderDetails().size(); i++) {
            if (basket.getOrderDetails().get(i).getProduct().equals(getProductByName(productNameBasket))) {
                basket.getOrderDetails().get(i).updateQuantity(quantity);
            }
        }
    }

    private boolean productOnList(String name) {
        for (Product product : products) {
            if (name.toLowerCase().equals(product.getName().toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private Product getProductByName(String name) throws IllegalArgumentException {
        for (Product product : products) {
            if (product.getName().toLowerCase().equals(name.toLowerCase())) {
                return product;
            }
        }
        throw new IllegalArgumentException("No product by that name!");
    }

    private void displayCategories(TreeMap<String, Integer> categories){
        String[] headers = {"ID", "Name"};
        String[][] table = new String[categories.keySet().size()][2];
        int id1 = 0;
        int id2 = 0;
        for (Map.Entry<String, Integer> entry : categories.entrySet()) {
            table[id1][id2] = Integer.toString(entry.getValue());
            table[id1][id2 + 1] = entry.getKey();
            id1++;
        }
        view.displayCategories(headers, table);
    }

    private void displayProducts(String userType) {
        if(userType == "admin"){
            view.displayProductsForAdmin(products);
        }else{
            view.displayProductsForUser(products);
        }

    }
}

