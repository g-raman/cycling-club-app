package com.example.gcc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.view.View;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private final String EMAIL_REGEX = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\\\.[a-zA-Z0-9-.]+$";

    /*
    At least one letter.
    At least one digit.
    At least one special character (in this case, @, #, $, %, ^, &, +, or =).
    Minimum length of 8 characters.
    */
    private final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@#$%^&+=!])([A-Za-z\\d@#$%^&+=!]){8,}$";

    private EditText username;
    private EditText password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(this);
    }

    private boolean validateField(String field, String regex) {
        return field.equals(regex);
    }

    public void tryLogin(View view) {
        boolean[] tryLoginUser = LoginUser();
        if (tryLoginUser[0]==true) {
            System.out.println("l8r");
        } else {
            System.out.println("epic fail");
        }
    }

    private boolean[] LoginUser(){
        final boolean[] isAllowed = {false, false};
        EditText username = findViewById(R.id.editEmailAddress);
        String strUsername = username.getText().toString();

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        DatabaseReference dbRef = db.child("users");
        DatabaseReference dbRefEmail = dbRef.child(strUsername);

        dbRefEmail.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();

                    if(snapshot.exists()) {
                        Log.d("TAG", "The document exists.");
                        password = findViewById(R.id.editPassword);
                        String strPassword = password.getText().toString();
                        if(snapshot.getValue().toString().equals(strPassword)){
                            isAllowed[0] = true;
                        }
                    } else {
                        Log.d("TAG", "The Document doesn't exist.");
                        isAllowed[1] = false;
                    }
                } else {
                    Log.d("TAG", task.getException().getMessage()); //Never ignore potential errors!
                }
            }
        });
        return isAllowed;
    }
}