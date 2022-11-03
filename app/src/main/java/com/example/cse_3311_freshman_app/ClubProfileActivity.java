package com.example.cse_3311_freshman_app;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ClubProfileActivity extends AppCompatActivity {

    ////////////////////////////////////////////////////////////////////////////////
    // Initialize Firebase authentification & database
    FirebaseAuth auth;
    FirebaseFirestore db;

    // Initialize activity parts
    // tags
    /*
    TextView locationTag;
    TextView emailTag;
    TextView pnumberTag;
    TextView addressTag;
    TextView descTag;
    */

    // Button
    Button followButton;

    // Firebase Placeholders
    TextView clubName;
    TextView locationInfo;
    TextView emailInfo;
    TextView pnumberInfo;
    TextView addressInfo;
    TextView descInfo;
    ImageView clubAvatar;

    ///////////////////////////////////////////////////////////////////////////////
    // On creation of the page
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /////////////////////////////////////////
        // XML File this java file is attached to
        setContentView(R.layout.activity_club_profile);

        ///////////////////////////////////////
        // Declare the Buttons and Placeholders
        followButton = findViewById(R.id.follow_club_button);
        clubName = findViewById(R.id.club_profile_name);
        locationInfo = findViewById(R.id.location_info);
        emailInfo = findViewById(R.id.email_info);
        pnumberInfo = findViewById(R.id.pnumber_info);
        addressInfo = findViewById(R.id.address_info);
        descInfo = findViewById(R.id.description_info);
        clubAvatar = findViewById(R.id.club_avatar);


    }
}
