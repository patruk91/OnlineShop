package com.codecool.controller.handler;

import com.codecool.dao.ProductDao;
import com.codecool.model.Basket;
import com.codecool.model.OrderDetail;
import com.codecool.model.Product;
import com.codecool.view.reader.Reader;
import com.codecool.view.viewer.View;

import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

public class ProductChooser {
    private Reader reader;
    private View view;
    private ProductDao productDao;
    private Basket basket;
    private List<Product> products;

    public ProductChooser(Reader reader, View view, ProductDao productDao) {
        this.reader = reader;
        this.view = view;
        this.productDao = productDao;
        products = productDao.readProduct("status", "active", "customer");

    }

    public ProductChooser(Reader reader, View view, ProductDao productDao, Basket basket) {
        this(reader, view, productDao);
        this.basket = basket;
    }

    public void productController(String userType) {
        boolean backToMenu = false;
        final int START = 1;
        int end = userType.equals("anonymous") ?  3 : 4;
        while (!backToMenu) {
            displayMenu(userType);
            view.displayQuestion("Choose menu option");
            int option = reader.getNumberInRange(START, end);
            switch (option) {
                case 1:
                    backToMenu = true;
                    break;
                case 2:
                    displayProductsByCategory(userType);
                    break;
                case 3:
                    displayProductsByName(userType);
                    break;
                case 4:
                    if (userType.equals("admin")) {
                        editProducts();
                        break;
                    } else {
                        addProductToBasket(userType);
                        break;
                    }
                default:
                    view.displayMessage("No option available!");
            }
        }
    }

    private void editProducts() {
        products = productDao.readProduct("admin");
        displayEditMenu();
    }

    private void displayEditMenu() {
        String editMenu = "1.Add product 2. Edit product 3.Remove product";
        view.displayMenu(editMenu);
        final int START = 1;
        final int END = 3;


        view.displayQuestion("Choose menu option");
        int option = reader.getNumberInRange(START, END);
        switch (option) {
            case 1:
                addProduct();
                break;
            case 2:
                editProduct();
                break;
            case 3:
                removeProduct();
                break;
            default:
                view.displayMessage("No option available!");
        }
    }

    private void removeProduct() {
        products = productDao.readProduct("admin");
        view.displayQuestion("Choose product to remove");
        String product = reader.getNotEmptyString();
        if (isProductOnList(product)) {
            productDao.deleteProduct(getProductByName(product));
        } else {
            view.displayError("No product by that name in database!");
        }

    }

    private void addProduct() {
        String productName = getProductName();
        int quantity = getQuantity();
        double price = getPrice();
        boolean orderStatus = getOrderStatus();
        int categoryId = getCategoryId();
        Product product = new Product(productName, quantity, price, orderStatus, categoryId);
        productDao.createProduct(product);
    }

    private int getCategoryId() {
        view.displayMessage("Category: ");
        String category = reader.getNotEmptyString().toUpperCase();
        return findCategoryId(category);
    }

    private boolean getOrderStatus() {
        view.displayMessage("Order status: ");
        return reader.getNotEmptyString().equalsIgnoreCase("active");
    }

    private double getPrice() {
        view.displayMessage("Enter price: ");
        return reader.getNumberInRange(0, Double.MAX_VALUE);
    }

    private int getQuantity() {
        view.displayMessage("Enter quantity: ");
        return reader.getNumberInRange(0, Integer.MAX_VALUE);
    }

    private String getProductName() {
        view.displayMessage("Enter product name: ");
        return reader.getNotEmptyString();
    }

    private int findCategoryId(String category) {
        int number = 0;
        TreeMap<String, Integer> categories = productDao.getCategories();
        for (String productCategory : categories.keySet()) {
            if (productCategory.equalsIgnoreCase(category)) {
                return categories.get(category);
            }
        }
        return number;
    }

    private void editProduct() {
        displayProductsByName("admin");
        boolean endEdition = false;
        while(!endEdition) {
            Product productToEdit = getProductToEdit();
            view.displayQuestion("Chose data to edit [name, price, amount, status, categoryId]");
            String option = reader.getNotEmptyString();
            switch (option) {
                case "name":
                    editName(productToEdit);
                    break;
                case "price":
                    editPrice(productToEdit);
                    break;
                case "amount":
                    editAmount(productToEdit);
                    break;
                case "status":
                    editStatus(productToEdit);
                    break;
                case "categoryId":
                    editCategoryId(productToEdit);
                    break;
                default:
                    view.displayError("Incorrect option");
                    break;
            }
            view.displayQuestion("Edit more data");
            String editMore = reader.getNotEmptyString();
            switch (editMore) {
                case "yes":
                    view.displayProductsForAdmin(products);
                    break;
                case "no":
                    commitChanges(productToEdit);
                    endEdition = true;
                    break;
                default :
                    view.displayError("Incorrect option [yes/no]");
            }

        }
    }

    private void commitChanges(Product productToEdit) {
        productDao.updateProduct(productToEdit);
    }

    private void editCategoryId(Product productToEdit) {
        view.displayCategories(productDao.getCategories());
        productToEdit.setCategoryId(getCategoryId());
    }

    private void editStatus(Product productToEdit) {
        productToEdit.setStatus(getOrderStatus());
    }

    private void editAmount(Product productToEdit) {
        productToEdit.setAmount(getQuantity());
    }

    private void editPrice(Product productToEdit) {
        productToEdit.setPrice(getPrice());
    }

    private void editName(Product productToEdit) {
        productToEdit.setName(getProductName());
    }

    private Product getProductToEdit() {
        String specificProductName = "";
        boolean isProductNameCorrect = false;
        while (!isProductNameCorrect && products.size() != 1) {
            view.displayMessage("Specify product!");
            specificProductName = getProductName();
            products = productDao.readProduct("name", specificProductName, "admin");
            if (isProductOnList(specificProductName)) {
                isProductNameCorrect = true;
                displayProducts("admin");
            }
        }

        if (products.size() == 1) {
            int firstProduct = 0;
            specificProductName = products.get(firstProduct).getName();
        }

        return getProductByName(specificProductName);
    }

    private void displayMenu(String userType) {
        String unloggedMenu = "1. Back to main menu 2. By category 3. By name";
        String userMenu = "1. Back to main menu 2. By category 3. By name 4. Add product to basket";
        String adminMenu = "1. Back to main menu 2. Show by category 3. Show by name 4. Edit products";

        switch (userType) {
            case "anonymous":
                view.displayMenu(unloggedMenu);
                break;
            case "customer":
                view.displayMenu(userMenu);
                break;
            case "admin":
                view.displayMenu(adminMenu);
                break;
            default:
                view.displayError("No option available");
                break;
        }
    }


    private void displayProductsByCategory(String userType) {
        view.clearScreen();
        TreeMap<String, Integer> categories = productDao.getCategories();
        view.displayCategories(categories);
        String category = reader.getCategoryFromUser(categories.keySet());
        products = productDao.readProduct("categoryName", category, userType);
        displayProducts(userType);
    }

    private void displayProductsByName(String userType) {
        view.clearScreen();
        String productName = getProductName();
        products = productDao.readProduct("name", productName, userType);
        if (isProductOnList(productName)) {
            displayProducts(userType);
        } else {
            view.displayError("No game by that name");
        }
    }

    private void addProductToBasket(String userType) {
        if (userType.equals("customer")) {
            String productNameInBasket = getProductName();
            ifProductOnList(productNameInBasket);
        }
    }

    private void ifProductOnList(String productNameInBasket) {
        if (isProductOnList(productNameInBasket)) {
            int quantity = getQuantity(productNameInBasket);
            ifProductInBasket(productNameInBasket, quantity);
            view.clearScreen();
        } else {
            view.displayMessage("No product available by that name!");
        }
    }

    private int getQuantity(String productNameInBasket) {
        view.displayMessage("Enter product amount: ");
        return reader.getNumberInRange(1, getProductByName(productNameInBasket).getAmount());
    }

    private void ifProductInBasket(String productNameInBasket, int quantity) {
        if (basket.getOrderDetails().size() > 0 && checkIfProductIsInBasket(productNameInBasket)) {
            addProductIfEnoughAmountInStock(productNameInBasket, quantity);
        } else {
            basket.addOrderDetails(new OrderDetail(getProductByName(productNameInBasket), quantity));
        }
    }

    private void addProductIfEnoughAmountInStock(String productNameInBasket, int quantity) {
        if (isExceedStock(productNameInBasket, quantity)) {
            updateProductInBasket(productNameInBasket, quantity);
        } else {
            view.displayMessage("Not enough amount of product in our shop!");
        }
    }

    private boolean isExceedStock(String productNameInBasket, int quantity) {
        OrderDetail userOrder = getOrderDetailFromBasket(productNameInBasket);
        int maxAmount = userOrder.getProduct().getAmount();
        return quantity + userOrder.getQuantity() < maxAmount;
    }

    private OrderDetail getOrderDetailFromBasket(String productNameInBasket) throws IllegalArgumentException {
        for (OrderDetail orderDetail : basket.getOrderDetails()) {
            if (orderDetail.getProduct().getName().equalsIgnoreCase(productNameInBasket)) {
                return orderDetail;
            }
        }
        throw new IllegalArgumentException("Order detail doesn't exist in basket!");
    }

    private boolean checkIfProductIsInBasket(String productNameBasket) {
        for (int i = 0; i < basket.getOrderDetails().size(); i++) {
            if (basket.getOrderDetails().get(i).getProduct().equals(getProductByName(productNameBasket))) {
                return true;
            }
        }
        return false;
    }

    private void updateProductInBasket(String productNameBasket, int quantity) {
        for (int i = 0; i < basket.getOrderDetails().size(); i++) {
            if (basket.getOrderDetails().get(i).getProduct().equals(getProductByName(productNameBasket))) {
                basket.getOrderDetails().get(i).updateQuantity(quantity);
            }
        }
    }

    private boolean isProductOnList(String name) {
        for (Product product : products) {
            if (name.toLowerCase().equals(product.getName().toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private Product getProductByName(String name) throws IllegalArgumentException {
        for (Product product : products) {
            if (product.getName().toLowerCase().equals(name.toLowerCase())) {
                return product;
            }
        }
        throw new IllegalArgumentException("No product by that name!");
    }

    private void displayProducts(String userType) {
        if(userType.equals("admin")){
            view.displayProductsForAdmin(products);
        }else{
            view.displayProductsForUser(products);
        }

    }
}

