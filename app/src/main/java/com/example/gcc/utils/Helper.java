package com.example.gcc.utils;

import android.widget.Toast;

import com.example.gcc.RegisterActivity;

public class Helper {
    /*
    Username must:
    Be 4 characters long
    Only include characters, numbers, underscores, & periods
     */
    private final String USERNAME_REGEX = "^[A-Za-z0-9_.]{4,}$";

    /*
    At least one letter.
    At least one digit.
    At least one special character (in this case, @, #, $, %, ^, &, +, or =).
    Minimum length of 8 characters.
    */
    private final String PASSWORD_REGEX = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$";

    public String validateFields(String username, String password) {
        if (username.length() < 4) {
            return "Username must be at least 4 characters long";
        } else if (!username.matches(USERNAME_REGEX)) {
            return "Username can only include letters, numbers, periods, & underscores";
        } else if (!password.matches(PASSWORD_REGEX)) {
            return "Make sure password meets all requirements";
        }
        return "Registration Successful";
    }
}
