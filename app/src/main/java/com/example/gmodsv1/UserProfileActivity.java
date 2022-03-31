package com.example.gmodsv1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfileActivity extends AppCompatActivity {

    private TextView textViewWelcome , textViewFullName , textViewEmail , textViewDoB , textViewGender , textViewMobile;
    private ProgressBar progressBar;
    private Button signOutButton;
    private String fullName , email , doB , gender , mobile;
    private ImageView imageView;
    private FirebaseAuth authProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        getSupportActionBar().setTitle("User Details");

        textViewWelcome = findViewById(R.id.textView_show_welcome);
        textViewFullName = findViewById(R.id.textView_show_full_name);
        textViewEmail = findViewById(R.id.textView_show_email);
        textViewDoB = findViewById(R.id.textView_show_dob);
        textViewGender = findViewById(R.id.textView_show_gender);
        textViewMobile = findViewById(R.id.textView_show_mobile);
        progressBar = findViewById(R.id.progressbar_viewProfile);
        signOutButton = findViewById(R.id.button_sign_out);



        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        if (firebaseUser == null){
            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_LONG).show();
        }
        else {
            progressBar.setVisibility(View.VISIBLE);
            showUserProfile(firebaseUser);
        }

        //Sign out function
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authProfile.signOut();
                Toast.makeText(UserProfileActivity.this, "You have signed out!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UserProfileActivity.this,MainActivity.class);
                //Clear stacks to prevent user from coming back
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
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

                    textViewWelcome.setText("Welcome " + fullName + "!");
                    textViewFullName.setText(fullName);
                    textViewEmail.setText(email);
                    textViewDoB.setText(doB);
                    textViewGender.setText(gender);
                    textViewMobile.setText(mobile);
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


    //Creating ActionBar Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //Inflate  menu items
        getMenuInflater().inflate(R.menu.common_menu , menu);
        return super.onCreateOptionsMenu(menu);
    }

    //When any menu item is selected
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        int id = item.getItemId();

        if (id == R.id.menu_refresh){
            //Refresh Activity
            startActivity(getIntent());
            finish();
            overridePendingTransition(0,0);
        }/*
        else if (id == R.id.menu_update_profile){
            Intent intent = new Intent(UserProfileActivity.this , UpdateProfileActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.menu_update_email){
            Intent intent = new Intent(UserProfileActivity.this , UpdateEmailActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.menu_change_password){
            Intent intent = new Intent(UserProfileActivity.this , ChangePasswordActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.menu_delete_profile){
            Intent intent = new Intent(UserProfileActivity.this , DeleteProfileActivity.class);
            startActivity(intent);
        } */
        else {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}