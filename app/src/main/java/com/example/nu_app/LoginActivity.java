package com.example.nu_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private Button register_button;
    private EditText email_from_user;
    private EditText password_from_user;
    private Button login_button;

    private FirebaseAuth mAuth;

    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        progress = new ProgressDialog(this);
        progress.setMessage("Logging In...");

        register_button = (Button)findViewById(R.id.register_button);
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRegisteringActivity();
            }
        });

        login_button = (Button)findViewById(R.id.sign_in_button);

        email_from_user = findViewById((R.id.email_text));
        password_from_user = findViewById((R.id.password_text));

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = email_from_user.getText().toString();
                email = email + "@nu.edu.kz";

                String password = password_from_user.getText().toString();

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    email_from_user.setError("Wrong Email!");
                    email_from_user.setFocusable(true);
                } else {
                    loginUser(email, password);
                }
            }
        });
    }

    private void loginUser(String email, String password) {
        progress.show();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if(user.isEmailVerified()) {
                                progress.dismiss();
                                // Sign in success, update UI with the signed-in user's information
                                startActivity(new Intent(LoginActivity.this, HomePage.class));
                                finish();
                            } else {
                                progress.dismiss();
                                Toast.makeText(LoginActivity.this, "Please, verify your email address", Toast.LENGTH_SHORT).show();
                            }


                        } else {
                            progress.dismiss();
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progress.dismiss();
                Toast.makeText(LoginActivity.this, e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void openRegisteringActivity() {
        Intent intent = new Intent(this, RegisteringActivity.class);
        startActivity(intent);
    }
}