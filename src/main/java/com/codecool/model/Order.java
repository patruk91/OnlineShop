package com.codecool.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order {
    private int userId;
    private Date date;
    private int orderId;
    private String orderStatus;
    private List<OrderDetail> orderDetails;

    public Order(int userId, int orderId, String orderStatus) {
        this.userId = userId;
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.orderDetails = new ArrayList<>();
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
}
