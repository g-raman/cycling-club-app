package com.example.gcc;

import com.example.gcc.utils.AgeInputTest;
import com.example.gcc.utils.PaceInputTest;
import com.example.gcc.utils.PasswordInputTest;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class TestRunner {
    public static void main(String[] args) {
        Result paceTestResults = JUnitCore.runClasses(PaceInputTest.class);
        Result ageTestResults = JUnitCore.runClasses(AgeInputTest.class);
        Result passwordTestResults = JUnitCore.runClasses(PasswordInputTest.class);

        String dashes = new String(new char[40]).replace("\0", "-");

        System.out.println("Pace test results " + dashes);
        System.out.println("Failures: " + paceTestResults.getFailures().size());
        for (Failure failure : paceTestResults.getFailures()) System.out.println(failure.toString());
        System.out.println("All successful?: " + paceTestResults.wasSuccessful());
        System.out.println();


        System.out.println("Age test results " + dashes);
        System.out.println("Failures: " + ageTestResults.getFailures().size());
        for (Failure failure : ageTestResults.getFailures()) System.out.println(failure.toString());
        System.out.println("All successful?: " + ageTestResults.wasSuccessful());
        System.out.println();

        System.out.println("Password test results " + dashes);
        System.out.println("Failures: " + passwordTestResults.getFailures().size());
        System.out.println("All successful?: " + passwordTestResults.wasSuccessful());
        for (Failure failure : passwordTestResults.getFailures()) System.out.println(failure.toString());
    }
}