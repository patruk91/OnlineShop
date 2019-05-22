package com.codecool.view.validator;

import java.util.regex.Pattern;

public class InputValidator {
    public boolean isInputEmpty(String input) {
        return input.isBlank();
    }

    public boolean isNumber(String input) {
        return input.matches("[\\d]+");
    }

    public boolean isDouble(String input) {
        return Pattern.matches("([0-9]*)\\.([0-9]*)", input);
    }

    public boolean isInRange(int input, int start, int end) {
        return (start <= input && input <= end);
    }

    public boolean isInRange(double input, double start, double end) {
        return (start <= input && input <= end);
    }
}
