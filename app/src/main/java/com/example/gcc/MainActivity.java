package com.example.gcc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import javax.xml.transform.Result;

public class MainActivity extends AppCompatActivity {

    private final String EMAIL_REGEX = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\\\.[a-zA-Z0-9-.]+$";

    /*
    At least one letter.
    At least one digit.
    At least one special character (in this case, @, #, $, %, ^, &, +, or =).
    Minimum length of 8 characters.
    */
    private final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@#$%^&+=!])([A-Za-z\\d@#$%^&+=!]){8,}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private boolean validateField(String field, String regex) {
        return field.equals(regex);
    }

    public Result register(String email, String password, String role) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference newUserRole = db.getReference("users/" + email + "/role");
        DatabaseReference newUserPassword = db.getReference("users/" + email + "/password");

        newUserRole.setValue(role);
        newUserPassword.setValue(password);
        //new stuff l8r
    }
}