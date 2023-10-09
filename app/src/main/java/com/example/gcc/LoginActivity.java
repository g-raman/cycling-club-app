package com.example.gcc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    interface callBack {
        void canLogin(boolean isAllowed);
    }

    /*
    At least one letter.
    At least one digit.
    At least one special character (in this case, @, #, $, %, ^, &, +, or =).
    Minimum length of 8 characters.
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(this);

        Button registerBtn = findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });
    }


    public void tryLogin(View view) {
        LoginUser(new callBack() {
            @Override
            public void canLogin(boolean isAllowed) {
                if (isAllowed){
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void LoginUser(callBack canUserLogin){
        EditText usernameField =findViewById(R.id.usernameLogin);
        String username = usernameField.getText().toString();

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        DatabaseReference dbRef = db.child("users");
        DatabaseReference dbRefEmail = dbRef.child(username);



        dbRefEmail.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();

                    if(snapshot.exists()) {
                        Log.d("TAG", "The document exists.");
                        EditText passwordField = findViewById(R.id.passwordLogin);
                        String password = passwordField.getText().toString();
                        if(snapshot.child("password").getValue().toString().equals(password)){
                            canUserLogin.canLogin(true);
                        }
                        else {
                            canUserLogin.canLogin(false);
                        }
                    } else {
                        Log.d("TAG", "The Document doesn't exist.");
                        canUserLogin.canLogin(false);
                    }
                } else {
                    Log.d("TAG", task.getException().getMessage()); //Never ignore potential errors!
                    canUserLogin.canLogin(false);
                }
            }
        });
    }
}
