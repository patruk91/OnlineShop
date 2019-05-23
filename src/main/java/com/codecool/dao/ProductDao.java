package com.codecool.dao;

import com.codecool.model.Product;

import java.util.List;
import java.util.TreeMap;

public interface ProductDao {
    void createProduct(Product product);
    List<Product> readProduct(String name, String type, String userType);
    List<Product> readProduct(String userType);
    void updateProduct(Product product);
    void deleteProduct(Product product);
    TreeMap<String, Integer> getCategories();
    void createCategory(String category);
    void updateCategory(String category, int categoryId);
    void deleteCategory(int categoryId);
}
