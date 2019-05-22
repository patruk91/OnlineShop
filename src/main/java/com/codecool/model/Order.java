package com.codecool.model;

import java.util.Date;

public class Order extends Basket {
    private Date date;
    private int orderId;
    private String orderStatus;

    public Order(int userId, int orderId, String orderStatus) {
        super(userId);
        this.orderId = orderId;
        this.orderStatus = orderStatus;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getOrderStatus(){ return orderStatus; }

    public void setOrderStatus(String orderStatus){ this.orderStatus = orderStatus; }

    @Override
    public String toString() {
        return "date: " + date + ", " + super.toString();
    }
}
