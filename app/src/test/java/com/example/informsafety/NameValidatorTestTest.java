package com.example.informsafety;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

public class NameValidatorTestTest {

    // Testing name validation for sign up page
    public boolean validateName(String name) {
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (!(Character.isLetter(c) || Character.isWhitespace(c) || c == '\'' || c == '-')) {
                return false;
            }
        }
        return true;
    }

    @Test
    public void validateNameIsCorrect() {

        String name = "John";
        validateName(name);

        Assert.assertTrue("Test Passed", validateName(name));

    }

    @Test
    public void validateNameIsIncorrect() {

        String name = "123";
        validateName(name);

        Assert.assertFalse("Test Passed", validateName(name));
    }
}