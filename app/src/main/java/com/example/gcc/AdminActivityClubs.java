package com.example.gcc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminActivityClubs extends AppCompatActivity {
    DatabaseReference dbEventTypes;

    ListView listViewEventTypes;

    interface eventTypeCB {
        void canAddEventType(boolean isAllowed);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_clubs);

        BottomNavigationView nav = findViewById(R.id.nav);
        nav.setSelectedItemId(R.id.nav_clubs);
        nav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_clubs) {
                return true;
            } else if (item.getItemId() == R.id.nav_users) {
                startActivity(new Intent(getApplicationContext(), AdminActivityUsers.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                return true;
            } else if (item.getItemId() == R.id.nav_settings) {
                startActivity(new Intent(getApplicationContext(), AdminActivitySettings.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                return true;
            }
            return false;
        });

        dbEventTypes = FirebaseDatabase.getInstance().getReference("eventTypes");
        listViewEventTypes = findViewById(R.id.listEventTypesView);
        List<eventType> eventTypes = new ArrayList<>();
        dbEventTypes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                eventTypes.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    eventType newEventType = new eventType(postSnapshot.child("name").getValue().toString(),postSnapshot.child("description").getValue().toString(),Integer.valueOf(String.valueOf(postSnapshot.child("level").getValue())),Float.parseFloat(postSnapshot.child("paceMin").getValue().toString()) ,Float.parseFloat(postSnapshot.child("paceMax").getValue().toString()),Integer.valueOf(postSnapshot.child("age").getValue().toString()));
                    eventTypes.add(newEventType);
                    Log.d("TAG",eventTypes.toString());

                }
                EventTypeList eventTypeAdaptor = new EventTypeList(AdminActivityClubs.this, eventTypes);
                listViewEventTypes.setAdapter(eventTypeAdaptor);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Spinner level = findViewById(R.id.spinnerLevel);
        Integer[] Items = new Integer[]{1, 2, 3, 4, 5};

        ArrayAdapter<Integer> levelAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Items);
        level.setAdapter(levelAdapter);
        Button addEventTypeBtn = findViewById(R.id.addEventTypeBtn);

        addEventTypeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText eventTypeNameText = findViewById(R.id.editTextEventTypeName);
                EditText eventTypeDescText = findViewById(R.id.editTextEventTypeDesc);
                EditText eventTypeMinPaceText = findViewById(R.id.editTextMinPace);
                EditText eventTypeMaxPaceText = findViewById(R.id.editTextPaceMax);
                EditText eventTypeAge = findViewById(R.id.editTextAge);

                Integer levelInt = level.getSelectedItemPosition() + 1;
                String eventTypeName = eventTypeNameText.getText().toString();
                String eventTypeDesc = eventTypeDescText.getText().toString();
                Float eventTypeMinPace = 0.0f;
                Float eventTypeMaxPace = 0.0f;
                Integer ageInt = 0;
                //make this apart of the helper func maybe?
                if ((eventTypeMinPaceText.getText().toString().matches("^\\d+(\\.\\d+)?")) && (eventTypeMaxPaceText.getText().toString().matches("^\\d+(\\.\\d+)?"))) {
                    eventTypeMinPace = Float.parseFloat(eventTypeMinPaceText.getText().toString());
                    eventTypeMaxPace = Float.parseFloat(eventTypeMaxPaceText.getText().toString());
                } else {
                    Toast.makeText(AdminActivityClubs.this, "Invalid pace values", Toast.LENGTH_SHORT).show();
                }
                if (eventTypeAge.getText().toString().matches("^\\d+(\\.\\d+)?")) {
                    ageInt = Integer.parseInt(eventTypeAge.getText().toString());
                }
                canAddEventType(new eventTypeCB() {
                    @Override
                    public void canAddEventType(boolean isAllowed) {
                        Toast.makeText(AdminActivityClubs.this, "Added event type", Toast.LENGTH_SHORT).show();
                    }
                }, eventTypeName, eventTypeDesc, eventTypeMinPace, eventTypeMaxPace, levelInt, ageInt);
            }
        });
    }

    private void canAddEventType(eventTypeCB canAddType, String Name, String Desc, Float minPace, Float maxPace, Integer level, Integer age) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();

        DatabaseReference ref = db.getReference("eventTypes");
        DatabaseReference name = ref.child(Name);

        name.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();

                    if (snapshot.exists()) {
                        Toast.makeText(AdminActivityClubs.this, "Event type already exists", Toast.LENGTH_SHORT).show();
                        canAddType.canAddEventType(false);

                    } else {
                        Log.d("TAG", "The Document doesn't exist.");
                        canAddType.canAddEventType(true);
                        eventType newEventType = new eventType(Name, Desc, level, minPace, maxPace, age);
                        ref.child(Name).setValue(newEventType);
                    }
                } else {
                    Log.d("TAG", task.getException().getMessage()); //Never ignore potential errors!
                    canAddType.canAddEventType(false);
                }
            }
        });
    }
}
