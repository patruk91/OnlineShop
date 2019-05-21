package com.codecool.view.viewer;

import com.codecool.model.Product;

import java.util.ArrayList;

public interface View {
    void displayMessage(String message);
    void displayQuestion(String question);
    void displayError(String error);

    void displayProductsForUser(ArrayList<Product> list);
    void displayProductsForAdmin(ArrayList<Product> list);

    void displayMenu(String menu);
    void clearScreen();
}
