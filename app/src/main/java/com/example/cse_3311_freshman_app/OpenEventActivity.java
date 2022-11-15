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
    TextView eName, eOrg, eDesc, eLoc, eTime;
    ImageView img;
    FirebaseAuth auth;
    FirebaseFirestore db;
    Query q;
    Query q1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_page);
        this.event = (Event) getIntent().getSerializableExtra("EVENT");
        this.eName = findViewById(R.id.eventName);
        this.eOrg = findViewById(R.id.eventOrgName);
        this.eDesc = findViewById(R.id.eventDesc);
        this.eLoc = findViewById(R.id.eventLocation);
        this.eTime = findViewById(R.id.eventTime);
        this.img = findViewById(R.id.eventImage);
        populateEventPage();
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        q = db.collection("Events").whereEqualTo("e_name", event.e_name);
        q1 = q.whereArrayContains("rsvp", auth.getCurrentUser().getUid());
    }

    public void populateEventPage() {
        eName.setText(event.e_name);
        //eOrg.setText(event.e_org);
        eDesc.setText(event.e_desc);
        eLoc.setText(event.e_location);
        eTime.setText(event.time);
        Glide.with(getApplicationContext()).load(event.getE_image()).apply(new RequestOptions().override(1000,1000)).into(img);
    }

    public void RSVP(View view) {
        q.get().addOnCompleteListener( qr -> {
            if (qr.isSuccessful()) {
                String docRef = "";
                for (QueryDocumentSnapshot doc : qr.getResult()) {
                    docRef = doc.getId();
                }
                String finalDocRef = docRef;
                q1.get().addOnCompleteListener(r -> {
                    if (r.isSuccessful()) {
                        QuerySnapshot res = r.getResult();
                        if (res.isEmpty()) {
                            db.collection("Events").document(finalDocRef)
                                .update("rsvp", FieldValue.arrayUnion(auth.getCurrentUser().getUid()))
                                .addOnCompleteListener(u -> {
                                    if (u.isSuccessful()) {
                                        Toast.makeText(OpenEventActivity.this, "Sent RSVP", Toast.LENGTH_SHORT).show();
                                    }
                                });
                        } else {
                            db.collection("Events").document(finalDocRef)
                                .update("rsvp", FieldValue.arrayRemove(auth.getCurrentUser().getUid()))
                                .addOnCompleteListener(u -> {
                                    if (u.isSuccessful()) {
                                        Toast.makeText(OpenEventActivity.this, "Unsent RSVP", Toast.LENGTH_SHORT).show();
                                    }
                                });
                        }
                    }
                });
            }
        });
    }
}
