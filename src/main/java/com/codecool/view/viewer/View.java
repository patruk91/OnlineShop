package com.codecool.view.viewer;

import com.codecool.model.Product;

import java.util.ArrayList;
import java.util.List;

public interface View {
    void displayMessage(String message);
    void displayQuestion(String question);
    void displayError(String error);

    void displayProductsForUser(List<Product> list);
    void displayProductsForAdmin(List<Product> list);
    void displayCategories(String[] headers, String[][] table);

    void displayMenu(String menu);
    void clearScreen();
}
