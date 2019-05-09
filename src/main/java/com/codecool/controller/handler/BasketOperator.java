package com.codecool.controller.handler;

import com.codecool.dao.OrderDao;
import com.codecool.model.Basket;
import com.codecool.reader.Reader;
import com.codecool.viewer.View;

public class BasketOperator {
    Reader reader;
    View viewer;
    OrderDao orderDao;

    public BasketOperator(Reader reader, View viewer, OrderDao orderDao) {
        this.reader = reader;
        this.viewer = viewer;
        this.orderDao = orderDao;
    }

    public void controller(Basket basket) {
        final int BACKTOMAINMENU = 1;
        final int EDITQUANTITY = 2;
        final int REMOVEPRODUCT = 3;
        final int CONFIRMORDER = 4;

        boolean backToMainMenu = false;
        while(!backToMainMenu) {
            viewer.clearScreen();
            displayMenu();
            displayBasket(basket);
            viewer.displayQuestion("Enter menu option");
            int option = reader.getNumberInRange(1, 3);

            switch (option) {
                case BACKTOMAINMENU:
                    backToMainMenu = true;
                    break;
                case EDITQUANTITY:
                    editQuantity(basket);
                    break;
                case REMOVEPRODUCT:
                    removeProduct(basket);
                    break;
                case CONFIRMORDER:
                    confirmOrder(basket);
                    break;
            }
        }
    }

    private void displayMenu() {
        viewer.displayMenu("1. Back to main menu 2. Edit quantity 3. Remove product 4. Confirm order\n");
    }

    private void displayBasket(Basket basket) {
        viewer.displayTable(basket.toString());
    }

    private void editQuantity(Basket basket) {
        if(basket.getOrderDetails().size() > 0 ) {
            int detailIndex = reader.getNumberInRange(1, basket.getOrderDetails().size()) - 1;
            int newQuantity = reader.getNumberInRange(1, basket.getOrderDetails().get(detailIndex).getProduct().getAmount());
            basket.getOrderDetails().get(detailIndex).setQuantity(newQuantity);
        }
    }

    public void removeProduct(Basket basket) {
        if(basket.getOrderDetails().size() > 0 ) {
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
