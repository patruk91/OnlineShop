package com.codecool.dao.SQL;

import com.codecool.dao.ProductDao;
import com.codecool.model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class ProductDaoSQL implements ProductDao {
    @Override
    public void createProduct(Product product) {
        Connection connection = DatabaseConnection.getConntectionToDatabase();

        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO products(name, quantity, price, status, categoryId) VALUES(?, ?, ?, ?. ?);");
            stmt.setString(1, product.getName());
            stmt.setInt(2, product.getAmount());
            stmt.setDouble(3, product.getPrice());
            stmt.setBoolean(4, product.isStatus());
            stmt.setInt(5, product.getCategoryId());
            stmt.executeUpdate();

            stmt.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<Product> readProduct(String data, String recordType) {
        Connection connection = DatabaseConnection.getConntectionToDatabase();
        List<Product> products = new ArrayList<>();

        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT * FROM products WHERE ?=?");
            stmt.setString(1,recordType);
            stmt.setString(2, data);

            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                int productId = resultSet.getInt("pid");
                String name = resultSet.getString("name");
                int quantity = resultSet.getInt("quantity");
                double price = resultSet.getDouble("price");
                boolean status = resultSet.getString("status").equals("active");
                int categoryId = resultSet.getInt("categoryId");

                Product product = new Product(productId, name, quantity, price, status, categoryId);
                products.add(product);
            }
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public void updateProduct(Product product, String column) {
        Connection connection = DatabaseConnection.getConntectionToDatabase();

        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "UPDATE products set ?=? where ID=?");
            stmt.setString(1, column);
            updateDataForProduct(product, column, stmt);
            stmt.setInt(3, product.getProductId());
            stmt.executeUpdate();

            stmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateDataForProduct(Product product, String column, PreparedStatement stmt) {
        try {
            switch (column) {
                case "name":
                    stmt.setString(2, product.getName());
                    break;
                case "quantity":
                    stmt.setInt(2, product.getAmount());
                    break;
                case "price":
                    stmt.setDouble(2, product.getPrice());
                    break;
                case "status":
                    stmt.setBoolean(2, product.isStatus());
                    break;
                case "categoryId":
                    stmt.setInt(2, product.getProductId());
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteProduct(Product product) {

    }

    @Override
    public TreeMap<Integer, String> getCategories() {
        return null;
    }
}
