package com.example.cse_3311_freshman_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    EditText emailEt, passwordEt, confirm_passwordEt;
    Button registerBtn, loginBtn;
    CheckBox showPass;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    FirebaseFirestore database;
    User newUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailEt = findViewById(R.id.register_email_et);
        passwordEt = findViewById(R.id.register_password_et);
        confirm_passwordEt = findViewById(R.id.register_confirm_password_et);
        showPass = findViewById(R.id.register_showpass);
        registerBtn = findViewById(R.id.button_register);
        loginBtn = findViewById(R.id.signup_to_login);
        progressBar = findViewById(R.id.progressbar_register);
        mAuth = FirebaseAuth.getInstance();


        showPass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                {
                    passwordEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    confirm_passwordEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else
                {
                    passwordEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    confirm_passwordEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        //Register button handles input and creates user
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEt.getText().toString();
                //Check to see if email address is from UTA only
//                if (!email.contains("@mavs.uta.edu") && !email.contains("@uta.edu"))
//                {
//                    email = "badEmail";
//                }
                String password = passwordEt.getText().toString();
                String confirm_password = confirm_passwordEt.getText().toString();

                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)
                        && !TextUtils.isEmpty(confirm_password))
                {
                    if (password.equals(confirm_password))
                    {
                        progressBar.setVisibility(View.VISIBLE);
                        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful())
                                {
                                    //Create user in firestore database
                                    database = FirebaseFirestore.getInstance();
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    String UID = user.getUid();
                                    User newUser = new User("none", "none");
                                    DocumentReference documentReference = database.collection("Users").document(UID);
                                    documentReference.set(newUser);
                                    //Send verify email to email address, must be verified in order to log in to app
                                    mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(RegisterActivity.this, "Please Verify Email Sent", Toast.LENGTH_SHORT).show();
                                            mAuth.signOut();
                                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                            finish();
                                        }
                                    });
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                                else
                                {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    String error = task.getException().getMessage();
                                    Toast.makeText(RegisterActivity.this, "Error  :"+error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else
                    {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(RegisterActivity.this, "Passwords Do Not Match", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(RegisterActivity.this, "Please Fill Out All Fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Login button moves to login page
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void moveToMain()
    {
        Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
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