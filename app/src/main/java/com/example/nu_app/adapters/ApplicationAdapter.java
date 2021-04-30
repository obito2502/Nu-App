package com.example.nu_app.adapters;

import android.content.Context;
import android.net.Uri;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nu_app.Club;
import com.example.nu_app.R;
import com.example.nu_app.Student;
import com.example.nu_app.models.Application;
import com.example.nu_app.models.Post;
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

import java.util.ArrayList;
import java.util.List;

public class ApplicationAdapter extends RecyclerView.Adapter<ApplicationAdapter.MyHolder> {

    Context context;
    private List<Application> applicationList;
    StorageReference ref;
    StorageReference clubRef;
    FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private String login;


    public ApplicationAdapter( Context context, List<Application> applicationList) {
        this.context = context;
        this.applicationList = applicationList;
    }

    @NonNull
    @Override
    public ApplicationAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_applications, parent, false);

        return new ApplicationAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ApplicationAdapter.MyHolder myHolder, int i) {

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        Log.d("USER", user.toString());
//        login = user.getDisplayName()
        final Application application = applicationList.get(i);

        myHolder.name.setText(application.getName());
        myHolder.surname.setText(application.getSurname());
        myHolder.email.setText(application.getEmail());
        myHolder.occupation.setText(application.getOccupation());
        myHolder.motivation.setText(application.getMotivation());
        myHolder.reason.setText(application.getReason());
        myHolder.phone.setText(application.getPhone());



    }

    @Override
    public int getItemCount() {
        return applicationList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{

        // from row_post.xml
        TextView name, surname, email, occupation, motivation, reason, phone;



        public MyHolder(@NonNull View itemView) {

            super(itemView);
            name = itemView.findViewById(R.id.authorName);
            surname = itemView.findViewById(R.id.authorSurname);
            email = itemView.findViewById(R.id.authorEmail);
            occupation = itemView.findViewById(R.id.authorOccupation);
            motivation = itemView.findViewById(R.id.authorMotivation);
            reason = itemView.findViewById(R.id.authorReason);
            phone = itemView.findViewById(R.id.authorPhone);

        }

    }

}
