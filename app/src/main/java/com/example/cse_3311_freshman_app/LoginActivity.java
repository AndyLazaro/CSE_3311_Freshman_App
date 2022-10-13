package com.example.cse_3311_freshman_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText emailEt, passwordEt;
    Button registerBtn, loginBtn, testBtn;
    ProgressBar progressBar;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEt = findViewById(R.id.login_email_et);
        passwordEt = findViewById(R.id.login_password_et);
        registerBtn = findViewById(R.id.login_to_signup);
        loginBtn = findViewById(R.id.button_login);
        progressBar = findViewById(R.id.progressbar_login);
        testBtn = findViewById(R.id.button_test);
        mAuth = FirebaseAuth.getInstance();

        //Register button moves to register page
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        //Login button handles input and validates if user exists in database to go to main
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEt.getText().toString();
                String password = passwordEt.getText().toString();
                if(!TextUtils.isEmpty(email) && (!TextUtils.isEmpty(password)))
                {
                    progressBar.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                           if (task.isSuccessful())
                           {
                               moveToMain();
                               progressBar.setVisibility(View.INVISIBLE);
                           }
                           else
                           {
                               progressBar.setVisibility(View.INVISIBLE);
                               String error = task.getException().getMessage();
                               Toast.makeText(LoginActivity.this, "Error  :"+error, Toast.LENGTH_SHORT).show();
                           }
                        }
                    });
                }
                else
                {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(LoginActivity.this, "Fill Out All Fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //---------Admin app testing button. Delete in user version-------
        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                //finish();
                startActivity(intent);
            }
        });
        //-----------------------------------------------------------------
    }

    private void moveToMain()
    {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Check to see if there is a current user logged in, if so, go to main page
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null)
        {
            moveToMain();
        }
    }
}