package com.example.cse_3311_freshman_app;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class OpenEventActivity extends AppCompatActivity {

    Event event;
    TextView eName, eOrg, eDesc, eLoc, eTime; // layout's componenets
    ImageView img;
    FirebaseAuth auth;
    FirebaseFirestore db;
    Query q;
    Query q1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_page);
        this.event = (Event) getIntent().getSerializableExtra("EVENT"); // retreive event from intent 
        this.eName = findViewById(R.id.eventName); // attch var to components
        this.eOrg = findViewById(R.id.eventOrgName);
        this.eDesc = findViewById(R.id.eventDesc);
        this.eLoc = findViewById(R.id.eventLocation);
        this.eTime = findViewById(R.id.eventTime);
        this.img = findViewById(R.id.eventImage);
        populateEventPage();
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        q = db.collection("Events").whereEqualTo("e_name", event.e_name); // base query
        q1 = q.whereArrayContains("rsvp", auth.getCurrentUser().getUid()); // rsvp checker query
    }

    public void populateEventPage() {
        eName.setText(event.e_name); // inputs event's info into thw layout
        eOrg.setText(event.e_org);
        eDesc.setText(event.e_desc);
        eLoc.setText(event.e_location);
        eTime.setText(event.time);
        Glide.with(getApplicationContext()).load(event.getE_image()).apply(new RequestOptions().override(1000,1000)).into(img);
    }

    public void RSVP(View view) {
        q.get().addOnCompleteListener( qr -> { // query event from db that matches current event's name
            if (qr.isSuccessful()) {
                String docRef = ""; // temp documentReference val storage
                for (QueryDocumentSnapshot doc : qr.getResult()) { // for loop but only ever one item in list
                    docRef = doc.getId(); // iterate thru results and update docRef val
                }
                String finalDocRef = docRef; // final docRef val
                q1.get().addOnCompleteListener(r -> { // query doc and check if uid is present in array "rsvp"
                    if (r.isSuccessful()) {
                        QuerySnapshot res = r.getResult();
                        if (res.isEmpty()) { // if the result is empty, then add uid
                            db.collection("Events").document(finalDocRef)
                                .update("rsvp", FieldValue.arrayUnion(auth.getCurrentUser().getUid()))
                                .addOnCompleteListener(u -> {
                                    if (u.isSuccessful()) {
                                        Toast.makeText(OpenEventActivity.this, "Sent RSVP", Toast.LENGTH_SHORT).show(); // if all succesful, send toast
                                    }
                                });
                        } else { // if uid found, then remove uid
                            db.collection("Events").document(finalDocRef)
                                .update("rsvp", FieldValue.arrayRemove(auth.getCurrentUser().getUid()))
                                .addOnCompleteListener(u -> {
                                    if (u.isSuccessful()) {
                                        Toast.makeText(OpenEventActivity.this, "Unsent RSVP", Toast.LENGTH_SHORT).show(); // if all succesful, send toast
                                    }
                                });
                        }
                    }
                });
            }
        });
    }
}
