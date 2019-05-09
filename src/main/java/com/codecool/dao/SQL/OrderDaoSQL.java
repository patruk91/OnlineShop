package com.codecool.dao.SQL;

import com.codecool.dao.OrderDao;
import com.codecool.model.Basket;
import com.codecool.model.Order;
import com.codecool.model.OrderDetail;
import com.codecool.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDaoSQL implements OrderDao {
    @Override
    public void createOrder(Basket basket) {

        Connection connection = DatabaseConnection.getConntectionToDatabase();
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO orders (userId, statusName, date) values (?, ?, ?)");
            stmt.setInt(1, basket.getUserId());
            stmt.setString(2, "submit");
            stmt.setDate(3, new java.sql.Date(System.currentTimeMillis()));
            stmt.executeUpdate();

            ResultSet rs = stmt.executeQuery("SELECT id FROM users ORDER BY ID DESC limit 1");
            int orderId = 1;
            while(rs.next()) {
                orderId = rs.getInt("id");
            }

            for(OrderDetail detail: basket.getOrderDetails()) {
                stmt = connection.prepareStatement(
                        "INSERT INTO ordersDetails (productId, productQuantity, orderId) values (?, ?, ?)");
                stmt.setInt(1, detail.getProduct().getProductId());
                stmt.setInt(2, detail.getQuantity());
                stmt.setInt(3, orderId);
                stmt.executeUpdate();

                stmt = connection.prepareStatement(
                        "UPDATE products SET quantity = ? WHERE pid = ?");
                stmt.setInt(1, detail.getProduct().getProductId());
                stmt.setInt(2, detail.getProduct().getAmount() - detail.getQuantity());
                stmt.executeUpdate();
            }

            stmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Order> readOrder(int userId) {
        List<Order> userOrdersList = new ArrayList<>();

        Connection connection = DatabaseConnection.getConntectionToDatabase();
        try {
            PreparedStatement getUserOrders = connection.prepareStatement(
                    "SELECT oid FROM orders WHERE userId = ?");
            getUserOrders.setInt(1, userId);
            ResultSet userOrders = getUserOrders.executeQuery();
            while(userOrders.next()) {
                int oid = userOrders.getInt("id");
                Order order = new Order(oid);

                PreparedStatement stmt = connection.prepareStatement(
                        "SELECT orders.oid, orders.date, products.pid, products.name, products.quantity, products.price, " +
                                "products.status, products.categoryId, ordersDetails.productQuantity, FROM orders " +
                                "JOIN ordersDetails ON orders.oid = ordersDetails.orderId JOIN products ON " +
                                "ordersDetails.productId = products.pid WHERE userId = ? AND oid = ?");
                stmt.setInt(1, userId);
                stmt.setInt(2, oid);
                ResultSet orderDetails = stmt.executeQuery();
                while(orderDetails.next()) {
                    int pid = orderDetails.getInt("pid");
                    String name = orderDetails.getString("name");
                    int amount = orderDetails.getInt("quantity");
                    double price = orderDetails.getDouble("price");
                    boolean status = orderDetails.getBoolean("status");
                    int category = orderDetails.getInt("categoryId");

                    int orderQuantity = orderDetails.getInt("productQuantity");
                    Date date = orderDetails.getDate("date");
                    order.setDate(date);

                    Product product = new Product(pid, name, amount, price, status, category);
                    OrderDetail orderDetail = new OrderDetail(product, orderQuantity);
                    order.addOrderDetails(orderDetail);
                }
                userOrdersList.add(order);
                stmt.close();
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userOrdersList;
    }

    @Override
    public void updateOder(Order order) {

    }

    @Override
    public void deleteOrder(Order order) {
        Connection connection = DatabaseConnection.getConntectionToDatabase();
        try {
            PreparedStatement removeOrder = connection.prepareStatement("DELETE FROM orders WHERE oid = ?");
            removeOrder.setInt(1, order.getOrderId());

            removeOrder = connection.prepareStatement("DELETE FROM ordersDetails WHERE orderId = ?");
            removeOrder.setInt(1, order.getOrderId());

            removeOrder.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
