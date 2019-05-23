package com.codecool.controller.handler;

import com.codecool.dao.ProductDao;
import com.codecool.model.Product;
import com.codecool.view.reader.Reader;
import com.codecool.view.viewer.View;
import jdk.jfr.Category;

import java.util.List;
import java.util.TreeMap;

public class AdminTools {
    private View view;
    private Reader reader;
    private ProductDao productDao;
    TreeMap<String, Integer> categories; ;

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
        String categoryToRemove = getCategoryName();
        List<Product> products = productDao.readProduct("admin");

        if (!isCategoryUsed(categoryToRemove, products)) {
            productDao.deleteCategory(categories.get(categoryToRemove));
        } else {
            view.displayError("Category in use");
        }
    }

    private boolean isCategoryUsed(String categoryToRemove, List<Product> products) {
        for (Product product : products) {
            if (product.getCategoryId() == categories.get(categoryToRemove)) {
                return true;
            }
        }
        return false;
    }

    private void editCategory() {
        String categoryToEdit = getCategoryName();

        if (isCategoryExist(categoryToEdit)) {
            view.displayQuestion("Provide  new category name: ");
            String newCategoryName = reader.getNotEmptyString().toUpperCase();
            productDao.updateCategory(newCategoryName, categories.get(categoryToEdit));
        } else {
            view.displayError("Category not exist");
        }

    }

    private String getCategoryName() {
        categories = productDao.getCategories();
        view.displayCategories(categories);
        view.displayQuestion("Provide category name: ");
        return reader.getNotEmptyString().toUpperCase();
    }

    private boolean isCategoryExist(String categoryToEdit) {
        categories = productDao.getCategories();
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
        if(!isCategoryExist(category)) {
            productDao.createCategory(category);
        } else {
            view.displayError("Category already exists!");
        }
    }
}
