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
    public void testReturnFalseWithInputUnderRange() {
        InputValidator inputValidator = new InputValidator();
        int START = 1;
        int END = 5;
        assertFalse(inputValidator.isInRange(-1, START, END));
    }
}