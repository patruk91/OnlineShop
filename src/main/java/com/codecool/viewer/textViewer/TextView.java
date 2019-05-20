package com.codecool.viewer.textViewer;

import com.codecool.viewer.View;

public class TextView implements View {
    @Override
    public void displayMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void displayQuestion(String question) {
        System.out.println(question + "?: ");
    }

    @Override
    public void displayError(String error) {
        System.out.println("Error: " + error + "!");
    }

    @Override
    public void displayTable(String table) {
        System.out.println(table);
    }

    @Override
    public void displayMenu(String menu) {
        System.out.println(menu);
    }

    @Override
    public void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
