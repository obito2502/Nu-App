package com.example.nu_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.nu_app.adapters.ApplicationAdapter;
import com.example.nu_app.adapters.PostAdapter;
import com.example.nu_app.models.Application;
import com.example.nu_app.models.Post;
import com.google.firebase.auth.FirebaseAuth;
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

    private RecyclerView recyclerView;
    private List<Application> applicationList;
    ApplicationAdapter applicationAdapter;


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
        databaseRef = FirebaseDatabase.getInstance().getReference("posts");

        //get all data from this ref
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                applicationList = new ArrayList<>();

                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    Application application = ds.getValue(Application.class);
                    applicationList.add(application);
                }

//                applicationAdapter = new ApplicationAdapter(activity, applicationList) ;
//                System.out.println(applicationList.toString());
//                recyclerView.setAdapter(applicationList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //error

//                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}