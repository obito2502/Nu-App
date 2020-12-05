package com.example.nu_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.icu.text.Edits;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ListOfClubs extends AppCompatActivity {

    private ListView listView;
    private ArrayList<String> listOfClubs;

    private ArrayAdapter<String> listOfClubsAdapter;

    public DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_clubs);

        listView = (ListView)findViewById(R.id.list_of_clubs);

        listOfClubs = new ArrayList<String>();

//        listOfClubsAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, listOfClubs);
//        listView.setAdapter(listOfClubsAdapter);

        reference = FirebaseDatabase.getInstance().getReference("clubs");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                   Club club = childSnapshot.getValue(Club.class);
                   System.out.println(club.getName());
                   listOfClubs.add(club.getName());
                }

                listOfClubsAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, listOfClubs);
                listView.setAdapter(listOfClubsAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}