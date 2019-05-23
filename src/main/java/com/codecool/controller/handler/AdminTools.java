package com.codecool.controller.handler;

import com.codecool.dao.OrderDao;
import com.codecool.model.Order;
import com.codecool.view.reader.Reader;
import com.codecool.view.viewer.View;

import java.util.List;

public class AdminTools {
    private View view;
    private Reader reader;
    private OrderDao orderDao;

    public AdminTools(View view, Reader reader, OrderDao orderDao) {
        this.view = view;
        this.reader = reader;
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
            view.displayMenu("1. Back to main menu 2. Manage categories 3. Manage orders\n");
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
        } else {
            view.displayMessage("No orders to display");
        }
        view.displayQuestion("Enter order id to change status");
        int orderId = reader.getNumberInRange(1, listOfOrders.size());
        Order order = listOfOrders.get(orderId - 1);
        view.displayQuestion("Enter new order status");
        order.setOrderStatus(reader.getNotEmptyString());
        orderDao.updateOder(order);
    }
}
