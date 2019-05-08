package com.codecool.reader;

import com.codecool.validator.InputValidator;
import com.codecool.viewer.View;

import java.util.Scanner;

public class Reader {
    private Scanner scanner;
    private View view;
    private InputValidator inputValidator;

    public Reader(Scanner scanner, View view, InputValidator inputValidator) {
        this.scanner = scanner;
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
        int result = 0;
        while (true) {
            String input = getInput();
            if(inputValidator.isInputEmpty(input) && inputValidator.isNumber(input)) {
                int numInput = Integer.parseInt(input);
                if(inputValidator.isInRange(numInput, start, end)) {
                    return result;
                }
            } else {
                view.printError("Please, provide correct data");
            }
        }
        return result;
    }

    public double getNumberInRange(double start, double end) {
        int result = 0;
        while (true) {
            String input = getInput();
            if(inputValidator.isInputEmpty(input) && inputValidator.isNumber(input)) {
                double numInput = Double.parseDouble(input);
                if(inputValidator.isInRange(numInput, start, end)) {
                    return result;
                }
            } else {
                view.printError("Please, provide correct data");
            }
        }
        return result;
    }





}
