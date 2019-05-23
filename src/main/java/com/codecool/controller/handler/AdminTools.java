package com.codecool.controller.handler;

import com.codecool.view.reader.Reader;
import com.codecool.view.viewer.View;

public class AdminTools {
    private View view;
    private Reader reader;

    public AdminTools(View view, Reader reader) {
        this.view = view;
        this.reader = reader;
    }

    public void adminController() {
        displayAdminMenu();
    }

    private void displayAdminMenu() {
        view.displayMenu("1. Back to main menu 2. Manage categories 3. Manage orders\n");
        boolean backToMenu = false;
        final int START = 1;
        final int END = 3;
        while (!backToMenu) {
            view.displayQuestion("Choose menu option");
            int option = reader.getNumberInRange(START, END);
            switch (option) {
                case 1:
                    backToMenu = true;
                    break;
                case 2:
//                    manageCategories();
                    break;
                case 3:
//                    manageOrders();
                    break;
                default:
                    view.displayMessage("No option available!");
            }
        }

    }
}
