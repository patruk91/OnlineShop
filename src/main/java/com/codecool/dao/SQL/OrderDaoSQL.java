package com.codecool.dao.SQL;

import com.codecool.dao.OrderDao;
import com.codecool.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDaoSQL implements OrderDao {
    @Override
    public void createOrder(Basket basket) {

        try (Connection connection = DatabaseConnection.getConntectionToDatabase()) {
            insertOrderBase(basket, connection);

            int orderId = getLastOrderId(connection);

            for(OrderDetail detail: basket.getOrderDetails()) {
                insertOrderDetails(connection, orderId, detail);
                updateProductQuantity(connection, detail);
            }

        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }
    }

    private void insertOrderBase(Basket basket, Connection connection) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO orders (userId, statusName, date) values (?, ?, ?)")) {
            stmt.setInt(1, basket.getUserId());
            stmt.setString(2, "submit");
            stmt.setDate(3, new Date(System.currentTimeMillis()));
            stmt.executeUpdate();
        }
    }

    private int getLastOrderId(Connection connection) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement("SELECT oid FROM orders ORDER BY oid DESC limit 1")) {
            try (ResultSet rs = stmt.executeQuery()) {
                int orderId = 1;
                while (rs.next()) {
                    orderId = rs.getInt("oid");
                }

                return orderId;
            }
        }
    }

    private void insertOrderDetails(Connection connection, int orderId, OrderDetail detail) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO ordersDetails (productId, productQuantity, orderId) values (?, ?, ?)")) {
            stmt.setInt(1, detail.getProduct().getProductId());
            stmt.setInt(2, detail.getQuantity());
            stmt.setInt(3, orderId);
            stmt.executeUpdate();
        }
    }

    private void updateProductQuantity(Connection connection, OrderDetail detail) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(
                "UPDATE products SET quantity = ? WHERE pid = ?")) {
            stmt.setInt(1, detail.getProduct().getAmount() - detail.getQuantity());
            stmt.setInt(2, detail.getProduct().getProductId());
            stmt.executeUpdate();
        }
    }

    @Override
    public List<Order> readOrder(int userId) {
        List<Order> userOrdersList = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConntectionToDatabase();
             PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM orders WHERE userId = ?")) {
            stmt.setInt(1, userId);
            try (ResultSet userOrders = stmt.executeQuery()) {
                while (userOrders.next()) {
                    int oid = userOrders.getInt("oid");
                    String orderStatus = userOrders.getString("statusName");
                    Order order = new Order(userId, oid, orderStatus);
                    userOrdersList.add(order);

                    try (PreparedStatement stmt1 = connection.prepareStatement(
                            "SELECT orders.oid, orders.date, products.pid, products.name, products.quantity, products.price, " +
                                    "products.status, products.categoryId, ordersDetails.productQuantity FROM orders " +
                                    "JOIN ordersDetails ON orders.oid = ordersDetails.orderId JOIN products ON " +
                                    "ordersDetails.productId = products.pid WHERE userId = ? AND oid = ?")) {
                        stmt1.setInt(1, userId);
                        stmt1.setInt(2, oid);

                        try (ResultSet orderDetails = stmt1.executeQuery()) {
                            while (orderDetails.next()) {
                                addOrderDetails(orderDetails, order);
                            }
                        }
                    }

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userOrdersList;
    }

    @Override
    public List<Order> readOrder() {
        List<Order> userOrdersList = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConntectionToDatabase();
             PreparedStatement stmt = connection.prepareStatement(
                     "SELECT * FROM orders")) {
            try (ResultSet userOrders = stmt.executeQuery()) {
                while (userOrders.next()) {
                    int oid = userOrders.getInt("oid");
                    int userId = userOrders.getInt("userId");
                    String orderStatus = userOrders.getString("statusName");

                    Order order = new Order(userId, oid, orderStatus);

                    Long date = userOrders.getLong("date");
                    order.setDate(new java.util.Date(date));

                    userOrdersList.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userOrdersList;
    }


    private void addOrderDetails(ResultSet rs, Order order) throws SQLException {
        int pid = rs.getInt("pid");
        String name = rs.getString("name");
        int amount = rs.getInt("quantity");
        double price = rs.getDouble("price");
        boolean status = rs.getBoolean("status");
        int category = rs.getInt("categoryId");

        int orderQuantity = rs.getInt("productQuantity");
        Long date = rs.getLong("date");

        order.setDate(new java.util.Date(date));

        Product product = new Product(pid, name, amount, price, status, category);
        OrderDetail orderDetail = new OrderDetail(product, orderQuantity);
        order.addOrderDetails(orderDetail);
    }

    @Override
    public void updateOder(Order order) {
        try (Connection connection = DatabaseConnection.getConntectionToDatabase();
             PreparedStatement stmt = connection.prepareStatement("UPDATE orders SET statusName = ? WHERE oid = ?")){
            stmt.setString(1, order.getOrderStatus());
            stmt.setInt(2, order.getOrderId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }
    }

    @Override
    public void deleteOrder(Order order) {
        try (Connection connection = DatabaseConnection.getConntectionToDatabase()){
            removeOrderBase(order, connection);
            removeOrderDetails(order, connection);
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }
    }

    private void removeOrderBase(Order order, Connection connection) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM orders WHERE oid = ?")) {
            stmt.setInt(1, order.getOrderId());
            stmt.executeUpdate();
        }
    }

    private void removeOrderDetails(Order order, Connection connection) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM ordersDetails WHERE orderId = ?")) {
            stmt.setInt(1, order.getOrderId());
            stmt.executeUpdate();
        }
    }
}
