package com.example.cse_3311_freshman_app;

import android.os.Bundle;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.cse_3311_freshman_app.databinding.ActivityCreateEventBinding;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CreateEventActivity extends AppCompatActivity {

    FirebaseAuth auth;              // variable giving us authorization in firebase
    FirebaseFirestore db;           // variable to hold the firestore database in firebases

    public void onCreate(Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();              // connect variable to firebase


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_club);

        Spinner categorySpinner = findViewById(R.id.spinner_category);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);


        final Button submit_button = findViewById(R.id.submitt_butt);
        submit_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText name_edit = (EditText) findViewById(R.id.event_name);
                EditText location_edit = (EditText) findViewById(R.id.event_location);
                EditText desc_edit = (EditText) findViewById(R.id.event_description);
                db = FirebaseFirestore.getInstance();           // connect variable to firestore database

                // TODO: Get orgs the user is an owner in


                // Add org to database
                //(String e_name, String e_desc, String e_location, Timestamp time, String e_image)
                //create org class
                Event new_event = new Event(name_edit.getText().toString(), desc_edit.getText().toString(), location_edit.getText().toString(), new Timestamp(new Date(10, 1, 20)), "");
                // check org against database (org contact)
                // create hash map for org
                Map<String, Object> event = new HashMap<>();
                event.put("name", new_event.getE_name());
                event.put("desc", new_event.getE_desc());
                event.put("address", new_event.getE_location());
                event.put("email", new_event.getTime());
                event.put("location", new_event.getE_image());

                /*Map<String, Object> org_roles = new HashMap<>();
                org_roles.put("")*/

                db.collection("Organizations").document(new_event.getE_name())
                        .set(event)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //Log.d(TAG, "DocumentSnapshot successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //Log.w(TAG, "Error writing document", e);
                            }
                        });

                // create org in database
                // go to org edit page
            }
        });
    }
}