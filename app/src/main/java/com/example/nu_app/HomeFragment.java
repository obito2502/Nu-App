package com.example.nu_app;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.nu_app.adapters.PostAdapter;
import com.example.nu_app.models.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    FirebaseAuth firebaseAuth;
    private DatabaseReference databaseRef;

    private RecyclerView recyclerView;
    private List<Post> postList;
    PostAdapter postAdapter;

    private List<String> followingList;
    ActionBar actionBar;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("clubs");

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        //recycler view and its properties


        recyclerView = view.findViewById(R.id.postsRecyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        //set layout to recyclerview
        recyclerView.setLayoutManager(layoutManager);

        //init post list


        loadPosts();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu items for use in the action bar
        inflater.inflate(R.menu.menu_main, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.add_post){
            Intent addPost = new Intent(getActivity(), AddPostActivity.class);
            startActivity(addPost);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadPosts() {
        //path of all posts
        databaseRef = FirebaseDatabase.getInstance().getReference("posts");

        //get all data from this ref
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                postList = new ArrayList<>();

                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    Post post = ds.getValue(Post.class);
                    postList.add(post);
                }

                postAdapter = new PostAdapter(getContext(), postList);
                recyclerView.setAdapter(postAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //error

                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}