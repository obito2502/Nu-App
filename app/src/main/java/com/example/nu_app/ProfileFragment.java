package com.example.nu_app;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    private TextView email_user;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private Button logout;
    private TextView condition;
    private TextView textOption;

    private String login;
    String parentChecker;

    private FirebaseDatabase db;
    public DatabaseReference reference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        condition = view.findViewById(R.id.textCondition);
        textOption = view.findViewById(R.id.textOption);

        email_user = view.findViewById(R.id.email_text);
        email_user.setText(user.getEmail());

        logout = view.findViewById(R.id.logout_button);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });

        final Button add_club_button = (Button)view.findViewById(R.id.add_club_button);
        login = user.getEmail().replaceAll("\\.", "_");
        login = login.substring(0, login.indexOf("@"));

        reference = FirebaseDatabase.getInstance().getReference();

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    if(childSnapshot.hasChild(login)){
                        if(childSnapshot.getKey().equals("students")) {
                            add_club_button.setVisibility(View.VISIBLE);
                            condition.setText("I Am Student");
                            textOption.setText("My subscriptions: adilya the best, kvn, chess");
                        } else if (childSnapshot.getKey().equals("clubs")){
                            condition.setText("I am Club");
                            textOption.setText("number of followers: 1");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        add_club_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addClub = new Intent(getActivity(), ListOfClubs.class);
                startActivity(addClub);
            }
        });

        return view;
    }

}