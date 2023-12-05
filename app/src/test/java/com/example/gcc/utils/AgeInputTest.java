package com.example.gcc.utils;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class AgeInputTest {

    private int inputAge;
    private boolean expectedResult;
    private Helper helper;

    public AgeInputTest(int age, boolean expectedResult) {
        super();
        this.inputAge = age;
        this.expectedResult = expectedResult;
    }

    @Before
    public void initialize() {
        helper = new Helper();
    }

    @Parameterized.Parameters
    public static Collection input() {
        Object[][] testCases = new Object[3][2];
        testCases[0] = new Object[]{-10, false};
        testCases[1] = new Object[]{0, false};
        testCases[2] = new Object[]{20, true};
        return Arrays.asList(testCases);
    }

    @Test
    public void testHelper() {
        assertEquals(expectedResult, helper.validateAge(inputAge));
    }
}

