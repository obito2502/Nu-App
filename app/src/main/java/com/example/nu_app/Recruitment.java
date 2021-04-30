package com.example.nu_app;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nu_app.models.Application;
import com.example.nu_app.models.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.util.ArrayList;

public class Recruitment extends AppCompatActivity {
    private FirebaseUser pAuth;
    public DatabaseReference students;
    public DatabaseReference recruitmentRef;

    private FirebaseStorage storage;
    private StorageReference storageReference;

    private Button uploadEventPhoto;
    private ImageView eventPoster;

    private String randomKey;
    public Uri imageUri;
    private StorageTask uploadTask;

    public DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruitment);

        final ArrayList<String> clubNames = new ArrayList<>();

        final EditText name = findViewById(R.id.student_name);
        final EditText surname = findViewById(R.id.student_surname);
        final EditText contact = findViewById(R.id.contact);
        final EditText occupation = findViewById(R.id.occupation);
        final EditText motivation = findViewById(R.id.motivation);
        final EditText reason = findViewById(R.id.reason);

        final Spinner dropdown = findViewById(R.id.spinner1);
        Button saveBtn = findViewById(R.id.save_btn);

        pAuth = FirebaseAuth.getInstance().getCurrentUser();
        recruitmentRef = FirebaseDatabase.getInstance().getReference().child("applications");
        clubNames.add("Select Any");
        reference = FirebaseDatabase.getInstance().getReference("clubs");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.e("Count ", "" + snapshot.getChildrenCount());
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Club club = postSnapshot.getValue(Club.class);
                    clubNames.add(club.getName());
                }

                String clubNameArray[] = clubNames.toArray(new String[clubNames.size()]);
                Log.d("List ", clubNameArray.toString());
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Recruitment.this,
                        android.R.layout.simple_spinner_item, clubNameArray);

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                dropdown.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("The read failed: ", databaseError.toString());
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String userEmail = pAuth.getEmail();
                students = FirebaseDatabase.getInstance().getReference().child("students");

                students.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            Student userName = ds.getValue(Student.class);
                            if (userName.email.equals(userEmail)) {
                                saveApplication(name, surname, userEmail, dropdown, occupation, motivation, reason, contact);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    private void saveApplication(EditText name, EditText surname, String email, Spinner dropdown, EditText occupation, EditText motivation, EditText reason, EditText phone) {
        final String aname = name.getText().toString().trim();
        final String asurname = surname.getText().toString().trim();
        final String aclub = dropdown.getSelectedItem().toString().trim();
        final String aoccupation = occupation.getText().toString().trim();
        final String amotivation = motivation.getText().toString().trim();
        final String areason = reason.getText().toString().trim();
        final String aphone = phone.getText().toString().trim();

        if( aname!=null && asurname !=null  && !aclub.equals("Select Any") && aoccupation!=null &&amotivation!=null && areason!=null && aphone!=null )
        {
            Application newApp = new Application(aname, asurname, email, aclub, aoccupation, amotivation, areason, aphone);
            Log.d("Application ---->", newApp.toString());
            String id = recruitmentRef.push().getKey();
            recruitmentRef.child(id).setValue(newApp);
            Toast.makeText(Recruitment.this, "Your application was succesfully stored!!", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(Recruitment.this, "Select and fill in all fields!", Toast.LENGTH_SHORT ).show();

    }
}


