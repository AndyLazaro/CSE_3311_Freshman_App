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

import com.example.cse_3311_freshman_app.MainActivity;
import com.example.cse_3311_freshman_app.Organizations;
import com.example.cse_3311_freshman_app.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;

public class edit_club extends AppCompatActivity
{
    FirebaseAuth auth;              // variable giving us authorization in firebase
    FirebaseFirestore db;           // variable to hold the firestore database in firebases

    Organizations org;              // Org to be editted

    public void onCreate(Bundle savedInstanceState)
    {
        auth = FirebaseAuth.getInstance();              // connect variable to firebase

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_club);

        this.org = (Organizations) getIntent().getSerializableExtra("org");

        // Set spinner info
        Spinner categorySpinner = findViewById(R.id.spinner_category);
        ArrayAdapter<CharSequence>adapter= ArrayAdapter.createFromResource(this, R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        // Get all text fields
        EditText name_edit =   (EditText)findViewById(R.id.club_name);
        EditText location_edit =   (EditText)findViewById(R.id.club_location);
        EditText description_edit =   (EditText)findViewById(R.id.club_description);

        // Populate edits with current text
        name_edit.setText(org.name);
        location_edit.setText(org.location);
        description_edit.setText(org.desc);

        // Connect submit button
        final Button submit_button = findViewById(R.id.submitt_butt);
        submit_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // get database
                db = FirebaseFirestore.getInstance();           // connect variable to firestore database

                // Make spinner text
                TextView categoryView = (TextView)categorySpinner.getSelectedView();

                // Make changes to database
                //Change org class
               // org.setAddress(location_edit.getText().toString()));
                org.setName(name_edit.getText().toString());
                org.setDesc(description_edit.getText().toString());
                //org.setEmail(email_edit.getText().toString());
                org.setLocation(location_edit.getText().toString());
                //org.setPNumber(pNumber_edit.getText().toString());
                //org.setUid();
                org.setCategory(categoryView.getText().toString());


                // create hash map for org
                Map<String, Object> org_hash = new HashMap<>();
                org_hash.put("desc", org.getDesc());
                org_hash.put("address", org.getAddress());
                org_hash.put("email", org.getEmail());
                org_hash.put("location", org.getLocation());
                org_hash.put("pNumber", org.getPNumber());
                org_hash.put("uid", auth.getCurrentUser().getUid());         // Send user ID for verification
                org_hash.put("category", org.getCategory());

                // upload to db
                db.collection("Organizations").document(org.getName())
                        .set(org_hash)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(com.example.cse_3311_freshman_app.edit_club.this, "Club has been created", Toast.LENGTH_SHORT).show();

                                // Go back to main page
                                finish();//finish current activity
                                Intent intent = new Intent(com.example.cse_3311_freshman_app.edit_club.this, MainActivity.class);
                                startActivity(intent);//start current activity again
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(com.example.cse_3311_freshman_app.edit_club.this, "Club creation failed, please try again.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

    }
}