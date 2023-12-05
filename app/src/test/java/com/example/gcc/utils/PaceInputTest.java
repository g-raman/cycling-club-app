package com.example.gcc.utils;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class PaceInputTest {

    private float inputPace;
    private boolean expectedResult;
    private Helper helper;

    public PaceInputTest(float pace, boolean expectedResult) {
        super();
        this.inputPace = pace;
        this.expectedResult = expectedResult;
    }

    @Before
    public void initialize() {
        helper = new Helper();
    }

    @Parameterized.Parameters
    public static Collection input() {
        Object[][] testCases = new Object[3][2];
        testCases[0] = new Object[]{-1, false};
        testCases[1] = new Object[]{0, false};
        testCases[2] = new Object[]{2, true};
        return Arrays.asList(testCases);
    }

    @Test
    public void testHelper() {
        assertEquals(expectedResult, helper.validatePace(inputPace));
    }
}
