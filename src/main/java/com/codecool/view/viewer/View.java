package com.codecool.view.viewer;

public interface View {
    void displayMessage(String message);
    void displayQuestion(String question);
    void displayError(String error);
    void displayTable(String table);
    void displayMenu(String menu);
    void clearScreen();
}