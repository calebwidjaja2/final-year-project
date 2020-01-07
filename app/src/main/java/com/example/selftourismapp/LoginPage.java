package com.example.selftourismapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginPage extends AppCompatActivity {
    EditText mUserEmail,mUserPassword;
    Button mSignInButton;
    TextView mtoSignUp;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        mUserEmail = findViewById(R.id.userEmail);
        mUserPassword = findViewById(R.id.userPassword);
        progressBar = findViewById(R.id.progressBarSignIn);
        firebaseAuth = FirebaseAuth.getInstance();
        mSignInButton = findViewById(R.id.signinButton);
        mtoSignUp = findViewById(R.id.toSignUp);


        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mUserEmail.getText().toString().trim();
                String password = mUserPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    mUserEmail.setError("Email is Required");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    mUserPassword.setError("Password is Required.");
                    return;
                }

                if(password.length() < 6){
                    mUserPassword.setError("Password Must Be More Than 6 Characters");
                    return;

                }

                progressBar.setVisibility(View.VISIBLE);

                // authenticate the user's account

                firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText (LoginPage.this, "Logged In", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }else {
                            Toast.makeText(LoginPage.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        mtoSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),RegisterPage.class));
            }
        });
    }
}
