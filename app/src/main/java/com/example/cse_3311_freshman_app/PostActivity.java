package com.example.cse_3311_freshman_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PostActivity extends AppCompatActivity {
    Button post_backBtn, post_attachBtn, post_postBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        post_backBtn = findViewById(R.id.button_back);
        post_attachBtn = findViewById(R.id.button_attach);
        post_postBtn = findViewById(R.id.button_post);

        post_backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PostActivity.this, MainActivity.class);
                finish();//close post activity
                startActivity(intent);
            }
        });
    }
}