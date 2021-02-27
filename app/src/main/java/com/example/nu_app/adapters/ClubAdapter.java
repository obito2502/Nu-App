package com.example.nu_app.adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.nu_app.Club;
import com.example.nu_app.R;
import com.example.nu_app.Student;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class ClubAdapter extends ArrayAdapter {

    private List<Club> clubModelList;
    private int resource;
    private LayoutInflater inflater;

    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference("Images");

    public ClubAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        clubModelList = objects;
        this.resource = resource;
        inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null) {
            convertView = inflater.inflate(R.layout.row_clubs, null);
        }

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        ImageView clubIcon;
        TextView clubName;
        TextView clubDescription;
        final Button subscribeButton;
        final List<String> subscribedClubs = new ArrayList<String>();

        clubName = convertView.findViewById(R.id.clubName);
        clubDescription = convertView.findViewById(R.id.clubDescription);
        subscribeButton = convertView.findViewById(R.id.subscribeButton);

        clubName.setText(clubModelList.get(position).getName());
        clubDescription.setText(clubModelList.get(position).getDescription());

        StorageReference ref = mStorageRef.child(clubModelList.get(position).getName() +".jpg");
        clubIcon = convertView.findViewById(R.id.clubIcon);

        final ImageView finalClubIcon = clubIcon;
        final View finalConvertView = convertView;
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                System.out.println("URIII = " + uri);
                Glide.with(finalConvertView).load(uri).into(finalClubIcon);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("students").child(user.getEmail().replace("@nu.edu.kz", "").replace(".", "_"));
        reference.child("subscriptions").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    subscribedClubs.add(childSnapshot.getValue().toString());

                    if(childSnapshot.getValue().equals(clubModelList.get(position).getName())) {
                        subscribeButton.setText("✓");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        subscribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Student student = dataSnapshot.getValue(Student.class);

                        if(subscribeButton.getText().equals("✓")) {
                            student.cancelSubsription(clubModelList.get(position).getName());
                            reference.setValue(student);

                            subscribeButton.setText("+");

                        } else if (subscribeButton.getText().equals("+")) {

                            student.addSubscription(clubModelList.get(position).getName());
                            reference.setValue(student);

                            subscribeButton.setText("✓");
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });


        return convertView;
    }


}
