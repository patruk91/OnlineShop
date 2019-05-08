package com.codecool.model;

public class Product {
    private int productId;
    private String name;
    private double price;
    private int amount;

    public Product(int productId, String name, double price, int amount) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "productId: " + productId
                + ", name: " + name
                + ", price: " + price
                + ", amount: " + amount;
    }
}
