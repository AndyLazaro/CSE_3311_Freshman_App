package com.example.cse_3311_freshman_app;
// https://www.geeksforgeeks.org/how-to-select-an-image-from-gallery-in-android/

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.EditText;
import android.net.Uri;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.database.ChildEventListener;
import org.checkerframework.checker.nullness.qual.NonNull;

public class PostActivity extends AppCompatActivity {
    Button post_backBtn, post_attachBtn, post_postBtn;
    private ImageView captureImage;
    EditText post_des, post_location, post_name, post_org, post_time;
    String img_data;
    int SELECT_PICTURE = 200;

    FirebaseDatabase db_base;
    DatabaseReference db_ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db_base = FirebaseDatabase.getInstance();
        db_ref = db_base.getReference("Events");

        setContentView(R.layout.activity_post);

        post_backBtn = findViewById(R.id.button_back);
        post_attachBtn = findViewById(R.id.button_attach);
        post_postBtn = findViewById(R.id.button_post);
        captureImage = findViewById(R.id.my_image);
        post_des = findViewById(R.id.post_des);//description
        post_location = findViewById(R.id.post_location);
        post_name = findViewById(R.id.post_name);
        post_org = findViewById(R.id.post_org);
        post_time = findViewById(R.id.post_time);

        post_backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        post_attachBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                imageChooser();
            }
        });

        //------------------Unfinished-----------------------------------
        post_postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                //post content
                //img_data = getImgString();
                String description = post_des.getEditableText().toString();
                String location = post_location.getEditableText().toString();
                String name = post_name.getEditableText().toString();
                String org = post_org.getEditableText().toString();
                //String time = post_time.getEditableText().toString();//time is timestamp

                //Event(name, org, description, location, time, img_data);
            }
        });
    }

    //private String getImgString(){}//complete Img -> string converison


    void imageChooser()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK)
        {
            if(requestCode==SELECT_PICTURE)
            {
                Uri selectedImageUri = data.getData();
                if(null!=selectedImageUri)
                {
                    captureImage.setImageURI(selectedImageUri);
                }
            }
        }
    }
}