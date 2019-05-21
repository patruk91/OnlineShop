package com.codecool.view.viewer.textViewer;

import com.codecool.model.Product;
import com.codecool.view.viewer.View;

import java.util.ArrayList;

public class TextView implements View {
    @Override
    public void displayMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void displayQuestion(String question) {
        System.out.println(question + "?: ");
    }

    @Override
    public void displayError(String error) {
        System.out.println("Error: " + error + "!");
    }

    @Override
    public void displayMenu(String menu) {
        System.out.println(menu);
    }

    @Override
    public void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    @Override
    public void displayProductsForUser(ArrayList<Product> products) {
        String[] headers = {"ID", "Name", "Price", "Amount"};
        String[][] list = new String[products.size()][];
        int idInTable = 1;
        int id = 0;

        for(Product product : products){
            String[] singleProduct = {String.valueOf(idInTable), product.getName(), Double.toString(product.getPrice()),
                    String.valueOf(product.getAmount())};
            list[id] = singleProduct;
            idInTable++;
            id++;
        }
        System.out.println(FlipTable.of(headers, list));
    }

    @Override
    public void displayProductsForAdmin(ArrayList<Product> products) {
        String[] headers = {"ID", "Name", "Price", "Amount","Status", "CategoryID"};
        String[][] list = new String[products.size()][];
        int idInTable = 1;
        int id = 0;

        for(Product product : products){
            String[] singleProduct = {String.valueOf(idInTable), product.getName(), Double.toString(product.getPrice()),
                    String.valueOf(product.getAmount()), String.valueOf(product.isStatus()), String.valueOf(product.getCategoryId())};
            list[id] = singleProduct;
            idInTable++;
            id++;
        }
        System.out.println(FlipTable.of(headers, list));
    }
}
