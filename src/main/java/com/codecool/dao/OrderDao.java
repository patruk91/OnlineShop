package com.codecool.dao;

import com.codecool.model.Basket;
import com.codecool.model.Order;

import java.util.List;

public interface OrderDao {
    void createOrder(Basket basket);
    List<Order> readOrder(int userId);
    void updateOder(Order order);
    void deleteOrder(Order order);
}
