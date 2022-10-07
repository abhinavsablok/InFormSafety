package com.example.informsafety;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

}

class NameValidatorTest {

    @Test
    public void validateNameIsCorrect()
    {
        assertTrue(validateName.isValidName("Olivia Brown"));
    }

    @Test
    public void validateNameIsNotCorrect() {
        assertFalse(validateName.isvalidName("12345"));
    }
}

