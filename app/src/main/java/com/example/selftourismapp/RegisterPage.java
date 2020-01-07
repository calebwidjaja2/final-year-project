
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

public class RegisterPage extends AppCompatActivity {
    EditText mFullName, mEmail, mPassword;
    Button mRegisterButton;
    TextView mSignIn;
    FirebaseAuth firebaseAuth;
    ProgressBar mLoadingBarRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        mFullName = findViewById(R.id.enterFullName);
        mEmail = findViewById(R.id.enterEmail);
        mPassword = findViewById(R.id.enterPassword);
        mRegisterButton = findViewById(R.id.registerButton);
        mSignIn = findViewById(R.id.toSignIn);

        firebaseAuth = FirebaseAuth.getInstance();
        mLoadingBarRegister = findViewById(R.id.loadingBarRegister);


        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Please Enter The Email");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Please Enter The Password");
                    return;
                }

                if(password.length() < 6){
                    mPassword.setError("Password Must Be More Than 6 Characters");
                    return;
                }

                mLoadingBarRegister.setVisibility(View.VISIBLE);

                // register the user to the firebase databse

                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(RegisterPage.this, "User Created", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else{
                            Toast.makeText(RegisterPage.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent (RegisterPage.this, LoginPage.class);
                startActivity(i);
            }
        });

    }
}
