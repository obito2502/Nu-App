package com.example.nu_app;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.nu_app.models.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;
import com.squareup.picasso.Picasso;

public class AddPostActivity extends AppCompatActivity {

    private FirebaseUser pAuth;
    public DatabaseReference post;
    public DatabaseReference club;

    private FirebaseStorage storage;
    private StorageReference storageReference;

    private Button uploadEventPhoto;
    private ImageView eventPoster;

    private String randomKey;
    public Uri imageUri;
    private StorageTask uploadTask;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);


        final EditText title = findViewById(R.id.event_name);
        final EditText date_time = findViewById(R.id.event_date_time);
        final EditText location = findViewById(R.id.event_location);
        final EditText description = findViewById(R.id.event_description);


        date_time.setInputType(InputType.TYPE_NULL);

        Button saveBtn = findViewById(R.id.save_event);

        pAuth = FirebaseAuth.getInstance().getCurrentUser();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference("Posters");
        post = FirebaseDatabase.getInstance().getReference("Posters");

        uploadEventPhoto = findViewById(R.id.upload_event_poster);
        eventPoster = findViewById(R.id.event_poster);

        post = FirebaseDatabase.getInstance().getReference().child("posts");

        date_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateTimeDialog(date_time);
            }
        });


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String clubEmail = pAuth.getEmail();

                club = FirebaseDatabase.getInstance().getReference().child("clubs");

                club.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String author = null;
                        for(DataSnapshot ds: dataSnapshot.getChildren())
                        {
                            Club clubName = ds.getValue(Club.class);
                            if(clubName.email.equals(clubEmail)){
                                author = clubName.name;
                                savePost(title, date_time, location, description, author);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });

        uploadEventPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
    }

    private void showDateTimeDialog(final EditText date_time) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        calendar.set(Calendar.HOUR, hour);
                        calendar.set(Calendar.MINUTE, minute);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
                        date_time.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                };

                new TimePickerDialog(AddPostActivity.this, timeSetListener, calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE),false).show();
            }
        };

        new DatePickerDialog(AddPostActivity.this,dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }


    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null ) {
            imageUri = data.getData();

            Picasso.get().load(imageUri).into(eventPoster);

        }

    }


    private String getExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }






    private void savePost(EditText title, EditText date, EditText location, EditText description, final String club){
        if(uploadTask != null && uploadTask.isInProgress()) {
            Toast.makeText(AddPostActivity.this, "Image Upload In Progess", Toast.LENGTH_SHORT).show();
        } else {
            final ProgressDialog pd = new ProgressDialog(this);
            pd.setTitle("Uploading Image...");
            pd.show();

            final String ptitle = title.getText().toString().trim();
            final String pdate = date.getText().toString().trim();
            final String plocation = location.getText().toString().trim();
            final String pdescription = description.getText().toString().trim();
            randomKey = UUID.randomUUID().toString();
            final String imageLink = randomKey+ "." +getExtension(imageUri);
            StorageReference posterRef = storageReference.child(imageLink);

            uploadTask = posterRef.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            pd.dismiss();
                            Toast.makeText(AddPostActivity.this, "Image uploaded!", Toast.LENGTH_SHORT).show();
                            Post newPost = new Post( ptitle, pdate, plocation, pdescription, club, imageLink);
                            String id = post.push().getKey();
                            post.child(id).setValue(newPost);
                            Toast.makeText(AddPostActivity.this, "Your post was succesfully stored!!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            pd.dismiss();
                            Toast.makeText(AddPostActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progressPercent = (100.0 * taskSnapshot.getBytesTransferred()/ taskSnapshot.getTotalByteCount());
                            pd.setMessage("Progress:" + (int) progressPercent + "%");
                        }
                    });




        }
    }


}