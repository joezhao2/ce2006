package com.example.gmodsv1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SettingActivity extends AppCompatActivity {

    private Button signOutButton;
    private Button changePassWordButton;
    private Button changeUserNameButton;
    private Button deleteAccountButton;
    private Button uploadProfilePicButton;
    private ProgressBar progressBar;
    private FirebaseAuth authProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        getSupportActionBar().hide();

        signOutButton = findViewById(R.id.button_sign_out);
        changePassWordButton = findViewById(R.id.button_change_password);
        changeUserNameButton = findViewById(R.id.button_change_username);
        deleteAccountButton = findViewById(R.id.button_delete_account);
        uploadProfilePicButton = findViewById(R.id.button_upload_profile_pic);
        ProgressBar progressBar = findViewById(R.id.progressBar_setting);

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView_setting);

        //Change Password
        changePassWordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText changePassword = new EditText(view.getContext());
                final AlertDialog.Builder passwordChangeDialog = new AlertDialog.Builder(view.getContext());
                passwordChangeDialog.setTitle("Change your password");
                passwordChangeDialog.setMessage("Enter your New Password (Password should contain at least 1 Upper-case character and one special character)");
                passwordChangeDialog.setView(changePassword);

                passwordChangeDialog.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //get the user input
                        String newPassword = changePassword.getText().toString();
                        progressBar.setVisibility(View.VISIBLE);
                        if (TextUtils.isEmpty(newPassword)){
                            Toast.makeText(SettingActivity.this, "New Password field is left empty", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                        else if (isValidPassword(newPassword)){
                            firebaseUser.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(SettingActivity.this, "Password Reset Successful", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SettingActivity.this, "Password Reset Fail", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(SettingActivity.this, "Password enter is too weak, Please Try again", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                passwordChangeDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                passwordChangeDialog.create().show();
            }
        });

        //Change Username
        changeUserNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText changeUsername = new EditText(view.getContext());
                final AlertDialog.Builder usernameChangeDialog = new AlertDialog.Builder(view.getContext());
                usernameChangeDialog.setTitle("Change your username");
                usernameChangeDialog.setMessage("Enter your New Username");
                usernameChangeDialog.setView(changeUsername);

                usernameChangeDialog.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String newUsername = changeUsername.getText().toString();
                        if (newUsername.equals(firebaseUser.getDisplayName())){
                            Toast.makeText(SettingActivity.this, "New Username is same as the current one", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(newUsername).build();
                            assert firebaseUser != null;
                            firebaseUser.updateProfile(profileChangeRequest);
                            Toast.makeText(SettingActivity.this, "Username updated", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                usernameChangeDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                usernameChangeDialog.create().show();
            }
        });

        //upload/update profile picture
        uploadProfilePicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this,UploadProfilePicActivity.class);
                startActivity(intent);
            }
        });


        //delete account function
        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(SettingActivity.this);
                dialog.setTitle("Are you sure?");
                dialog.setMessage("Deleting this account will result in deletion of account from the system" +
                        " and you wont be able to access the application anymore");
                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteAccount();
                    }
                });

                dialog.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }
        });


        //Sign out function
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authProfile.signOut();
                Toast.makeText(SettingActivity.this, "You have signed out!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SettingActivity.this,MainActivity.class);
                //Clear stacks to prevent user from coming back
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        //set home selected
        bottomNavigationView.setSelectedItemId(R.id.settings);

        //perform item selected listener , switching between the activities when clicked
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.search:
                        Intent modIntent = new Intent(SettingActivity.this, FirestoreListActivity.class);
                        startActivity(modIntent);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        startActivity(new Intent(SettingActivity.this , UserProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }

    private boolean isValidPassword(String newPassword) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(newPassword);
        return matcher.matches();
    }

    private void deleteAccount() {
        authProfile = FirebaseAuth.getInstance();
        final FirebaseUser firebaseUser = authProfile.getCurrentUser();
        //progressBar.setVisibility(View.VISIBLE);
        firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(SettingActivity.this, "Account deleted", Toast.LENGTH_SHORT).show();
                    //progressBar.setVisibility(View.GONE);
                    Intent intent = new Intent(SettingActivity.this,MainActivity.class);
                    //Clear stacks to prevent user from coming back
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
                else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(SettingActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Delete User Data from database also
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
        referenceProfile.child(firebaseUser.getUid()).removeValue().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SettingActivity.this, "Data fail to delete", Toast.LENGTH_SHORT).show();
            }
        });
    }
}