package com.codecool.controller.handler;

import com.codecool.dao.ProductDao;
import com.codecool.model.Product;
import com.codecool.dao.OrderDao;
import com.codecool.model.Order;
import com.codecool.view.reader.Reader;
import com.codecool.view.viewer.View;

import java.util.List;
import java.util.TreeMap;


public class AdminTools {
    private View view;
    private Reader reader;
    private OrderDao orderDao;
    private ProductDao productDao;
    TreeMap<String, Integer> categories; ;

    public AdminTools(View view, Reader reader, ProductDao productDao, OrderDao orderDao) {
        this.view = view;
        this.reader = reader;
        this.productDao = productDao;
        this.orderDao = orderDao;
    }

    public void adminController() {
        displayAdminMenu();
    }

    private void displayAdminMenu() {
        final int START = 1;
        final int END = 3;
        
        boolean backToMenu = false;
        while (!backToMenu) {
            view.clearScreen();
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
                    manageOrders();
                    break;
                default:
                    view.displayMessage("No option available!");
            }
        }
    }

    private void manageOrders() {
        List<Order> listOfOrders = orderDao.readOrder();
        if(listOfOrders.size() != 0) {
            view.displayOrders(listOfOrders);
            view.displayQuestion("Enter order id to change status");
            int orderId = reader.getNumberInRange(1, listOfOrders.size());
            Order order = listOfOrders.get(orderId - 1);
            view.displayQuestion("Enter new order status");
            order.setOrderStatus(reader.getNotEmptyString());
            orderDao.updateOder(order);
        } else {
            view.displayMessage("No orders to display");
            reader.promptEnterKey();
        }

    }

    private void manageCategories() {
        boolean backToAdminMenu = false;
        final int START = 1;
        final int END = 4;
        while (!backToAdminMenu) {
            view.clearScreen();
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

        if (isCategoryExist(categoryToRemove) && !isCategoryUsed(categoryToRemove, products)) {
            productDao.deleteCategory(categories.get(categoryToRemove));
        } else {
            view.displayError("Category in use on no category by that name");
            reader.promptEnterKey();
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
            reader.promptEnterKey();
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
            reader.promptEnterKey();
        }
    }
}
