package com.codecool.model;

import java.util.Date;

public class Order extends Basket {
    private Date date;
    private int orderId;

    public Order(int userId) {
        super(userId);
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

    @Override
    public String toString() {
        return super.toString() + ", date: " +date;
    }
}
