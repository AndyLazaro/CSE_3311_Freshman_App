package com.example.cse_3311_freshman_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;

public class CreateClubActivity extends AppCompatActivity
{
    FirebaseAuth auth;              // variable giving us authorization in firebase
    FirebaseFirestore db;           // variable to hold the firestore database in firebases

    public void onCreate(Bundle savedInstanceState)
    {
        auth = FirebaseAuth.getInstance();              // connect variable to firebase


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_club);

        Spinner categorySpinner = findViewById(R.id.spinner_category);
        ArrayAdapter<CharSequence>adapter= ArrayAdapter.createFromResource(this, R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);


        final Button submit_button = findViewById(R.id.submitt_butt);
        submit_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Get all text fields
                EditText name_edit =   (EditText)findViewById(R.id.club_name);
                EditText location_edit =   (EditText)findViewById(R.id.club_location);
                EditText description_edit =   (EditText)findViewById(R.id.club_description);
                TextView categoryView = (TextView)categorySpinner.getSelectedView();
                String result = categoryView.getText().toString();

                // get database
                db = FirebaseFirestore.getInstance();           // connect variable to firestore database



                // Add org to database
                //create org class
                Organizations new_org = new Organizations(name_edit.getText().toString(), description_edit.getText().toString(), "", "", location_edit.getText().toString(), "", categoryView.getText().toString());
                // create hash map for org
                Map<String, Object> org = new HashMap<>();
                org.put("name", new_org.getName());
                org.put("desc", new_org.getDesc());
                org.put("address", new_org.getCAddress());
                org.put("email", new_org.getCEmail());
                org.put("location", new_org.getLocation());
                org.put("pNumber", new_org.getCPhoneNumber());
                org.put("uid", auth.getCurrentUser().getUid());
                org.put("category", new_org.getCategory());

                // upload to db
                db.collection("Organizations").document(new_org.getName())
                        .set(org)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(CreateClubActivity.this, "Club has been created", Toast.LENGTH_SHORT).show();

                                // Go back to main page
                                finish();//finish current activity
                                Intent intent = new Intent(CreateClubActivity.this, MainActivity.class);
                                startActivity(intent);//start current activity again
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(CreateClubActivity.this, "Club creation failed, please try again.", Toast.LENGTH_SHORT).show();
                            }
                        });

                // create org in database
                // go to org edit page
            }
        });

    }

}
