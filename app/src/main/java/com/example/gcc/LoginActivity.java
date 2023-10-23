package com.example.gcc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    interface callBack {
        void canLogin(boolean isAllowed,String role);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(this);

        TextView signUpLink = findViewById(R.id.signInLink);
        signUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });
    }


    public void tryLogin(View view) {
        EditText passwordField = findViewById(R.id.passwordLogin);
        String password = passwordField.getText().toString();

        EditText usernameField =findViewById(R.id.usernameLogin);
        String username = usernameField.getText().toString();
        String role = "";

        LoginUser(new callBack() {
            @Override
            public void canLogin(boolean isAllowed, String role) {
                if (isAllowed){
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    User newUser = null;
                    ClubOwner newClubOwner = null;
                    if (role.equals("admin")){
                        User adminAccount = new User(password,role,username);
                        Intent adminActivity = new Intent(LoginActivity.this, adminActivity.class);
                        adminActivity.putExtra("USER",adminAccount);
                        startActivity(adminActivity);
                        finish();
                    } else {
                        Intent welcomeActivity = new Intent(LoginActivity.this, WelcomeActivity.class);
                        if (role.equals("owner")) {
                            newClubOwner = new ClubOwner(username, password, role);
                            welcomeActivity.putExtra("USER", newClubOwner);
                        } else if (role.equals("user")) {
                            newUser = new User(password, role, username);
                            welcomeActivity.putExtra("USER", newUser);
                        }
                        welcomeActivity.putExtra("ROLE", role);
                        startActivity(welcomeActivity);
                        finish();
                    }

                } else {
                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }
        }, username, password, role);
    }

    private void LoginUser(callBack canUserLogin, String username, String password, String role){
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();

        DatabaseReference dbRef = db.child("users");
        DatabaseReference dbRefEmail = dbRef.child(username);

        dbRefEmail.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();

                    if(snapshot.exists() && (!(username.equals("")) || !(password.equals("")))) {
                        Log.d("TAG", "The document exists.");

                        if(snapshot.child("password").getValue().toString().equals(password)){
                            canUserLogin.canLogin(true,snapshot.child("role").getValue().toString());
                        }
                        else {
                            canUserLogin.canLogin(false,"");
                        }
                    } else {
                        Log.d("TAG", "The Document doesn't exist.");
                        canUserLogin.canLogin(false,"");
                    }
                } else {
                    Log.d("TAG", task.getException().getMessage()); //Never ignore potential errors!
                    canUserLogin.canLogin(false,"");
                }
            }
        });
    }
}
