package com.codecool.view.viewer;

import com.codecool.model.Product;
import com.codecool.model.Order;

import java.util.List;

public interface View {
    void displayMessage(String message);
    void displayQuestion(String question);
    void displayError(String error);

    void displayProductsForUser(List<Product> list);
    void displayProductsForAdmin(List<Product> list);
    void displayCategories(String[] headers, String[][] table);
    void displayOrders(List<Order> list);

    void displayMenu(String menu);
    void clearScreen();
}
