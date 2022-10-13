package com.example.cse_3311_freshman_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText emailEt, passwordEt;
    CheckBox showPass;
    Button registerBtn, loginBtn;
    TextView forgotPassBtn;
    ProgressBar progressBar;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEt = findViewById(R.id.login_email_et);
        passwordEt = findViewById(R.id.login_password_et);
        showPass = findViewById(R.id.login_showpass);
        registerBtn = findViewById(R.id.login_to_signup);
        loginBtn = findViewById(R.id.button_login);
        forgotPassBtn = findViewById(R.id.login_to_forgotPass);
        progressBar = findViewById(R.id.progressbar_login);
        mAuth = FirebaseAuth.getInstance();

        showPass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                {
                    passwordEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else
                {
                    passwordEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        //Register button moves to register page
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        //Forgot password button moves to forgot password page
        forgotPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,ForgotPasswordActivity.class);
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
                               //moveToMain();
                               checkIfEmailVerified();
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
    }

    //Only allow verified emails into the application
    private void checkIfEmailVerified()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user.isEmailVerified())
        {
            moveToMain();
        }
        else
        {
            Toast.makeText(LoginActivity.this, "Email Not Verified", Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();
        }
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