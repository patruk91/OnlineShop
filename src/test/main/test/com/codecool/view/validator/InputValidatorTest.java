package com.codecool.view.validator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InputValidatorTest {
    @Test
    public void testReturnTrueForEmptyString() {
        InputValidator inputValidator = new InputValidator();
        assertTrue(inputValidator.isInputEmpty(""));
    }

    @Test
    public void testReturnTrueForStringWithWhiteSpaces() {
        InputValidator inputValidator = new InputValidator();
        assertTrue(inputValidator.isInputEmpty(" "));
    }

    @Test
    public void testReturnTrueForStringWithWhiteSpacesTab() {
        InputValidator inputValidator = new InputValidator();
        assertTrue(inputValidator.isInputEmpty("    "));
    }

    @Test
    public void testReturnTrueForStringWithNewLine() {
        InputValidator inputValidator = new InputValidator();
        assertTrue(inputValidator.isInputEmpty("\n"));
    }

    @Test
    public void testReturnFalseWithNumbericString() {
        InputValidator inputValidator = new InputValidator();
        assertFalse(inputValidator.isInputEmpty("123"));
    }

    @Test
    public void testReturnFalseWithAlphabeticString() {
        InputValidator inputValidator = new InputValidator();
        assertFalse(inputValidator.isInputEmpty("abc"));
    }

    @Test
    public void testReturnFalseWithAlphanumericString() {
        InputValidator inputValidator = new InputValidator();
        assertFalse(inputValidator.isInputEmpty("ab3c67"));
    }

    @Test
    public void testReturnTrueWithProperInput() {
        InputValidator inputValidator = new InputValidator();
        int START = 1;
        int END = 5;
        assertTrue(inputValidator.isInRange(3, START, END));
    }

    @Test
    public void testReturnFalseWithInputOverRange() {
        InputValidator inputValidator = new InputValidator();
        int START = 1;
        int END = 5;
        assertFalse(inputValidator.isInRange(6, START, END));
    }

    @Test
    void testIsNumberReturnFalseForString() {
        InputValidator inputValidator = new InputValidator();
        assertFalse(inputValidator.isNumber("string"));

    }

    @Test
    void testIsNumberReturnTrueForString() {
        InputValidator inputValidator = new InputValidator();
        assertTrue(inputValidator.isNumber("5"));
    }

    @Test
    void testIsNumberReturnFalseForDoubleNumberAsString() {
        InputValidator inputValidator = new InputValidator();
        assertFalse(inputValidator.isNumber("4.5"));
    }

    @Test
    void testIsNumberReturnFalseForEmptyString() {
        InputValidator inputValidator = new InputValidator();
        assertFalse(inputValidator.isNumber(""));
    }

    @Test
    void testIsNumberReturnFalseForAlphabeticAndNumbersString() {
        InputValidator inputValidator = new InputValidator();
        assertFalse(inputValidator.isNumber("a23sdf123d"));
    }


    @Test
    void testIsDoubleReturnFalseForString() {
        InputValidator inputValidator = new InputValidator();
        assertFalse(inputValidator.isDouble("string"));

    }

    @Test
    void testIsDoubleReturnTrueForString() {
        InputValidator inputValidator = new InputValidator();
        assertFalse(inputValidator.isDouble("5"));
    }

    @Test
    void testIsDoubleReturnFalseForDoubleNumberAsString() {
        InputValidator inputValidator = new InputValidator();
        assertTrue(inputValidator.isDouble("4.5"));
    }

    @Test
    void testIsDoubleReturnFalseForEmptyString() {
        InputValidator inputValidator = new InputValidator();
        assertFalse(inputValidator.isDouble(""));
    }

    @Test
    void testIsDoubleReturnFalseForAlphabeticAndNumbersString() {
        InputValidator inputValidator = new InputValidator();
        assertFalse(inputValidator.isDouble("a23sdf123d"));
    }

    @Test
    public void testReturnFalseWithInputUnderRange() {
        InputValidator inputValidator = new InputValidator();
        int START = 1;
        int END = 5;
        assertFalse(inputValidator.isInRange(-1, START, END));
    }
}