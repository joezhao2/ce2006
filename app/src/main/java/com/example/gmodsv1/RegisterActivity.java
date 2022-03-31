package com.example.gmodsv1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextRegisterFullName , editTextRegisterEmail , editTextRegisterDoB ,editTextRegisterMobile
            ,editTextRegisterPwd , editTextRegisterConfirmPwd;
    private ProgressBar progressBar;
    private RadioGroup radioGroupRegisterGender;
    private RadioButton radioButtonRegisterGenderSelected;
    private DatePickerDialog picker;
    private static final String TAG = "Register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setTitle("Register");

        Toast.makeText(this, "You can register now..", Toast.LENGTH_LONG).show();
        progressBar = findViewById(R.id.progressBar);
        editTextRegisterFullName = findViewById(R.id.editText_register_full_name);
        editTextRegisterEmail = findViewById(R.id.editText_register_email);
        editTextRegisterDoB = findViewById(R.id.editText_register_dob);
        editTextRegisterMobile = findViewById(R.id.editText_register_mobile);
        editTextRegisterPwd = findViewById(R.id.editText_register_password);
        editTextRegisterConfirmPwd = findViewById(R.id.editText_register_confirm_password);


        //Radio Button for gender
        radioGroupRegisterGender = findViewById(R.id.radio_group_register_gender);
        radioGroupRegisterGender.clearCheck();

        //Setting up Date Picker on EditText
        editTextRegisterDoB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calender = Calendar.getInstance();
                int days = calender.get(Calendar.DAY_OF_MONTH);
                int months = calender.get(Calendar.MONTH);
                int year = calender.get(Calendar.YEAR);

                //Date Picker Dialog
                picker = new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        editTextRegisterDoB.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year , months , days);
                picker.show();
            }
        });

        Button buttonRegister = findViewById(R.id.button_register);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedGenderId = radioGroupRegisterGender.getCheckedRadioButtonId();
                radioButtonRegisterGenderSelected = findViewById(selectedGenderId);

                //Obtain selected data
                String textFullName = editTextRegisterFullName.getText().toString();
                String textEmail = editTextRegisterEmail.getText().toString();
                String textDoB = editTextRegisterDoB.getText().toString();
                String textMobile = editTextRegisterMobile.getText().toString();
                String textPwd = editTextRegisterPwd.getText().toString();
                String textConfirmPwd = editTextRegisterConfirmPwd.getText().toString();
                String textGender;

                //Validate Mobile Number using Matcher and Pattern (Regular Expression)
//                String mobileRegex = "[6-9][0-9]{9}"; //First number can be {6,7,8,9} and rest can be any no
//                Matcher mobileMatcher;
//                Pattern mobilePattern = Pattern.compile(mobileRegex);
//                mobileMatcher = mobilePattern.matcher(textMobile);

                if (TextUtils.isEmpty(textFullName)){
                    Toast.makeText(RegisterActivity.this, "Please enter your full name", Toast.LENGTH_SHORT).show();
                    editTextRegisterFullName.setError("Full Name is required");
                    editTextRegisterFullName.requestFocus();
                }
                else if (TextUtils.isEmpty(textEmail)){
                    Toast.makeText(RegisterActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                    editTextRegisterEmail.setError("Email is required");
                    editTextRegisterEmail.requestFocus();
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()){
                    Toast.makeText(RegisterActivity.this, "Please enter your valid email", Toast.LENGTH_SHORT).show();
                    editTextRegisterEmail.setError("Valid email is required");
                    editTextRegisterEmail.requestFocus();
                }
                else if (TextUtils.isEmpty(textDoB)){
                    Toast.makeText(RegisterActivity.this, "Please enter your DOB", Toast.LENGTH_SHORT).show();
                    editTextRegisterDoB.setError("DOB is required");
                    editTextRegisterDoB.requestFocus();
                }
                else if (radioGroupRegisterGender.getCheckedRadioButtonId() == -1){
                    Toast.makeText(RegisterActivity.this, "Please select your gender", Toast.LENGTH_SHORT).show();
                    radioButtonRegisterGenderSelected.setError("Gender is required");
                    radioButtonRegisterGenderSelected.requestFocus();
                }
                else if (TextUtils.isEmpty(textMobile)){
                    Toast.makeText(RegisterActivity.this, "Please enter your Mobile", Toast.LENGTH_SHORT).show();
                    editTextRegisterMobile.setError("Mobile is required");
                    editTextRegisterMobile.requestFocus();
                }
//                else if (!mobileMatcher.find()){
//                    Toast.makeText(RegisterActivity.this, "Please enter a valid Mobile no", Toast.LENGTH_SHORT).show();
//                    editTextRegisterMobile.setError("Valid mobile no is required");
//                    editTextRegisterMobile.requestFocus();
//                }
                else if (textMobile.length() != 8){
                    Toast.makeText(RegisterActivity.this, "Please re-enter your mobile no", Toast.LENGTH_SHORT).show();
                    editTextRegisterMobile.setError("Mobile No should be 8 digits");
                    editTextRegisterMobile.requestFocus();
                }
                else if (TextUtils.isEmpty(textPwd)){
                    Toast.makeText(RegisterActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
                    editTextRegisterPwd.setError("Password is required");
                    editTextRegisterPwd.requestFocus();
                }
                else if (textPwd.length() < 6){
                    Toast.makeText(RegisterActivity.this, "Password should be at least 6 digits", Toast.LENGTH_SHORT).show();
                    editTextRegisterPwd.setError("Password too weak");
                    editTextRegisterPwd.requestFocus();
                }
                else if (TextUtils.isEmpty(textConfirmPwd)){
                    Toast.makeText(RegisterActivity.this, "Please confirm your password", Toast.LENGTH_SHORT).show();
                    editTextRegisterConfirmPwd.setError("Password confirmation is required");
                    editTextRegisterConfirmPwd.requestFocus();
                }
                else if (!textPwd.equals(textConfirmPwd)){
                    Toast.makeText(RegisterActivity.this, "Please enter same password", Toast.LENGTH_SHORT).show();
                    editTextRegisterConfirmPwd.setError("Password confirmation is required");
                    editTextRegisterConfirmPwd.requestFocus();
                    //clear the entered password
                    editTextRegisterPwd.clearComposingText();
                    editTextRegisterConfirmPwd.clearComposingText();
                }
                else {
                    textGender = radioButtonRegisterGenderSelected.getText().toString();
                    progressBar.setVisibility(View.VISIBLE);
                    registerUser(textFullName , textEmail , textDoB , textGender , textMobile , textPwd);
                }
            }
        });
    }

    //Register User in Firebase
    private void registerUser(String textFullName, String textEmail, String textDoB, String textGender, String textMobile, String textPwd) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        //Creating a new User
        auth.createUserWithEmailAndPassword(textEmail , textPwd).addOnCompleteListener(RegisterActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            //Toast.makeText(RegisterActivity.this, "User Registered", Toast.LENGTH_LONG).show();
                            //get the current user
                            FirebaseUser firebaseUser = auth.getCurrentUser();


                            //Update Display Name of User , don't have to save it explicitly
                            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(textFullName).build();
                            assert firebaseUser != null;
                            firebaseUser.updateProfile(profileChangeRequest);

                            //Entering User Data into FireBase RealTime DataBase
                            ReadWrite writeUserDetails = new ReadWrite(textDoB , textGender , textMobile);

                            //Extract user reference from Database for 'Registered User' , if dont have will create one
                            DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");

                            referenceProfile.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        //send verification email
                                        firebaseUser.sendEmailVerification();

                                        Toast.makeText(RegisterActivity.this, "User registered", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                        //Redirect user to login after registration is done
                                        Intent intent = new Intent(RegisterActivity.this , MainActivity.class);
                                        //cannot return to the register page after successful registration
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish(); //to close Register Activity

                                    }
                                    else {
                                        Toast.makeText(RegisterActivity.this, "Registration failed. Please Try again", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }
                        else {
                            try {
                                throw task.getException();

                            } catch (FirebaseAuthWeakPasswordException e) {
                                editTextRegisterPwd.setError("Your password is too weak!");
                                editTextRegisterPwd.requestFocus();
                                progressBar.setVisibility(View.GONE);
                            } catch (FirebaseAuthInvalidCredentialsException e){
                                editTextRegisterPwd.setError("Your email is invalid or already in use.");
                                editTextRegisterPwd.requestFocus();
                                progressBar.setVisibility(View.GONE);
                            } catch (FirebaseAuthUserCollisionException e){
                                editTextRegisterPwd.setError("User already registered with this email.");
                                editTextRegisterPwd.requestFocus();
                                progressBar.setVisibility(View.GONE);
                            } catch (Exception e){
                                Log.e(TAG , e.getMessage());
                                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }

                        }
                    }
                });
    }
}