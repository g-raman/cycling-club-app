package com.example.gcc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ClubOwnerActivitySettings extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICK = 1;
    private Uri selectedImageUri;
    ClubOwner newClubOwner;
    String UUID;
    DatabaseReference dbClub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_owner_settings);

        Intent i = getIntent();
        newClubOwner = (ClubOwner)i.getSerializableExtra("USER");
        UUID = (String)i.getSerializableExtra("UUID");
        Log.d("TAG", UUID);
        BottomNavigationView nav = findViewById(R.id.navClubOwner);
        nav.setSelectedItemId(R.id.nav_club_owner_settings);
        nav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_club_owner_settings) {
                return true;
            } else if (item.getItemId() == R.id.nav_club_owner_events) {
                Intent clubOwnerEvents = new Intent(ClubOwnerActivitySettings.this, ClubOwnerActivityEvents.class);
                clubOwnerEvents.putExtra("USER", newClubOwner);
                startActivity(clubOwnerEvents);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                return true;
            }
            return false;
        });
        EditText editTextClubName = findViewById(R.id.clubName);
        EditText editTextClubDesc = findViewById(R.id.clubDescription);
        EditText editTextclubNumber = findViewById(R.id.clubPhoneNumber);
        EditText editTextclubEmail = findViewById(R.id.clubEmailAddress);
        ImageView clubImg = findViewById(R.id.clubImage);
        dbClub = FirebaseDatabase.getInstance().getReference("clubs").child(UUID);

        dbClub.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("clubname").exists()) {
                    editTextClubName.setText(snapshot.child("clubname").getValue().toString());
                }
                if (snapshot.child("clubdesc").exists()) {
                    editTextClubDesc.setText(snapshot.child("clubdesc").getValue().toString());
                }
                if (snapshot.child("clubnumber").exists()) {
                    editTextclubNumber.setText(snapshot.child("clubnumber").getValue().toString());
                }
                if (snapshot.child("clubemail").exists()) {
                    editTextclubEmail.setText(snapshot.child("clubemail").getValue().toString());
                }
                if (snapshot.child("clubimg").exists()){
                    String base64Image = snapshot.child("clubimg").getValue(String.class);
                    byte[] decodedByteArray = Base64.decode(base64Image, Base64.DEFAULT);
                    // Convert byte array to bitmap
                    Bitmap bitmap = BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
                    // Set the bitmap to the ImageView
                    clubImg.setImageBitmap(bitmap);
                }


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Adding a TextWatcher to the EditText
        editTextClubName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                dbClub.child("clubname").setValue(editable.toString());
                Toast.makeText(getApplicationContext(), "name updated", Toast.LENGTH_SHORT).show();
            }
        });
        editTextClubDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                dbClub.child("clubdesc").setValue(editable.toString());
                Toast.makeText(getApplicationContext(), "description updated", Toast.LENGTH_SHORT).show();
            }
        });
        editTextclubNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                dbClub.child("clubnumber").setValue(editable.toString());
                Toast.makeText(getApplicationContext(), "number updated", Toast.LENGTH_SHORT).show();
            }
        });
        editTextclubEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                dbClub.child("clubemail").setValue(editable.toString());
                Toast.makeText(getApplicationContext(), "email updated", Toast.LENGTH_SHORT).show();
            }
        });



        clubImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_IMAGE_PICK);
            }
        });


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            uploadImageToFirebaseDatabase(selectedImageUri);
        }
    }

    private void uploadImageToFirebaseDatabase(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            byte[] imageBytes = getBytes(inputStream);
            String base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            dbClub.child("clubimg").setValue(base64Image)
                    .addOnSuccessListener(aVoid -> {
                    })
                    .addOnFailureListener(e -> {
                    });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Utility method to convert InputStream to byte array
    private byte[] getBytes(InputStream inputStream) {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len;
        try {
            while ((len = inputStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteBuffer.toByteArray();
    }
}