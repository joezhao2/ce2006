package com.example.gmodsv1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private EditText editTextLoginEmail , editTextLoginPwd;
    private ProgressBar progressBar;
    private FirebaseAuth authProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        editTextLoginEmail = findViewById(R.id.editText_login_email);
        editTextLoginPwd = findViewById(R.id.editText_login_pwd);
        progressBar = findViewById(R.id.progressbar_login);

        //To allow ppl who are already login to continue using the app after re entering
        authProfile = FirebaseAuth.getInstance();


        //Reset Password
        Button buttonForgotPassword = findViewById(R.id.button_forgot_password);
        buttonForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "You can reset your password now", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this , ForgotPasswordActivity.class));
            }
        });

        //Show/Hide Password using Eye Icon
        ImageView imageViewHowHidePwd = findViewById(R.id.imageView_show_hide_pwd);
        imageViewHowHidePwd.setImageResource(R.drawable.close_eye);
        imageViewHowHidePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if the password is visible , then we hide it
                if (editTextLoginPwd.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    editTextLoginPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    //change icon
                    imageViewHowHidePwd.setImageResource(R.drawable.close_eye);
                }
                else {
                    editTextLoginPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imageViewHowHidePwd.setImageResource(R.drawable.eye);
                }
            }
        });

        //Login User
        Button buttonLogin = findViewById(R.id.button_login);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textEmail = editTextLoginEmail.getText().toString();
                String textPwd = editTextLoginPwd.getText().toString();

                if (TextUtils.isEmpty(textEmail)){
                    editTextLoginEmail.setError("Email is required");
                    editTextLoginEmail.requestFocus();
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()){
                    editTextLoginEmail.setError("Valid email is required");
                    editTextLoginEmail.requestFocus();
                }
                else if (TextUtils.isEmpty(textPwd)){
                    editTextLoginPwd.setError("Password is required");
                    editTextLoginPwd.requestFocus();
                }
                else {
                    progressBar.setVisibility(View.VISIBLE);
                    loginUser(textEmail , textPwd);
                }
            }
        });
    }

    private void loginUser(String textEmail, String textPwd) {
        authProfile.signInWithEmailAndPassword(textEmail , textPwd).addOnCompleteListener(LoginActivity.this ,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();


                    //Start the UserProfileActivity
                    startActivity(new Intent(LoginActivity.this , UserProfileActivity.class));
                    finish(); // Close Login Activity


//                    //Get instance of the current user
//                    FirebaseUser firebaseUser = authProfile.getCurrentUser();
//
//                    //check if email is verified before user can access their profile
//                    if (firebaseUser.isEmailVerified()){
//                        Toast.makeText(LoginActivity.this, "You are login in", Toast.LENGTH_SHORT).show();
//                    }
//                    else {
//                        firebaseUser.sendEmailVerification();
//                        authProfile.signOut(); //sign out the user
//                        showAlertDialog();
//                    }
                }
                else {
                    try {
                        throw task.getException();

                    }
                    catch (FirebaseAuthInvalidUserException e){
                        editTextLoginEmail.setError("User does not exist or is no longer valid , Please register again");
                        editTextLoginEmail.requestFocus();
                    }
                    catch (FirebaseAuthInvalidCredentialsException e) {
                        editTextLoginEmail.setError("Email / Password entered is invalid , Please try again");
                        editTextLoginEmail.requestFocus();
                    }
                    catch (Exception e) {
                        Log.e(TAG , e.getMessage());
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(LoginActivity.this, "Login Unsuccessful", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

//    private void showAlertDialog() {
//        //Set up Alert Builder
//        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
//        builder.setTitle("Email is not verified");
//        builder.setMessage("Please verify your email now. You cannot login without email verification");
//
//        //Open Email App if user click on continue button
//        builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                Intent intent = new Intent(Intent.ACTION_MAIN);
//                //This will choose the email app to go to
//                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
//                //email app will open a new window , will not affect out app
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//            }
//        });
//
//        //Create the AlertDialog
//        AlertDialog alertDialog = builder.create();
//
//        //Show the AlertDialog
//
//        alertDialog.show();
//    }

    //Check if User is already logged in. In such case , go to user activity page
    @Override
    protected void onStart() {
        super.onStart();
        if (authProfile.getCurrentUser() != null){
            Toast.makeText(this, "Welcome back", Toast.LENGTH_SHORT).show();

            //Start the UserProfileActivity
            startActivity(new Intent(LoginActivity.this , UserProfileActivity.class));
            finish(); // Close Login Activity
        }
        else {
            Toast.makeText(this, "Please Login", Toast.LENGTH_SHORT).show();
        }
    }

}