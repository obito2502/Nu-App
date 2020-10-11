package com.example.nu_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    private Button register_button;
    private EditText email;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        register_button = (Button)findViewById(R.id.register_button);
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRegisteringActivity();
            }
        });

        email = findViewById((R.id.email_text));
        password = findViewById((R.id.password_text));


    }

    public void openRegisteringActivity() {
        Intent intent = new Intent(this, RegisteringActivity.class);
        startActivity(intent);
    }
}