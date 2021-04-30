package com.example.nu_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.nu_app.adapters.ApplicationAdapter;
import com.example.nu_app.adapters.PostAdapter;
import com.example.nu_app.models.Application;
import com.example.nu_app.models.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ApplicationActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    private DatabaseReference databaseRef;
    private DatabaseReference clubRef;

    private RecyclerView recyclerView;
    private List<Application> applicationList;
    ApplicationAdapter applicationAdapter;
    private String login;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);
        recyclerView = findViewById(R.id.applicantsRecyclerview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        //set layout to recyclerview
        recyclerView.setLayoutManager(layoutManager);

        //init post list


        loadApplications();

    }


    private void loadApplications() {
        //path of all posts
        databaseRef = FirebaseDatabase.getInstance().getReference("applications");

        clubRef = FirebaseDatabase.getInstance().getReference("clubs");
        //get all data from this ref


        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();



        login = user.getEmail().replaceAll("\\.", "_");
        login = login.substring(0, login.indexOf("@"));



        clubRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    Club club = ds.getValue(Club.class);
                    String clubEmail = club.getEmail().replaceAll("\\.", "_");
                    clubEmail = clubEmail.substring(0, clubEmail.indexOf("@"));
                    if (clubEmail.equals(login)) {
                        login = club.getName();

                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                applicationList = new ArrayList<>();



                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    Application application = ds.getValue(Application.class);
                    Log.d("ANSWER", application.getClub());
                    Log.d("ANSWER2",login);
                    if (application.getClub().equals(login)) {
                        Log.d("LOGGIN", application.getClub());
                        applicationList.add(application);
                    }
                }

                applicationAdapter = new ApplicationAdapter(getApplicationContext() ,applicationList);
                recyclerView.setAdapter(applicationAdapter);


                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }


        });
    }
}