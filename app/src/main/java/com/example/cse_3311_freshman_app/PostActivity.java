package com.example.cse_3311_freshman_app;
// https://www.geeksforgeeks.org/how-to-select-an-image-from-gallery-in-android/

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.TextView;
import android.net.Uri;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.Spinner;
import android.widget.DatePicker;
import android.app.DatePickerDialog;
import android.util.Log;
import android.app.AlertDialog;

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
import java.util.Calendar;
import java.text.SimpleDateFormat;

public class PostActivity extends AppCompatActivity {
    private static final String TAG = "PostActivity";
    Button post_backBtn, post_attachBtn, post_postBtn;
    private ImageView captureImage;
    EditText post_des, post_location, post_name, post_org;
    int SELECT_PICTURE = 200;
    private DatePickerDialog datePickerDialog;
    private Button dateButton;

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
        initDatePicker();
        dateButton = findViewById(R.id.button_date);
        dateButton.setText(getTodaysDate());

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
                //String img_data = getImgString(captureImage);
                Timestamp timestamp = Timestamp.now();
                String img_data = "https://resources.uta.edu/advancement/identity/_images/new-logos/a-logo-wordmark.jpg";

                Event post_event = new Event(name, org, description, location, timestamp, img_data);
                db_ref.add(post_event);//post the event data to database

                //-------------Close post window after posting event----------------
                finish();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

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

    private String getTodaysDate()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateButton.setText(date);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    private String makeDateString(int day, int month, int year)
    {
        //get time_int
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month)
    {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";

        //default should never happen
        return "JAN";
    }

    public void openDatePicker(View view)
    {
        datePickerDialog.show();
    }
}