package com.example.nu_app.adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nu_app.Club;
import com.example.nu_app.R;
import com.example.nu_app.Student;
import com.example.nu_app.models.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyHolder> {

    Context  context;
    List<Post> postList;

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
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {

//        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Post post = postList.get(i);
        System.out.println(post.getDescription());

        authorInfo(post.getAuthor(), myHolder.author);


//        String email = postList.get(i).();
//
//
//
//        Calendar calendar = Calendar.getInstance(Locale.getDefault());
////        calendar.setTimeInMillis(Long.parseLong(pTimeStamp));
//
//        String postedTime = DateFormat.format("dd/mm/yyyy hh:mm aa", calendar).toString();
//
//

       myHolder.postTitle.setText(post.getTitle());
       myHolder.postDate.setText(post.getDate());
       myHolder.postLocation.setText(post.getLocation());
       myHolder.postDescription.setText(post.getDescription());
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{

        // from row_post.xml
        TextView author, postTitle, postDate, postLocation, postDescription;




        public MyHolder(@NonNull View itemView) {
            super(itemView);

            author = itemView.findViewById(R.id.author);
            postTitle = itemView.findViewById(R.id.postTitle);
            postDate = itemView.findViewById(R.id.postDate);
            postLocation = itemView.findViewById(R.id.postLocation);
            postDescription = itemView.findViewById(R.id.postDescription);


        }

    }

    private void authorInfo(final String email, final TextView name)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("students").child("aiymkhan_kenessova");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Student club = dataSnapshot.getValue(Student.class);
                name.setText(club.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
