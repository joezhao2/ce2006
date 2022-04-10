package com.example.gmodsv1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class UserProfileActivity extends AppCompatActivity {

    private TextView textViewWelcome , textViewFullName , textViewEmail , textViewDoB , textViewGender , textViewMobile;
    private ProgressBar progressBar;
    private String fullName , email , doB , gender , mobile;
    private ImageView imageView;
    private FirebaseAuth authProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        getSupportActionBar().hide();

        //textViewWelcome = findViewById(R.id.textView_show_welcome);
        textViewFullName = findViewById(R.id.textView_show_full_name);
        textViewEmail = findViewById(R.id.textView_show_email);
        textViewDoB = findViewById(R.id.textView_show_dob);
        textViewGender = findViewById(R.id.textView_show_gender);
        textViewMobile = findViewById(R.id.textView_show_mobile);
        progressBar = findViewById(R.id.progressbar_viewProfile);
        imageView = findViewById(R.id.imageView_profile_dp);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);



        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        //set on click listener on ImageView to open UploadProfilePicActivity


        if (firebaseUser == null){
            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_LONG).show();
        }
        else {
            progressBar.setVisibility(View.VISIBLE);
            showUserProfile(firebaseUser);
        }

        //set home selected
        bottomNavigationView.setSelectedItemId(R.id.home);

        //perform item selected listener , switching between the activities when clicked
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.search:
                        startActivity(new Intent(UserProfileActivity.this, FirestoreListActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.settings:
                        startActivity(new Intent(UserProfileActivity.this , SettingActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }

    //fetching of the info from firebase database and displaying them on the screen
    private void showUserProfile(FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();

        //Extracting User Reference from Database for 'Registered User'
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWrite readUserDetails = snapshot.getValue(ReadWrite.class);
                if (readUserDetails != null){
                    fullName = firebaseUser.getDisplayName();
                    email = firebaseUser.getEmail();
                    doB = readUserDetails.doB;
                    gender = readUserDetails.gender;
                    mobile = readUserDetails.mobile;

                    //textViewWelcome.setText("Welcome " + "!");
                    textViewFullName.setText(fullName);
                    textViewEmail.setText(email);
                    textViewDoB.setText(doB);
                    textViewGender.setText(gender);
                    textViewMobile.setText(mobile);

                    //Set User Profile Pic
                    Uri uri = firebaseUser.getPhotoUrl();
                    //nid to use piccasso cos image in data base
                    Picasso.with(UserProfileActivity.this).load(uri).into(imageView);
                }
                else {
                    Toast.makeText(UserProfileActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserProfileActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });

    }
}