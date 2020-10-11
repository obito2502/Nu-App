package com.example.nu_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class RegisteringActivity extends AppCompatActivity {

    private Button register_button;
    private EditText email;
    private EditText password;
    private EditText confirm_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registering);

        email = findViewById(R.id.email_text);
        password = findViewById(R.id.password_text);
        confirm_password = findViewById(R.id.confirm_password_text);
        register_button = (Button)findViewById(R.id.register_button);


    }
}