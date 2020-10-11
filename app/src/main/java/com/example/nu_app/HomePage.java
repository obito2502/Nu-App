package com.example.nu_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomePage extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    TextView user_email_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        firebaseAuth = FirebaseAuth.getInstance();
        user_email_address = findViewById(R.id.user_email);
    }

    private void checkUserStatus() {
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null) {
            user_email_address.setText(user.getEmail());
        } else {
            startActivity(new Intent(HomePage.this, LoginActivity.class));
            finish();
        }
    }

    protected void onStart() {
        checkUserStatus();
        super.onStart();
    }

}