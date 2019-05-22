package com.codecool.model;

import java.util.ArrayList;
import java.util.List;

public class Basket {
    private int userId;
    private List<OrderDetail> orderDetails;

    public Basket(int userId) {
        this.userId = userId;
        this.orderDetails = new ArrayList<>();
    }

    public int getUserId() {
        return userId;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void addOrderDetails(OrderDetail orderDetail) {
        orderDetails.add(orderDetail);
    }
}
