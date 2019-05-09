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

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void addOrderDetails(OrderDetail orderDetail) {
        orderDetails.add(orderDetail);
    }

    @Override
    public String toString() {
        double total = 0.0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < orderDetails.size(); i++) {
            OrderDetail orderDetail = orderDetails.get(i);
            sb.append(String.format("%d. Name: %s Quantity: %d Price: %f\n", i + 1, orderDetail.getProduct().getName(),
                    orderDetail.getQuantity(), orderDetail.getProduct().getPrice() * orderDetail.getQuantity()));
            total += orderDetail.getProduct().getPrice() * orderDetail.getQuantity();
        }
        sb.append("\n");
        sb.append(total);
        return sb.toString();
    }
}
