package com.codecool.controller.handler;

import com.codecool.dao.ProductDao;
import com.codecool.model.Basket;
import com.codecool.model.OrderDetail;
import com.codecool.model.Product;
import com.codecool.reader.Reader;
import com.codecool.viewer.View;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class ProductChooser {
    private Reader reader;
    private View view;
    private ProductDao productDao;
    private Basket basket;
    private List<Product> products = new ArrayList<>();

    public ProductChooser(Reader reader, View view, ProductDao productDao) {
        this.reader = reader;
        this.view = view;
        this.productDao = productDao;
    }

    public ProductChooser(Reader reader, View view, ProductDao productDao, Basket basket) {
        this(reader, view, productDao);
        this.basket = basket;
    }

    public void productController(String userType) {
        boolean backToMenu = false;
        final int START = 1;
        int end = userType.equals("anonymous") ?  3 :  4;
        while (!backToMenu) {
            displayMenu(userType);
            view.displayQuestion("Choose menu option");
            int option = reader.getNumberInRange(START, end);
            switch (option) {
                case 1:
                    backToMenu = true;
                    break;
                case 2:
                    displayProductsByCategory();
                    break;
                case 3:
                    displayProductsByName();
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

    private void displayProductsByCategory() {
        view.clearScreen();
        TreeMap<String, Integer> categories = productDao.getCategories();
        displayCategories(categories);
        String category = reader.getCategoryFromUser(categories.keySet());
        products = productDao.readProduct("categoryName", category);
        displayProducts();
    }

    private void displayProductsByName() {
        view.clearScreen();
        String productName = reader.getStringFromUser("Enter product name");
        products = productDao.readProduct("name", productName);
        displayProducts();
    }

    private void addProductToBasket(String userType) {
        if (userType.equals("customer") && products.size() > 0) {
            String productNameBasket = reader.getStringFromUser("Enter product name");
            if (productOnList(productNameBasket)) {
                view.displayMessage("Enter product amount: ");
                int quantity = reader.getNumberInRange(1, getProductByName(productNameBasket).getAmount());
                if (basket.getOrderDetails().size() > 0 && checkIfProductIsInBasket(productNameBasket)) {
                    updateProductInBasket(productNameBasket, quantity);
                } else {
                    basket.addOrderDetails(new OrderDetail(getProductByName(productNameBasket), quantity));
                }

                view.clearScreen();
            } else {
                view.displayMessage("No product available by that name!");
            }
        } else {
            view.displayMessage("No product on list!");
        }
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
        view.clearScreen();
        view.displayTable(sb.toString());
    }

    private boolean productOnList(String name) {
        for (Product product : products) {
            if (name.equals(product.getName())) {
                return true;
            }
        }
        return false;
    }

    private Product getProductByName(String name) {
        for (Product product : products) {
            if (product.getName().toLowerCase().equals(name.toLowerCase())) {
                return product;
            }
        }
        return new Product(0,"",0,0,false,0);
    }
}
