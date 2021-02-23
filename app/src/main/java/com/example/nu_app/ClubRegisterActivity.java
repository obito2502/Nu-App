package com.example.nu_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

public class ClubRegisterActivity extends AppCompatActivity {

    private TextView email;
    private Button registerButton;
    private EditText clubName;
    private EditText clubDescription;
    private EditText president;
    private ProgressDialog progress;
    private FirebaseAuth mAuth;

    public String login;

    private FirebaseDatabase db;
    public DatabaseReference club;

    private Button uploadGroupAvatar;
    private ImageView clubAvatar;
    private StorageReference mStorageRef;
    public Uri imageUri;
    private StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_register);

        email = (TextView)findViewById(R.id.club_email);
        registerButton = (Button)findViewById(R.id.club_register);
        progress = new ProgressDialog(this);
        clubName = findViewById(R.id.club_name);
        clubDescription = findViewById(R.id.description);
        president = findViewById(R.id.president_of_club);
        uploadGroupAvatar = findViewById(R.id.uploadGroupAvatar);
        clubAvatar = findViewById(R.id.clubAvatar);

        mStorageRef = FirebaseStorage.getInstance().getReference("Images");

        mAuth = FirebaseAuth.getInstance();

        final String email_of_club = getIntent().getStringExtra("email");
        final String password_of_club = getIntent().getStringExtra("password");


        email.setText(email_of_club);


        login = email_of_club.replaceAll("\\.", "_");
        login = login.substring(0, login.indexOf("@"));

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(uploadTask != null && uploadTask.isInProgress()) {
                    Toast.makeText(ClubRegisterActivity.this, "Image Upload In Progess", Toast.LENGTH_LONG).show();
                } else {
                    final String name = clubName.getText().toString().trim();
                    final String description = clubDescription.getText().toString().trim();
                    final String presidentOfClub = president.getText().toString().trim();
                    registerClub(email_of_club, password_of_club, name, description, presidentOfClub, login);

                    Fileuploader(name);
                }
            }
        });

        uploadGroupAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filechooser();
            }
        });
    }

    private void registerClub(final String email, String password, final String name, final String desc, final String pres, final String login) {
        progress.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            db = FirebaseDatabase.getInstance();
                            club = db.getReference("clubs");

                            Club newClub = new Club(email, name, desc, pres);

                            club.child(login).setValue(newClub);

                            mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        progress.dismiss();
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Toast.makeText(ClubRegisterActivity.this, "Registering Done. Please, verify your email address", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(ClubRegisterActivity.this, LoginActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(ClubRegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                            // Sign in success, update UI with the signed-in user's information

                        } else {
                            System.out.println("Proverka = " + task.isSuccessful());
                            // If sign in fails, display a message to the user.
                            Toast.makeText(ClubRegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progress.dismiss();
                Toast.makeText(ClubRegisterActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void Filechooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            clubAvatar.setImageURI(imageUri);
        }
    }

    private String getExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    private void Fileuploader(String clubName) {
        StorageReference ref = mStorageRef.child(clubName + '.' + getExtension(imageUri));

        uploadTask = ref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(ClubRegisterActivity.this, "Image uploaded!", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

}