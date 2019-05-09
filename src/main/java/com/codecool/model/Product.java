package com.codecool.model;

import java.util.Objects;

public class Product {
    private int productId;
    private String name;
    private int amount;
    private double price;
    private boolean status;
    private int categoryId;

    public Product(int productId, String name, int amount, double price, boolean status, int categoryId) {
        this.productId = productId;
        this.name = name;
        this.amount = amount;
        this.price = price;
        this.status = status;
        this.categoryId = categoryId;
    }

    public int getProductId() {
        return productId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public double getPrice() {
        return price;
    }

    public boolean isStatus() {
        return status;
    }

    public int getCategoryId() {
        return categoryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Product product = (Product) o;
        return productId == product.productId && name.equals(product.name);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result * productId + name.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "name: " + name
                + ", amount: " + amount
                + ", price: " + price
                + ", status: " + status
                + ", categoryId: " + categoryId;
    }
}
