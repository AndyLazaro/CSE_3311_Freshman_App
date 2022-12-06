package com.example.cse_3311_freshman_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText emailEt;
    Button forgotPassBtn, loginBtn;
    ProgressBar progressBar;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailEt = findViewById(R.id.forgotPass_email_et);
        loginBtn = findViewById(R.id.forgotPass_to_login);
        forgotPassBtn = findViewById(R.id.button_forgotPass);
        progressBar = findViewById(R.id.progressbar_register);
        mAuth = FirebaseAuth.getInstance();

        //Forgot Password button handles input and sends reset email to string given
        forgotPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEt.getText().toString();
                //Check to see if email address is from UTA only
//                if (!email.contains("@mavs.uta.edu") && !email.contains("@uta.edu"))
//                {
//                    email = "badEmail";
//                }

                if (!TextUtils.isEmpty(email))
                {
                    progressBar.setVisibility(View.VISIBLE);
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ForgotPasswordActivity.this, "Password Reset Email Sent", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                                finish();
                            }
                            else
                            {
                                progressBar.setVisibility(View.INVISIBLE);
                                String error = task.getException().getMessage();
                                Toast.makeText(ForgotPasswordActivity.this, "Error  :"+error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    progressBar.setVisibility(View.INVISIBLE);
                }
                else
                {
                    Toast.makeText(ForgotPasswordActivity.this, "Please Fill Out Email Field", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Login button moves to login page
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ForgotPasswordActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void moveToMain()
    {
        Intent intent = new Intent(ForgotPasswordActivity.this,MainActivity.class);
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