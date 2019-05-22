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
        try (Connection connection = DatabaseConnection.getConntectionToDatabase();
             PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO products(name, quantity, price, status, categoryId) VALUES(?, ?, ?, ?, ?);")){
            insertProductData(stmt, product);
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }
    }

    private void insertProductData(PreparedStatement stmt, Product product) throws SQLException {
        stmt.setString(1, product.getName());
        stmt.setInt(2, product.getAmount());
        stmt.setDouble(3, product.getPrice());
        stmt.setBoolean(4, product.isStatus());
        stmt.setInt(5, product.getCategoryId());
        stmt.executeUpdate();
    }

    @Override
    public List<Product> readProduct(String column, String data, String userType) {
        String query = "";
        if (userType.equals("admin")) {
            query = "SELECT * FROM products JOIN categories ON categoryId = cid WHERE "
                    + column + " LIKE ?";
        } else {
            query = "SELECT * FROM products JOIN categories ON categoryId = cid WHERE "
                    + column + " LIKE ? AND status = 'active'";
        }

        List<Product> products = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConntectionToDatabase();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, "%" + data + "%");
            addProduct(stmt, products);
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }
        return products;
    }

    private void addProduct(PreparedStatement stmt, List<Product> products) throws SQLException {
        try (ResultSet resultSet = stmt.executeQuery()) {
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
        }

    }

    @Override
    public void updateProduct(Product product, String column) {
        try (Connection connection = DatabaseConnection.getConntectionToDatabase();
             PreparedStatement stmt = connection.prepareStatement(
                "UPDATE products set " +  column + " = ? WHERE id = ?")) {
            updateColumn(product, column, stmt);
            stmt.setInt(2, product.getProductId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }
    }

    private void updateColumn(Product product, String column, PreparedStatement stmt) throws SQLException {
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
    }

    @Override
    public void deleteProduct(Product product) {
        try (Connection connection = DatabaseConnection.getConntectionToDatabase();
             PreparedStatement removeOrder = connection.prepareStatement(
                     "DELETE FROM products WHERE pid = ?")) {
            removeOrder.setInt(1, product.getProductId());
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());
        }
    }

    @Override
    public TreeMap<String, Integer> getCategories() {
        TreeMap<String, Integer> categories = new TreeMap<>();
        try (Connection connection = DatabaseConnection.getConntectionToDatabase();
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM categories")) {
            addCategory(stmt, categories);
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage()
                    + "\nSQLState: " + e.getSQLState()
                    + "\nVendorError: " + e.getErrorCode());        }
        return categories;
    }

    private void addCategory(PreparedStatement stmt, TreeMap<String, Integer> categories) throws SQLException {
        try (ResultSet resultSet = stmt.executeQuery()) {
            while (resultSet.next()) {
                int categoryId = resultSet.getInt("cid");
                String name = resultSet.getString("categoryName");
                categories.put(name, categoryId);
            }
        }
    }
}
