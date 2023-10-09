package com.example.gcc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class RegisterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button registerBtn = findViewById(R.id.registerBtn2);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (registerUser()) {
                    Toast.makeText(RegisterActivity.this, "Success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean registerUser() {
        EditText usernameField = findViewById(R.id.usernameRegister);
        EditText passwordField = findViewById(R.id.passwordRegister);

        RadioGroup radioGroup = findViewById(R.id.accountType);
        int checkedBtnId = radioGroup.getCheckedRadioButtonId();
        RadioButton checkedBtn = findViewById(checkedBtnId);
        String checkedRole = checkedBtn.getText().toString();

        String role = checkedRole.equals("Participant") ? "user" : "owner";
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("users");

        ref.child(username).setValue(new User(password, role));

        return true;
    }

}