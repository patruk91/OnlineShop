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

    public void setUserId(int userid) {
        this.userId = userid;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if(orderDetails.size() > 0) {
            double total = 0.0;
            for (int i = 0; i < orderDetails.size(); i++) {
                OrderDetail orderDetail = orderDetails.get(i);
                sb.append(String.format("%d. Name: %s Quantity: %d Price: %f\n", i + 1, orderDetail.getProduct().getName(),
                        orderDetail.getQuantity(), orderDetail.getProduct().getPrice() * orderDetail.getQuantity()));
                total += orderDetail.getProduct().getPrice() * orderDetail.getQuantity();
            }
            sb.append("\n");
            sb.append(total);
        } else {
            sb.append("Basket is empty.");
        }
        return sb.toString();
    }
}
