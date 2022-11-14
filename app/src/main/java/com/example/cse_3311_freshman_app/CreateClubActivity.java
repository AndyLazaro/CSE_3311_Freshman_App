package com.example.cse_3311_freshman_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;

public class CreateClubActivity extends AppCompatActivity {
    FirebaseAuth auth;              // variable giving us authorization in firebase
    FirebaseFirestore db;           // variable to hold the firestore database in firebases
    int SELECT_PICTURE = 200;
    Button BSelectImage;
    ImageView IVPreviewImage;



    public void onCreate(Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();              // connect variable to firebase

        BSelectImage = findViewById(R.id.image_select);
        IVPreviewImage = findViewById(R.id.club_avatar);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_club);

        Spinner categorySpinner = findViewById(R.id.spinner_category);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        // Image selection
        final Button image_button = findViewById(R.id.image_select);
        image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image_chooser();
            }
        });


        final Button submit_button = findViewById(R.id.submitt_butt);
        submit_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText name_edit = (EditText) findViewById(R.id.club_name);
                EditText location_edit = (EditText) findViewById(R.id.club_location);
                db = FirebaseFirestore.getInstance();           // connect variable to firestore database


                // Add org to database
                //create org class
                Organizations new_org = new Organizations(name_edit.getText().toString(), "", "", "", location_edit.getText().toString(), "");
                // check org against database (org contact)
                // create hash map for org
                Map<String, Object> org = new HashMap<>();
                org.put("name", new_org.getName());
                org.put("desc", new_org.getDesc());
                org.put("address", new_org.getCAddress());
                org.put("email", new_org.getCEmail());
                org.put("location", new_org.getLocation());
                org.put("Phone", new_org.getCPhoneNumber());
                org.put("uid", auth.getCurrentUser().getUid());

                /*Map<String, Object> org_roles = new HashMap<>();
                org_roles.put("")*/

                db.collection("Organizations").document(new_org.getName())
                        .set(org)
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

    void image_chooser() {
        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        //TODO: figure out how to upload image to firebase

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    IVPreviewImage.setImageURI(selectedImageUri);
                }
            }
        }

    }
}
