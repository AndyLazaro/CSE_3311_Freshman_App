package com.example.cse_3311_freshman_app;
// https://www.geeksforgeeks.org/how-to-select-an-image-from-gallery-in-android/

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.EditText;
import android.net.Uri;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class PostActivity extends AppCompatActivity {
    Button post_backBtn, post_attachBtn, post_postBtn;
    private ImageView captureImage;
    EditText post_des, post_location, post_name, post_org, post_date, post_time;
    Calendar dateCal, timeCal, dateTimeCal;
    int SELECT_PICTURE = 200;

    String image_URL;
    String name;
    String org;
    String description;
    String location;
    Timestamp timestamp;

    FirebaseFirestore db_base;
    CollectionReference db_ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db_base = FirebaseFirestore.getInstance();
        db_ref = db_base.collection("Events");

        setContentView(R.layout.activity_post);

        post_backBtn = findViewById(R.id.button_back);
        post_attachBtn = findViewById(R.id.button_attach);
        post_postBtn = findViewById(R.id.button_post);
        captureImage = findViewById(R.id.my_image);
        post_des = findViewById(R.id.post_des);//description
        post_location = findViewById(R.id.post_location);
        post_name = findViewById(R.id.post_name);
        post_org = findViewById(R.id.post_org);
        post_date = findViewById(R.id.post_date);
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
            public void onClick(View view) {
                String image_name = "";
                imageChooser();
            }
        });

        //------------------Unfinished-----------------------------------
        post_postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                //post content
                name = post_name.getEditableText().toString();
                org = post_org.getEditableText().toString();
                description = post_des.getEditableText().toString();
                location = post_location.getEditableText().toString();

                //String img_data = getImgString(captureImage);
                //timestamp = Timestamp.now();

                // upload image
                upload_image();

                String img_data = "https://firebasestorage.googleapis.com/v0/b/freshmen-app.appspot.com/o/images%2Frefresh-the-day-lemonade-stand-and-banner.jpg?alt=media&token=0cfda8e5-cb6c-4596-9356-dba0df993537";
            }
        });
    }

    public void getDate (View v)
    {
        dateCal = Calendar.getInstance();

        int year = dateCal.get(Calendar.YEAR);
        int month = dateCal.get(Calendar.MONTH);
        int day = dateCal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        post_date.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);

                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    public void getTime (View v)
    {
        timeCal = Calendar.getInstance();

        int hour = timeCal.get(Calendar.HOUR_OF_DAY);
        int minute = timeCal.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        String ampm = (hourOfDay>11 || hourOfDay==0) ? "PM" : "AM";
                        if (hourOfDay>12) {hourOfDay-=12;}
                        String min = (minute<10) ? "0" + minute : String.valueOf(minute);
                        post_time.setText(hourOfDay + ":" + min + " " + ampm);
                    }
                }, hour, minute, false);
        timePickerDialog.show();
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

    public void upload_image() {
        String image_name = generate_file_name();
        // Get storage reference to specific image
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        // Create a reference to 'images/mountains.jpg'
        final StorageReference image_path_ref = storageRef.child(image_name);

        // Get image bit map
        // Get the data from an ImageView as bytes
        if (captureImage != null) {
            captureImage.setDrawingCacheEnabled(true);
            captureImage.buildDrawingCache();
            Bitmap bitmap = ((BitmapDrawable) captureImage.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = image_path_ref.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    Toast.makeText(PostActivity.this, "Image upload processing.", Toast.LENGTH_SHORT).show();
                }
            });

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return image_path_ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        image_URL = downloadUri.toString();
                        Toast.makeText(PostActivity.this, "Image upload success.", Toast.LENGTH_SHORT).show();

                        int year = dateCal.get(Calendar.YEAR);
                        int month = dateCal.get(Calendar.MONTH);
                        int day = dateCal.get(Calendar.DAY_OF_MONTH);
                        int hour = timeCal.get(Calendar.HOUR_OF_DAY);
                        int minute = timeCal.get(Calendar.MINUTE);
                        dateTimeCal = Calendar.getInstance();
                        dateTimeCal.set(year, month, day, hour, minute);
                        Date date = dateTimeCal.getTime();
                        Timestamp timestamp = new Timestamp(date);

                        // complete post
                        Event post_event = new Event(name, org, description, location, timestamp, image_URL);
                        db_ref.add(post_event);//post the event data to database

                        //-------------Close post window after posting event----------------
                        finish();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(PostActivity.this, "Image upload failed. Try again", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    protected String generate_file_name() {
        String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder name = new StringBuilder();
        Random rnd = new Random();
        while (name.length() < 20) { // length of the random string.
            int index = (int) (rnd.nextFloat() * CHARS.length());
            name.append(CHARS.charAt(index));
        }
        return name.toString();
    }
}