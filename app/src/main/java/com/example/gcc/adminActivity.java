package com.example.gcc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gcc.utils.Helper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.units.qual.A;

public class adminActivity extends AppCompatActivity implements Serializable {
    interface callBack {
        void canRegister(boolean isAllowed);
    }

    DatabaseReference dbUsers;
    ListView listViewAccounts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        dbUsers = FirebaseDatabase.getInstance().getReference("users");

        listViewAccounts = (ListView) findViewById(R.id.listUsersView);
        List<Account> Accounts = new ArrayList<Account>();
        Button addUserBtn = findViewById(R.id.addUserBtn);
        dbUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Accounts.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    User account = new User(postSnapshot.child("password").getValue().toString(),postSnapshot.child("role").getValue().toString(),postSnapshot.getKey().toString());
                    Log.d("TAG",account.getUsername());
                    Accounts.add(account);
                    AccountList accountAdaptor = new AccountList(adminActivity.this, Accounts);
                    listViewAccounts.setAdapter(accountAdaptor);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
        addUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText usernameField = findViewById(R.id.editTextUsername);
                EditText passwordField = findViewById(R.id.editTextPassword);

                RadioGroup radioGroup = findViewById(R.id.accountType);
                int checkedBtnId = radioGroup.getCheckedRadioButtonId();
                RadioButton checkedBtn = findViewById(checkedBtnId);
                String checkedRole = checkedBtn.getText().toString();

                String username = usernameField.getText().toString();
                String password = passwordField.getText().toString();
                String role = checkedRole.equals("Participant") ? "user" : "owner";




                Helper helper = new Helper();
                String msg = helper.validateFields(username, password);
                if (!msg.equals("Registration Successful")) {
                    Toast.makeText(adminActivity.this, msg, Toast.LENGTH_SHORT).show();
                    return;
                }
                registerUser(new callBack() {
                    @Override
                    public void canRegister(boolean isAllowed) {
                        if (isAllowed) {
                            Toast.makeText(adminActivity.this, "Registration Successful", Toast.LENGTH_LONG).show();
                            User newUser;
                            ClubOwner newClubOwner;
                            if (role.equals("owner")) {
                                newClubOwner = new ClubOwner(username, password, role);
                            } else if (role.equals("user")) {
                                newUser = new User(password, role, username);
                            }
                        } else {
                            Toast.makeText(adminActivity.this, "Registration Failed, try a new username", Toast.LENGTH_SHORT).show();
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

                    if (snapshot.exists()) {
                        canUserLogin.canRegister(false);

                    } else {
                        Log.d("TAG", "The Document doesn't exist.");
                        canUserLogin.canRegister(true);
                        if (role.equals("owner")){
                            ref.child(username).setValue(new ClubOwner(password, role));
                        } else if (role.equals("user")){
                            ref.child(username).setValue(new User(password, role));
                        }
                    }
                } else {
                    Log.d("TAG", task.getException().getMessage()); //Never ignore potential errors!
                    canUserLogin.canRegister(false);
                }
            }
        });
    }


    private void showUpdateDeleteDialog(final String productId, String productName) {

    }
}
