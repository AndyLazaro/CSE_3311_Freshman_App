package com.example.cse_3311_freshman_app;

import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ClubProfileActivity {

    // Initialize Firebase authentification & database
    FirebaseAuth auth;
    FirebaseFirestore db;

    // Initialize activity parts
    // tags
    TextView location_tag;
    TextView email_tag;
    TextView pnumber_tag;
    TextView address_tag;
    TextView desc_tag;

    // Firebase Placeholders
    TextView club_profile_name;
    TextView location_info;
    TextView email_info;
    TextView pnumber_info;
    TextView address_info;
    TextView desc_info;



}
