package com.codecool.controller.handler;

import com.codecool.dao.ProductDao;
import com.codecool.view.reader.Reader;
import com.codecool.view.viewer.View;
import jdk.jfr.Category;

import java.util.TreeMap;

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
                    removeCategory();
                    break;
                default:
                    view.displayMessage("No option available!");
            }
        }

    }

    private void removeCategory() {
        
    }

    private void editCategory() {
        TreeMap<String, Integer> categories = productDao.getCategories();
        view.displayCategories(categories);
        view.displayQuestion("Provide category name to edit: ");
        String categoryToEdit = reader.getNotEmptyString().toUpperCase();

        if (isCategoryExist(categories, categoryToEdit)) {
            view.displayQuestion("Provide  new category name: ");
            String newCategoryName = reader.getNotEmptyString().toUpperCase();
            productDao.updateCategory(newCategoryName, categories.get(categoryToEdit));
        } else {
            view.displayError("Category not exist");
        }

    }

    private boolean isCategoryExist(TreeMap<String, Integer> categories, String categoryToEdit) {
        for (String categoryName : categories.keySet()) {
            if (categoryName.equalsIgnoreCase(categoryToEdit)) {
                return true;
            }
        }
        return false;
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
