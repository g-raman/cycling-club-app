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
import android.widget.Button;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClubOwnerActivitySettings extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICK = 1;
    private Uri selectedImageUri;
    ClubOwner newClubOwner;
    String UUID;
    DatabaseReference dbClub;

    public boolean validatePhoneNum(String phoneNum) {
        /**
         * Checks for an plus sign for country code
         * Checks for an optional country code
         * Checks for an optional area code
         * Checks for 10 digits
         */
        Pattern pattern = Pattern.compile("^\\+?1?\\s*\\(?\\d{3}\\)?[-.\\s]?\\d{3}[-.\\s]?\\d{4}$");
        Matcher matcher = pattern.matcher(phoneNum);

        return matcher.matches();
    }

    public boolean validateEmail(String email) {
        String regex = "^[a-zA-Z0-9_][a-zA-Z0-9_]+@[a-zA-Z0-9]+(?:\\.[a-zA-Z0-9_]+)";
        Pattern pattern = Pattern.compile(regex, Pattern.UNICODE_CASE);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    public boolean validateInstagramLink(String link) {
        String regex = "^(https?:\\/\\/)?(www\\.)?instagram\\.com\\/[a-zA-Z0-9_]+\\/?$";
        Pattern pattern = Pattern.compile(regex, Pattern.UNICODE_CASE);
        Matcher matcher = pattern.matcher(link);

        return matcher.matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_owner_settings);

        Intent i = getIntent();
        newClubOwner = (ClubOwner) i.getSerializableExtra("USER");
        UUID = (String) i.getSerializableExtra("UUID");
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
        EditText editTextclubSocialMedia = findViewById(R.id.clubSocialMediaHandle);
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
                if (snapshot.child("clubsocial").exists()){
                    editTextclubSocialMedia.setText(snapshot.child("clubsocial").getValue().toString());
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



        clubImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_IMAGE_PICK);
            }
        });

        Button chngSettings = findViewById(R.id.changeEventSettingsBtn);
        
        chngSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInstagramLink(editTextclubSocialMedia.getText().toString())){
                    dbClub.child("clubsocial").setValue(editTextclubSocialMedia.getText().toString());
                }
                else {
                    Toast.makeText(ClubOwnerActivitySettings.this, "Try valid Instagram link.", Toast.LENGTH_SHORT).show();
                }
                if (validatePhoneNum(editTextclubNumber.getText().toString())){
                    dbClub.child("clubnumber").setValue(editTextclubNumber.getText().toString());
                }
                else {
                    Toast.makeText(ClubOwnerActivitySettings.this, "Try valid Number.", Toast.LENGTH_SHORT).show();
                }
                if (validateEmail(editTextclubEmail.getText().toString())){
                    dbClub.child("clubemail").setValue(editTextclubEmail.getText().toString());
                }
                else {
                    Toast.makeText(ClubOwnerActivitySettings.this, "Try valid Email.", Toast.LENGTH_SHORT).show();
                }
                dbClub.child("clubdesc").setValue(editTextClubDesc.getText().toString());
                dbClub.child("clubname").setValue(editTextClubName.getText().toString());

            }
        });



    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            ImageView clubImg = findViewById(R.id.clubImage);
            InputStream inputStream = null;
            try {
                inputStream = getContentResolver().openInputStream(selectedImageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                clubImg.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }


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