package com.example.cse_3311_freshman_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
<<<<<<< Updated upstream

public class PostActivity extends AppCompatActivity {
    Button backBtn;
=======
import android.widget.ImageView;
import android.widget.EditText;
import android.net.Uri;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.database.ChildEventListener;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;

public class PostActivity extends AppCompatActivity {
    Button post_backBtn, post_attachBtn, post_postBtn;
    private ImageView captureImage;
    EditText post_des, post_location, post_name, post_org;
    int SELECT_PICTURE = 200;

    FirebaseFirestore db_base;
    CollectionReference db_ref;
>>>>>>> Stashed changes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
<<<<<<< Updated upstream
=======

        db_base = FirebaseFirestore.getInstance();
        db_ref = db_base.collection("Events");//get to Events

>>>>>>> Stashed changes
        setContentView(R.layout.activity_post);
        backBtn = findViewById(R.id.button_back);

<<<<<<< Updated upstream
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PostActivity.this, MainActivity.class);
                finish();//close post activity
=======
        post_backBtn = findViewById(R.id.button_back);
        post_attachBtn = findViewById(R.id.button_attach);
        post_postBtn = findViewById(R.id.button_post);
        captureImage = findViewById(R.id.my_image);
        post_des = findViewById(R.id.post_des);//description
        post_location = findViewById(R.id.post_location);
        post_name = findViewById(R.id.post_name);
        post_org = findViewById(R.id.post_org);

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
                String name = post_name.getEditableText().toString();
                String org = post_org.getEditableText().toString();
                String description = post_des.getEditableText().toString();
                String location = post_location.getEditableText().toString();
                Timestamp timestamp = Timestamp.now();

                //String img_data = getImgString(captureImage);
                //test sample
                String img_data = "https://www.celebsmoviejackets.com/image/cache/catalog/Tunnel%20Snakes%20Jacket/tunnel-snake-jacket-800x800.jpg";

                Event post_event = new Event(name, org, description, location, timestamp, img_data);
                db_ref.add(post_event);

                //-------------Close post window after posting event----------------
                finish();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
>>>>>>> Stashed changes
                startActivity(intent);
            }
        });
    }
<<<<<<< Updated upstream
=======

    private String getImgString(ImageView img){//convert image to string format
        String img_string = null;
        Bitmap bmp = null;
        ByteArrayOutputStream bos = null;
        byte[] bt = null;

        try{
            img.buildDrawingCache();
            bmp = img.getDrawingCache();//convert img to bitmap
            bos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bt = bos.toByteArray();
            img_string = Base64.encodeToString(bt, Base64.DEFAULT);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return img_string;
    }

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
>>>>>>> Stashed changes
}