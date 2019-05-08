package com.codecool.dao;

import com.codecool.model.Product;

import java.util.List;
import java.util.TreeMap;

public interface ProductDao {
    void createProduct(Product product);
    List<Product> readProduct(String name, String type);
    void updateProduct(Product product, String column);
    void deleteProduct(Product product);
    TreeMap<Integer, String> getCategories();
}
