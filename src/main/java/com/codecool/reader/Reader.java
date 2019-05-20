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
        return scanner.nextLine().trim();
    }

    public String getNotEmptyString() {
        String input = "";
        while (inputValidator.isInputEmpty(input)) {
            input = getInput();
            if (inputValidator.isInputEmpty(input)) {
                view.displayError("Please, provide not empty data");
            }
        }
        return input;
    }

    private String getNumberAsString() {
        String input = "";
        while (!inputValidator.isNumber(input)) {
            input = getNotEmptyString();
            if (!inputValidator.isNumber(input)) {
                view.displayError("Please, provide numeric data");
            }
        }
        return input;
    }

    public int getNumberInRange(int start, int end) {
        int number = Integer.parseInt(getNumberAsString());
        while (!inputValidator.isInRange(number, start, end)) {
            view.displayError("Please, provide number in range");
            number = Integer.parseInt(getNumberAsString());
        }
        return number;
    }

    public double getNumberInRange(double start, double end) {
        double number = Double.parseDouble(getNumberAsString());
        while (!inputValidator.isInRange(number, start, end)) {
            view.displayError("Please, provide number in range");
            number = Double.parseDouble(getNumberAsString());
        }
        return number;
    }

    public String getCategoryFromUser(Set<String> categories) {
        view.displayQuestion("Choose category");
        String input = getNotEmptyString();
        while (!categories.contains(input.toUpperCase())) {
            view.displayError("Please, provide correct category");
            input = getNotEmptyString();
        }
        return input;
    }
}
