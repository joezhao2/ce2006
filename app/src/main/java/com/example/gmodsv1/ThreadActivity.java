package com.example.gmodsv1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class ThreadActivity extends AppCompatActivity {
    private FirebaseFirestore mDb = FirebaseFirestore.getInstance();
    //private static final String TAG = "FirestoreListActivity";
    private static final String MODULES = "modules";

    private ArrayAdapter<String> adapter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);
        getSupportActionBar().hide();

        ListView commentlistview = findViewById(R.id.commentlist);
        adapter = new ArrayAdapter<String>(
                ThreadActivity.this,//activity is this
                android.R.layout.simple_list_item_1,//how it list(text view, display and module.toString
                //if toString() method not defined in class, it will show class fullname@hex addr
                new ArrayList<String>()
        );


        Intent intent = getIntent();
        String title = intent.getStringExtra("threadtitle");
        String s=intent.getStringExtra("course").substring(0,6);
        String s1=intent.getStringExtra("course").substring(6,26);
        TextView threadtitle = findViewById(R.id.threadtitle);
        threadtitle.setText(title);
        Log.d("coursename",s);
        Log.d("documentID",s1);
        mDb.collection(MODULES)
                .document(s)
                .collection("thread")
                .document(s1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<String> commentlist = new ArrayList<>();
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                ArrayList<String> arrayList = (ArrayList<String>) document.get("comments");
                                //Do what you need to do with your ArrayList
                                for (String s : arrayList) {
                                    if(s!=""){
                                        Log.d("comments", s);
                                        commentlist.add(s);
                                    }
                                }

                                adapter.clear();
                                adapter.addAll(commentlist);
                                commentlistview.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                commentlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        {
                                            String data=(String)adapterView.getItemAtPosition(i);
                                            Log.d("Deleting comment",data);
                                            new AlertDialog.Builder(adapterView.getContext())
                                                    .setMessage("Are you sure you wan to delete this comment?")
                                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {

                                                            mDb.collection(MODULES)
                                                                    .document(s)
                                                                    .collection("thread")
                                                                    .document(s1)
                                                                    .update("comments", FieldValue.arrayRemove(data));
                                                            //refresh kekek
                                                            mDb.collection(MODULES)
                                                                    .document(s)
                                                                    .collection("thread")
                                                                    .document(s1)
                                                                    .get()
                                                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                            if (task.isSuccessful()) {
                                                                                ArrayList<String> commentlist = new ArrayList<>();
                                                                                DocumentSnapshot document = task.getResult();
                                                                                if (document.exists()) {
                                                                                    ArrayList<String> arrayList = (ArrayList<String>) document.get("comments");
                                                                                    //Do what you need to do with your ArrayList
                                                                                    for (String s : arrayList) {
                                                                                        if(s!=""){
                                                                                            Log.d("comments", s);
                                                                                            commentlist.add(s);
                                                                                        }
                                                                                    }

                                                                                    adapter.clear();
                                                                                    adapter.addAll(commentlist);
                                                                                    commentlistview.setAdapter(adapter);
                                                                                    adapter.notifyDataSetChanged();

                                                                                }
                                                                            }
                                                                        }
                                                                    });


                                                        }
                                                    })
                                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            adapter.notifyDataSetChanged();
                                                        }
                                                    })
                                                    .create()
                                                    .show();


                                        }
                                    }
                                });

                            }
                        }
                    }
                });

                }
    public void onSubmitClickComment(View view) {
        Intent intent = getIntent();
        //String title = intent.getStringExtra("threadtitle");
        String s=intent.getStringExtra("course").substring(0,6);
        String s1=intent.getStringExtra("course").substring(6);
        EditText commentEditText = findViewById(R.id.commentadd);//get the name out from the "nameEditText"box


        String c = commentEditText.getText().toString();

        //int age = Integer.parseInt(ageString);

        //Log.d(TAG, "Submitted name: " + m.getName() + ", Cousecode: " + m.getCoursecode());
        mDb.collection(MODULES)
                .document(s)
                .collection("thread")
                .document(s1)
                .update("comments", FieldValue.arrayUnion(c));
        //adapter.notifyDataSetChanged();
        //.add(p) .add generates random ID
        ListView commentlistview = findViewById(R.id.commentlist);
        adapter = new ArrayAdapter<String>(
                ThreadActivity.this,//activity is this
                android.R.layout.simple_list_item_1,//how it list(text view, display and module.toString
                //if toString() method not defined in class, it will show class fullname@hex addr
                new ArrayList<String>()
        );
        mDb.collection(MODULES)
                .document(s)
                .collection("thread")
                .document(s1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<String> commentlist = new ArrayList<>();
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                ArrayList<String> arrayList = (ArrayList<String>) document.get("comments");
                                //Do what you need to do with your ArrayList
                                for (String s : arrayList) {
                                    if(s!="")
                                    {Log.d("comments", s);
                                    commentlist.add(s);}
                                }

                                adapter.clear();
                                adapter.addAll(commentlist);
                                commentlistview.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                commentEditText.getText().clear();

                            }
                        }
                    }
                });

    }

    }
