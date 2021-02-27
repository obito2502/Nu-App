package com.example.nu_app.adapters;

import android.content.Context;
import android.net.Uri;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nu_app.Club;
import com.example.nu_app.R;
import com.example.nu_app.Student;
import com.example.nu_app.models.Post;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyHolder> {

    Context  context;
    List<Post> postList;
    StorageReference ref;
    StorageReference clubRef;


//    private FirebaseUser firebaseUser;


    public PostAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_posts, parent, false);

        return new PostAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder, int i) {

//        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Post post = postList.get(i);

//        authorInfo(post.getAuthor(), myHolder.author);

        myHolder.author.setText(post.getAuthor());
        myHolder.postTitle.setText(post.getTitle());
        myHolder.postDate.setText(post.getDate());
        myHolder.postLocation.setText(post.getLocation());
        myHolder.postDescription.setText(post.getDescription());
        myHolder.postTime.setText(DateUtils.getRelativeTimeSpanString(post.getTime(), System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS));

        ref = FirebaseStorage.getInstance().getReference("Posters").child(post.getImageLink());
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(myHolder.poster);
            }
        });

        clubRef = FirebaseStorage.getInstance().getReference("Images").child(post.getAuthor()+ ".jpg");
        clubRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                System.out.println(uri);
                Glide.with(context).load(uri).into(myHolder.clubIcon);
            }
        });


    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{

        // from row_post.xml
        TextView author, postTitle, postDate, postLocation, postDescription, postTime;
        ImageView clubIcon, poster;



        public MyHolder(@NonNull View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.author);
            postTitle = itemView.findViewById(R.id.postTitle);
            postDate = itemView.findViewById(R.id.postDate);
            postLocation = itemView.findViewById(R.id.postLocation);
            postDescription = itemView.findViewById(R.id.postDescription);
            postTime = itemView.findViewById(R.id.postedTime);
            poster = itemView.findViewById(R.id.poster);
            clubIcon = itemView.findViewById(R.id.club_icon);


        }

    }

    private void authorInfo(final String email, final TextView name)
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("clubs").child(user.getEmail().replace("@nu.edu.kz", "").replace(".", "_"));

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Club club = dataSnapshot.getValue(Club.class);

                name.setText(club.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
