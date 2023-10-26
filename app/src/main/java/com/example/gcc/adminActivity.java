package com.example.gcc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.gcc.utils.Helper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class adminActivity extends AppCompatActivity implements Serializable {
    interface callBack {
        void canRegister(boolean isAllowed);
    }
    interface eventTypeCB {
        void canAddEventType(boolean isAllowed);
    }

    DatabaseReference dbUsers;
    DatabaseReference dbEventTypes;
    ListView listViewAccounts;
    ListView listViewEventTypes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        dbEventTypes = FirebaseDatabase.getInstance().getReference("eventTypes");
        listViewEventTypes = findViewById(R.id.listEventTypesView);
        List<eventType> eventTypes = new ArrayList<>();
        dbEventTypes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //eventTypes.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    eventType newEventType = postSnapshot.getValue(eventType.class);
                    eventTypes.add(newEventType);

                }
                EventTypeList eventTypeAdaptor = new EventTypeList(adminActivity.this, eventTypes);
                listViewEventTypes.setAdapter(eventTypeAdaptor);
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        dbUsers = FirebaseDatabase.getInstance().getReference("users");

        listViewAccounts = findViewById(R.id.listUsersView);
        List<Account> Accounts = new ArrayList<>();
        Button addUserBtn = findViewById(R.id.addUserBtn);

        dbUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Accounts.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    User account = new User(postSnapshot.child("password").getValue().toString(),postSnapshot.child("role").getValue().toString(),postSnapshot.getKey().toString());
                    //Log.d("TAG",account.getUsername());
                    Accounts.add(account);

                }
                AccountList accountAdaptor = new AccountList(adminActivity.this, Accounts);

                listViewAccounts.setAdapter(accountAdaptor);
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
        listViewAccounts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Account account = Accounts.get(i);
                showUpdateDeleteDialog(account.getUsername(), account.getPassword(),account.getRole());
                return true;
            }
        });




        Spinner level = findViewById(R.id.spinnerLevel);
        Integer[] Items = new Integer[]{1,2,3,4,5};

        ArrayAdapter<Integer> levelAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Items);
        level.setAdapter(levelAdapter);
        Button addEventTypeBtn = findViewById(R.id.addEventTypeBtn);

        addEventTypeBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                EditText eventTypeNameText = findViewById(R.id.editTextEventTypeName);
                EditText eventTypeDescText = findViewById(R.id.editTextEventTypeDesc);
                EditText eventTypeMinPaceText = findViewById(R.id.editTextMinPace);
                EditText eventTypeMaxPaceText = findViewById(R.id.editTextPaceMax);
                EditText eventTypeAge = findViewById(R.id.editTextAge);

                Integer levelInt = level.getSelectedItemPosition()+1;
                String eventTypeName = eventTypeNameText.getText().toString();
                String eventTypeDesc = eventTypeDescText.getText().toString();
                Float eventTypeMinPace=0.0f;
                Float eventTypeMaxPace=0.0f;
                Integer ageInt=0;
                //make this apart of the helper func maybe?
                if ((eventTypeMinPaceText.getText().toString().matches("^\\d+(\\.\\d+)?")) && (eventTypeMaxPaceText.getText().toString().matches("^\\d+(\\.\\d+)?"))){
                    eventTypeMinPace = Float.parseFloat(eventTypeMinPaceText.getText().toString());
                    eventTypeMaxPace = Float.parseFloat(eventTypeMaxPaceText.getText().toString());
                }
                else {
                    Toast.makeText(adminActivity.this, "bad pace vals", Toast.LENGTH_SHORT).show();
                }
                if (eventTypeAge.getText().toString().matches("^\\d+(\\.\\d+)?")){
                    ageInt = Integer.parseInt(eventTypeAge.getText().toString());
                }
                canAddEventType(new eventTypeCB() {
                    @Override
                    public void canAddEventType(boolean isAllowed) {
                        Toast.makeText(adminActivity.this, "added eventType", Toast.LENGTH_LONG).show();
                    }
                }, eventTypeName,eventTypeDesc,eventTypeMinPace,eventTypeMaxPace,levelInt, ageInt);


            }
        });

    }
    private void canAddEventType(eventTypeCB canAddType, String Name, String Desc, Float minPace, Float maxPace, Integer level, Integer age){
        FirebaseDatabase db = FirebaseDatabase.getInstance();

        DatabaseReference ref = db.getReference("eventTypes");
        DatabaseReference name = ref.child(Name);

        name.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();

                    if (snapshot.exists()) {
                        Toast.makeText(adminActivity.this, "EventType already exists", Toast.LENGTH_SHORT).show();
                        canAddType.canAddEventType(false);

                    } else {
                        Log.d("TAG", "The Document doesn't exist.");
                        canAddType.canAddEventType(true);
                        eventType newEventType = new eventType(Name,Desc,level,minPace,maxPace, age);
                        ref.child(Name).setValue(newEventType);
                    }
                }else {
                    Log.d("TAG", task.getException().getMessage()); //Never ignore potential errors!
                    canAddType.canAddEventType(false);
                }
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


    private void showUpdateDeleteDialog(final String userName,String userpwd,String userRole) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.account_update_dialog, null);
        dialogBuilder.setView(dialogView);

        String[] Roles = new String[]{"user","owner"};
        ArrayAdapter<String> roleAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Roles);
        Spinner role = dialogView.findViewById(R.id.spinnerUpdateRole);
        role.setAdapter(roleAdapter);

        final EditText editTextUsername = (EditText) dialogView.findViewById(R.id.editTextUserName);
        final EditText editTextUserpwd  = (EditText) dialogView.findViewById(R.id.editTextUserPwd);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateUser);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteUser);

        editTextUsername.setText(userName);
        editTextUserpwd.setText(userpwd);
        if (userRole.equals("user")){
            role.setSelection(0);
        } else {
            role.setSelection(1);
        }

        dialogBuilder.setTitle(userName);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextUsername.getText().toString().trim();
                String pwd = (editTextUserpwd.getText().toString());
                String roleStr = role.getSelectedItem().toString();
                if (!(TextUtils.isEmpty(name) && TextUtils.isEmpty(pwd))) {
                    updateUsername(name, pwd, roleStr);
                    b.dismiss();
                }
            }
        });
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextUsername.getText().toString().trim();
                deleteUser(name);
                b.dismiss();
            }
        });


    }
    private void updateUsername(String userName,String userPwd,String userRole){
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference ("users").child(userName);
        Account acc;
        if (userRole.equals("user")){
            acc = new User(userName,userPwd,userRole);
        }
        else {
            acc = new ClubOwner(userName,userPwd,userRole);
        }
        dR.setValue(acc);
        Toast.makeText(getApplicationContext(), "User Updated", Toast.LENGTH_LONG).show();
    }
    private void deleteUser(String name){
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference ("users").child(name);
        dR.removeValue();
        Toast.makeText(getApplicationContext(), "User Deleted", Toast.LENGTH_LONG).show();
    }
}
