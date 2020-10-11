package com.example.nu_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisteringActivity extends AppCompatActivity {

    private Button register_button;
    private EditText email_from_user;
    private EditText password_from_user;
    private EditText confirm_password_from_user;

    private ProgressDialog progress;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registering);

        email_from_user = findViewById(R.id.email_text);
        password_from_user = findViewById(R.id.password_text);
        confirm_password_from_user = findViewById(R.id.confirm_password_text);
        register_button = (Button)findViewById(R.id.register_button);
        progress = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        progress.setMessage("Registering...");

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = email_from_user.getText().toString().trim();
                email = email + "@nu.edu.kz";

                String password = password_from_user.getText().toString().trim();
                String confirm_password = confirm_password_from_user.getText().toString().trim();

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    email_from_user.setError("Wrong email");
                    email_from_user.setFocusable(true);
                } else if(password.length() < 6) {
                    password_from_user.setError("Password should be at least 6 characters");
                    password_from_user.setFocusable(true);
                } else if (!password.equals(confirm_password)) {
                    confirm_password_from_user.setError("Passwords do not match");
                    confirm_password_from_user.setFocusable(true);
                } else {
                    registerUser(email, password);
                }
            }
        });
    }

    private void registerUser(final String email, String password) {
        progress.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        progress.dismiss();
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Toast.makeText(RegisteringActivity.this, "Registering Done. Please, verify your email address", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(RegisteringActivity.this, LoginActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(RegisteringActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                            // Sign in success, update UI with the signed-in user's information

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(RegisteringActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progress.dismiss();
                Toast.makeText(RegisteringActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
            }
        });

    }
}