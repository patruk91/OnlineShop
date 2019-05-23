package com.codecool.controller.handler;

import com.codecool.dao.OrderDao;
import com.codecool.model.Basket;
import com.codecool.model.OrderDetail;
import com.codecool.model.Product;
import com.codecool.view.reader.Reader;
import com.codecool.view.viewer.View;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class BasketOperator {
    private Reader reader;
    private View viewer;
    private OrderDao orderDao;

    public BasketOperator(Reader reader, View viewer, OrderDao orderDao) {
        this.reader = reader;
        this.viewer = viewer;
        this.orderDao = orderDao;
    }

    public void controller(Basket basket) {
        final int BACK_TO_MAIN_MENU = 1;
        final int EDIT_QUANTITY = 2;
        final int REMOVE_PRODUCT = 3;
        final int CONFIRM_ORDER = 4;

        boolean backToMainMenu = false;
        while(!backToMainMenu) {
            viewer.clearScreen();
            displayMenu();
            displayBasket(basket);
            viewer.displayQuestion("Enter menu option");
            int option = reader.getNumberInRange(1, 4);

            switch (option) {
                case BACK_TO_MAIN_MENU:
                    backToMainMenu = true;
                    break;
                case EDIT_QUANTITY:
                    editQuantity(basket);
                    break;
                case REMOVE_PRODUCT:
                    removeProduct(basket);
                    break;
                case CONFIRM_ORDER:
                    confirmOrder(basket);
                    break;
            }
        }
    }

    private void displayMenu() {
        viewer.displayMenu("1. Back to main menu 2. Edit quantity 3. Remove product 4. Confirm order\n");
    }

    private void displayBasket(Basket basket) {
        TreeMap<String, Integer> temporary = new TreeMap<>();
        List<Product> list = new ArrayList<>();
        int totalPrice = 0;
        for(OrderDetail orderDetail : basket.getOrderDetails()){
            Product product = orderDetail.getProduct();
            temporary.put(product.getName(), product.getAmount());
            product.setAmount(orderDetail.getQuantity());
            list.add(product);
            totalPrice += orderDetail.getProduct().getPrice() * orderDetail.getQuantity();
        }
        viewer.displayProductsForUser(list);
        viewer.displayMessage("Total order price: " + totalPrice);

        for(OrderDetail orderDetail : basket.getOrderDetails()){
            Product product = orderDetail.getProduct();
            for (String key : temporary.keySet()) {
                if (key.equalsIgnoreCase(product.getName())) {
                    product.setAmount(temporary.get(key));
                }
            }
        }
    }

    private void editQuantity(Basket basket) {
        if(basket.getOrderDetails().size() > 0 ) {
            viewer.displayQuestion("Enter product number");
            int detailIndex = reader.getNumberInRange(1, basket.getOrderDetails().size()) - 1;
            viewer.displayQuestion("Enter new product quantity");
            int newQuantity = reader.getNumberInRange(1, basket.getOrderDetails().get(detailIndex).getProduct().getAmount());
            basket.getOrderDetails().get(detailIndex).setQuantity(newQuantity);
        }
    }

    private void removeProduct(Basket basket) {
        if(basket.getOrderDetails().size() > 0 ) {
            viewer.displayQuestion("Enter product number");
            int detailIndex = reader.getNumberInRange(1, basket.getOrderDetails().size()) - 1;
            basket.getOrderDetails().remove(detailIndex);
        }
    }

    private void confirmOrder(Basket basket) {
        if(basket.getOrderDetails().size() > 0 ) {
            orderDao.createOrder(basket);
            basket.getOrderDetails().clear();
        }
    }
}
