package com.codecool.dao.SQL;

import com.codecool.dao.ProductDao;
import com.codecool.model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    public List<Product> readProduct(String name, String type) {
        return null;
    }

    @Override
    public void updateProduct(Product product) {

    }

    @Override
    public void deleteProduct(Product product) {

    }

    @Override
    public TreeMap<Integer, String> getCategories() {
        return null;
    }
}
