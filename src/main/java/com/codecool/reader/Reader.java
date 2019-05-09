package com.codecool.reader;

import com.codecool.validator.InputValidator;
import com.codecool.viewer.View;

import java.util.Scanner;
import java.util.Set;

public class Reader {
    private Scanner scanner;
    private View view;
    private InputValidator inputValidator;

    public Reader(View view, InputValidator inputValidator) {
        this.scanner = new Scanner(System.in);
        this.view = view;
        this.inputValidator = inputValidator;
    }

    private String getInput() {
        return scanner.nextLine();
    }

    public String getStringFromUser(String question) {
        String userInput = "";
        while (userInput.isBlank()){
            this.view.displayQuestion(question);
            userInput = this.getInput();
            if (userInput.isBlank()){
                view.displayError("Please, provide correct data");
            }
        }
        return userInput;
    }

    public int getNumberInRange(int start, int end) {
        while (true) {
            String input = getInput();
            if(inputValidator.isInputEmpty(input) && inputValidator.isNumber(input)) {
                int numInput = Integer.parseInt(input);
                if(inputValidator.isInRange(numInput, start, end)) {
                    return numInput;
                } else {
                    view.displayError("Please, provide correct data");
                }
            } else {
                view.displayError("Please, provide correct data");
            }
        }
    }

    public double getNumberInRange(double start, double end) {
        while (true) {
            String input = getInput();
            if(inputValidator.isInputEmpty(input) && inputValidator.isNumber(input)) {
                double numInput = Double.parseDouble(input);
                if(inputValidator.isInRange(numInput, start, end)) {
                    return numInput;
                } else {
                    view.displayError("Please, provide correct data");
                }
            } else {
                view.displayError("Please, provide correct data");
            }
        }
    }

    public String getCategoryFromUser(Set<String> categories) {
        String userInput = "";
        while (userInput.isBlank()){
            this.view.displayQuestion("Choose category");
            userInput = this.getInput();
            if (userInput.isBlank() && !categories.contains(userInput)){
                view.displayError("Please, provide correct data");
            }
        }
        return userInput;
    }
}
