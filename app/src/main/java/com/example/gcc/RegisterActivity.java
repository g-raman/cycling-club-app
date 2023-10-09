package com.example.gcc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        FirebaseApp.initializeApp(this);
    }

    public void tryRegister(View view){
        Boolean tryToRegister = registerUser();
        if (tryToRegister) {
            System.out.println("l8r");
        } else {
            System.out.println("epic fail");
        }

    }

    private Boolean registerUser() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();

        username = findViewById(R.id.usernameLogin);
        password = findViewById(R.id.passwordLogin);

        String strUsername = username.getText().toString();
        String strPassword = password.getText().toString();

        DatabaseReference newUserRole = db.getReference("users/"+strUsername+"/role");
        DatabaseReference newUserPassword = db.getReference("users/"+strPassword+"/password");

        newUserPassword.setValue(strPassword);

        return true;
    }
}