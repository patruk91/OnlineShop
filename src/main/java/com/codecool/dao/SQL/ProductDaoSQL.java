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
    public List<Product> readProduct(String column, String data) {
        Connection connection = DatabaseConnection.getConntectionToDatabase();
        List<Product> products = new ArrayList<>();

        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT * FROM products JOIN categories ON categoryId = cid WHERE " + column +" LIKE ?");
            stmt.setString(1, "%" + data + "%");


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
                    "UPDATE products set " +  column + " = ? WHERE id = ?");
            updateDataForProduct(product, column, stmt);
            stmt.setInt(2, product.getProductId());
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
                    stmt.setString(1, product.getName());
                    break;
                case "quantity":
                    stmt.setInt(1, product.getAmount());
                    break;
                case "price":
                    stmt.setDouble(1, product.getPrice());
                    break;
                case "status":
                    stmt.setBoolean(1, product.isStatus());
                    break;
                case "categoryId":
                    stmt.setInt(1, product.getProductId());
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteProduct(Product product) {
        Connection connection = DatabaseConnection.getConntectionToDatabase();
        try {
            PreparedStatement removeOrder = connection.prepareStatement("DELETE FROM products WHERE pid = ?");
            removeOrder.setInt(1, product.getProductId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public TreeMap<String, Integer> getCategories() {
        Connection connection = DatabaseConnection.getConntectionToDatabase();
        TreeMap<String, Integer> categories = new TreeMap<>();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM categories");
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                int categoryId = resultSet.getInt("cid");
                String name = resultSet.getString("categoryName");
                categories.put(name, categoryId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }
}
