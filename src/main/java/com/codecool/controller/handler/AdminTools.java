package com.codecool.controller.handler;

import com.codecool.dao.ProductDao;
import com.codecool.view.reader.Reader;
import com.codecool.view.viewer.View;

public class AdminTools {
    private View view;
    private Reader reader;
    private ProductDao productDao;

    public AdminTools(View view, Reader reader, ProductDao productDao) {
        this.view = view;
        this.reader = reader;
        this.productDao = productDao;
    }

    public void adminController() {
        displayAdminMenu();
    }

    private void displayAdminMenu() {
        boolean backToMenu = false;
        final int START = 1;
        final int END = 3;
        while (!backToMenu) {
            view.displayMenu("1. Back to main menu 2. Manage categories 3. Manage orders\n");
            view.displayQuestion("Choose menu option");
            int option = reader.getNumberInRange(START, END);
            switch (option) {
                case 1:
                    backToMenu = true;
                    break;
                case 2:
                    manageCategories();
                    break;
                case 3:
//                    manageOrders();
                    break;
                default:
                    view.displayMessage("No option available!");
            }
        }

    }

    private void manageCategories() {
        boolean backToAdminMenu = false;
        final int START = 1;
        final int END = 4;
        while (!backToAdminMenu) {
            view.displayMenu("1. Back to main menu 2. Add category 3. Edit category 4. Remove category\n");
            view.displayQuestion("Choose menu option");
            int option = reader.getNumberInRange(START, END);
            switch (option) {
                case 1:
                    backToAdminMenu = true;
                    break;
                case 2:
                    addCategory();
                    break;
                case 3:
                    editCategory();
                    break;
                case 4:
//                    removeCategory();
                    break;
                default:
                    view.displayMessage("No option available!");
            }
        }

    }

    private void editCategory() {
        view.displayCategories(productDao.getCategories());

    }

    private void addCategory() {
        view.displayMessage("New category: ");
        String category = reader.getNotEmptyString().toUpperCase();
        for (String categoryName : productDao.getCategories().keySet()) {
            if (!categoryName.equalsIgnoreCase(category)) {
                productDao.createCategory(category);
            } else {
                view.displayError("Category already exists!");
            }
        }

    }
}
