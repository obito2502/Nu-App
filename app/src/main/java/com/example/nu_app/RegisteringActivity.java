package com.example.nu_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisteringActivity extends AppCompatActivity {

    private Button register_button;
    private EditText email_from_user;
    private EditText password_from_user;
    private EditText confirm_password_from_user;
    private Switch aSwitch;

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
        aSwitch = (Switch)findViewById(R.id.student_club_switch);
        progress = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        progress.setMessage("Registering...");

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean switchState = aSwitch.isChecked();

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
                    if (switchState == false) {
                        registerUser(email, password);
                    } else {
                        registerClub(email, password);
                    }

                }
            }
        });
    }

    private void registerClub(final  String email, String password) {
        progress.show();
        Intent registerClub = new Intent(RegisteringActivity.this, ClubRegisterActivity.class);
        registerClub.putExtra("email", email);
        registerClub.putExtra("password", password);
        startActivity(registerClub);
    }

    private void registerUser(final String email, String password) {
        progress.show();

        Intent registerStudent = new Intent(RegisteringActivity.this, StudentRegisterActivity.class);
        registerStudent.putExtra("email", email);
        registerStudent.putExtra("password", password);
        startActivity(registerStudent);
    }
}