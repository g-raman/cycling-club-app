package com.example.gcc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.Serializable;

public class WelcomeActivity extends AppCompatActivity implements Serializable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Intent i = getIntent();
        User newUser = (User)i.getSerializableExtra("USER");
        TextView userName = (TextView)findViewById(R.id.displayName);
        TextView userRole = (TextView)findViewById(R.id.displayRole);

        userName.setText(newUser.getUsername().toString());
        userRole.setText(newUser.getRole().toString());
    }
}