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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public Result register(String email,String password, String role){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference newUserRole = db.getReference("users/"+email+"/role");
        DatabaseReference newUserPassword = db.getReference("users/"+email+"/password");

        newUserRole.setValue(role);
        newUserPassword.setValue(password);
        //new stuff l8r
    }
}