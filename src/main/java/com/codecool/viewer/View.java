package com.codecool.viewer;

public interface View {
    void displayMessage(String message);
    void displayQuestion(String question);
    void displayError(String error);
    void displayTable(String table);
    void displayMenu(String menu);
    void clearScrean();
}
