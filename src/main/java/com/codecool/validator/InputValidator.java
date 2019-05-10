package com.codecool.validator;

public class InputValidator {
    public boolean isInputEmpty(String input) {
        return !input.isBlank();
    }

    public boolean isNumber(String input) {
        return input.matches("[\\d]+");
    }

    public boolean isInRange(int input, int start, int end) {
        return (start <= input && input <= end);
    }

    public boolean isInRange(double input, double start, double end) {
        return (start <= input && input <= end);
    }
}
