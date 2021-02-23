package com.example.nu_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StudentRegisterActivity extends AppCompatActivity {

    private TextView email;
    private TextView password;
    private Button registerButton;
    private EditText studentName;
    private EditText studentMajor;
    private ProgressDialog progress;
    private FirebaseAuth mAuth;

    public String login;

    private FirebaseDatabase db;
    public DatabaseReference student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_register);

        email = (TextView)findViewById(R.id.student_email);
        password = (TextView)findViewById(R.id.student_password);
        registerButton = (Button)findViewById(R.id.student_register);
        studentName = findViewById(R.id.student_name);
        studentMajor = findViewById(R.id.student_major);
        progress = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        final String email_of_student = getIntent().getStringExtra("email");
        final String password_of_student = getIntent().getStringExtra("password");

        email.setText(email_of_student);
        password.setText(password_of_student);

        login = email_of_student.replaceAll("\\.", "_");
        login = login.substring(0, login.indexOf("@"));

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = studentName.getText().toString().trim();
                final String major = studentMajor.getText().toString().trim();
                registerUser(email_of_student, password_of_student, name, major, login);
            }
        });
    }

    private void registerUser(final String email, final String password, final String name, final String major, final String login) {
        progress.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            db = FirebaseDatabase.getInstance();
                            student = db.getReference("students");

                            Student newStudent = new Student(email, name, major);

                            student.child(login).setValue(newStudent);

                            mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        progress.dismiss();
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Toast.makeText(StudentRegisterActivity.this, "Registering Done. Please, verify your email address", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(StudentRegisterActivity.this, LoginActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(StudentRegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                            // Sign in success, update UI with the signed-in user's information

                        } else {
                            // If sign in fails, display a message to the user.
                            System.out.println(task.isSuccessful());
                            Toast.makeText(StudentRegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progress.dismiss();
                Toast.makeText(StudentRegisterActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

