package com.example.gcc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
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
    interface callBack {
        void canRegister(boolean isAllowed);
    }
    /*
    Username must:
    Be 4 characters long
    Only include characters, numbers, underscores, & periods
     */
    private final String USERNAME_REGEX = "^[A-Za-z0-9_.]{4,}$";
    private final String PASSWORD_REGEX = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TextView signInLink = findViewById(R.id.signInLink);
        signInLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        Button registerBtn = findViewById(R.id.registerBtn2);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText usernameField = findViewById(R.id.usernameRegister);
                EditText passwordField = findViewById(R.id.passwordRegister);

                RadioGroup radioGroup = findViewById(R.id.accountType);
                int checkedBtnId = radioGroup.getCheckedRadioButtonId();
                RadioButton checkedBtn = findViewById(checkedBtnId);
                String checkedRole = checkedBtn.getText().toString();

                String username = usernameField.getText().toString();
                String password = passwordField.getText().toString();
                String role = checkedRole.equals("Participant") ? "user" : "owner";

                if (username.length() < 4) {
                    Toast.makeText(RegisterActivity.this, "Username must be at least 4 characters long", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!username.matches(USERNAME_REGEX)) {
                    Toast.makeText(RegisterActivity.this, "Username can only include letters, numbers, periods, & underscores", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!password.matches(PASSWORD_REGEX)) {
                    Toast.makeText(RegisterActivity.this, "Make sure password meets all requirements", Toast.LENGTH_SHORT).show();
                    return;
                }
                registerUser(new callBack() {
                    @Override
                    public void canRegister(boolean isAllowed) {
                        if (isAllowed) {
                            Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_LONG).show();
                            User newUser = new User(password,role,username);
                            Intent welcomeActivity = new Intent(RegisterActivity.this, WelcomeActivity.class);
                            welcomeActivity.putExtra("USER",newUser);
                            startActivity(welcomeActivity);
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Registration Failed, try a new username", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, username, password, role);
            }
        });
    }


    private void registerUser(callBack canUserLogin, String username, String password, String role) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("users");

        DatabaseReference dbRefEmail = ref.child(username);

        dbRefEmail.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();

                    if(snapshot.exists()) {
                        canUserLogin.canRegister(false);

                    } else {
                        Log.d("TAG", "The Document doesn't exist.");
                        canUserLogin.canRegister(true);
                        ref.child(username).setValue(new User(password, role));
                    }
                } else {
                    Log.d("TAG", task.getException().getMessage()); //Never ignore potential errors!
                    canUserLogin.canRegister(false);
                }
            }
        });
    }

}