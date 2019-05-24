package com.codecool.view.validator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InputValidatorTest {
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



}