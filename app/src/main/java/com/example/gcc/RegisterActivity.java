package com.example.gcc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private EditText email;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void tryRegister(View view){
        Boolean tryToRegister = registerUser();
        if (tryToRegister==true) {
            System.out.println("l8r");
        } else {
            System.out.println("epic fail");
        }

    }

    private Boolean registerUser() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();

        email = findViewById(R.id.usernameLogin);
        password = findViewById(R.id.passwordLogin);

        String strEmail = email.getText().toString();
        String strPassword = password.getText().toString();

        DatabaseReference newUserRole = db.getReference("users/"+strEmail+"/role");
        DatabaseReference newUserPassword = db.getReference("users/"+strPassword+"/password");

        newUserPassword.setValue(strPassword);

        return true;
    }
}