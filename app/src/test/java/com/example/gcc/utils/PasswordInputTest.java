package com.example.gcc.utils;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class PasswordInputTest {

    private String inputPassword;
    private boolean expectedResult;
    private Helper helper;

    public PasswordInputTest(String password, boolean expectedResult) {
        super();
        this.inputPassword = password;
        this.expectedResult = expectedResult;
    }

    @Before
    public void initialize() {
        helper = new Helper();
    }

    @Parameterized.Parameters
    public static Collection input() {
        Object[][] testCases = new Object[4][2];
        testCases[0] = new Object[]{"Test", false};
        testCases[1] = new Object[]{"Test1", false};
        testCases[2] = new Object[]{"test@1", true};
        testCases[3] = new Object[]{"Test@1", true};
        return Arrays.asList(testCases);
    }

    @Test
    public void testHelper() {
        assertEquals(expectedResult, helper.validatePassword(inputPassword));
    }
}
