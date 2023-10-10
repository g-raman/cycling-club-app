package com.example.gcc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.io.Serializable;

public class WelcomeActivity extends AppCompatActivity implements Serializable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Intent i = getIntent();
        User newUser = (User)i.getSerializableExtra("USER");
        Log.d("TAG", String.valueOf(newUser.getPassword()));
    }
}