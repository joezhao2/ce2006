package com.example.gmodsv1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ThreadActivity extends AppCompatActivity {
    private FirebaseFirestore mDb = FirebaseFirestore.getInstance();
    //private static final String TAG = "FirestoreListActivity";
    private static final String MODULES = "modules";

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    List<CommentModelClass> commentList;

    CommentAdapter adapter;
    private CommentAdapter.RecyclerViewClickListener listener;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);
        getSupportActionBar().hide();

//        ListView commentlistview = findViewById(R.id.commentlist);
//        adapter = new ArrayAdapter<String>(
//                ThreadActivity.this,//activity is this
//                android.R.layout.simple_list_item_1,//how it list(text view, display and module.toString
//                //if toString() method not defined in class, it will show class fullname@hex addr
//                new ArrayList<String>()
//        );

        initData();
        initRecyclerView("text");
        Intent intent = getIntent();
        String threadTitle = intent.getStringExtra("threadtitle");
        String courseName = intent.getStringExtra("course").substring(0, 6);    // s
        String documentId = intent.getStringExtra("course").substring(6, 26);   // s1
        TextView threadTitleText = findViewById(R.id.threadTitle);
        //TextView threadtitle2 =findViewById(R.id.mainview);
        threadTitleText.setText(threadTitle);
        //threadtitle2.setText(s);
        Log.d("courseName", courseName);
        Log.d("documentId", documentId);
        mDb.collection(MODULES)
                .document(courseName)
                .collection("thread")
                .document(documentId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<HashMap<String, String>> commentArrayList = new ArrayList<>();
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                commentArrayList = (ArrayList<HashMap<String, String>>) document.get("comments");
                                for (HashMap<String, String> commentObj: commentArrayList) {
                                    String username = commentObj.get("username");
                                    String userId = commentObj.get("userId");
                                    String content = commentObj.get("content");
                                    String upvoteCount = commentObj.get("upvoteCount");
                                    Log.d("comment", content);
                                    CommentModelClass tmpCommentObj = new CommentModelClass(R.drawable.photo6208635785310221009,
                                            username,
                                            content,
                                            courseName+document.getId(),
                                            0,
                                            true);
                                    commentList.add(tmpCommentObj);
                                    initRecyclerView(courseName+document.getId());

                                }
                            }

//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                if (document.exists()) {
//                                    String title = document.get("message").toString();
//                                    String user = document.get("userName").toString();
//                                    //Do what you need to do with your ArrayList
//                                    ArrayList<String> arrayListcomments = (ArrayList<String>) document.get("comments");
//                                    for (String s : arrayListcomments) {
//                                        Log.d("comments", s);
//                                    }
//                                    ModelClass m= new ModelClass(R.drawable.photo6208635785310221009,user, s+document.getId(), title);
//                                    userList.add(m);
//                                    initRecyclerView(courseName+document.getId());
//                                    Log.d("name+id",courseName+document.getId());
//
//
//                                }
//                            }
                        }
                    }
                });
//        mDb.collection(MODULES)
//                .document(courseName)
//                .collection("thread")
//                .document(documentId)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if (task.isSuccessful()) {
//                            ArrayList<String> commentlist = new ArrayList<>();
//                            DocumentSnapshot document = task.getResult();
//                            if (document.exists()) {
//                                ArrayList<String> arrayList = (ArrayList<String>) document.get("comments");
//                                //Do what you need to do with your ArrayList
//                                for (String s : arrayList) {
//                                    if (s != "") {
//                                        Log.d("comments", s);
//                                        commentlist.add(s);
//                                    }
//                                }
//
//                                adapter.clear();
//                                adapter.addAll(commentlist);
//                                commentlistview.setAdapter(adapter);
//                                adapter.notifyDataSetChanged();
//                                commentlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                    @Override
//                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                                        {
//                                            String data = (String) adapterView.getItemAtPosition(i);
//                                            Log.d("Deleting comment", data);
//                                            new AlertDialog.Builder(adapterView.getContext())
//                                                    .setMessage("Are you sure you wan to delete this comment?")
//                                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                                                        @Override
//                                                        public void onClick(DialogInterface dialogInterface, int i) {
//
//                                                            mDb.collection(MODULES)
//                                                                    .document(s)
//                                                                    .collection("thread")
//                                                                    .document(s1)
//                                                                    .update("comments", FieldValue.arrayRemove(data));
//                                                            //refresh kekek
//                                                            mDb.collection(MODULES)
//                                                                    .document(s)
//                                                                    .collection("thread")
//                                                                    .document(s1)
//                                                                    .get()
//                                                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                                                                        @Override
//                                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                                                            if (task.isSuccessful()) {
//                                                                                ArrayList<String> commentlist = new ArrayList<>();
//                                                                                DocumentSnapshot document = task.getResult();
//                                                                                if (document.exists()) {
//                                                                                    ArrayList<String> arrayList = (ArrayList<String>) document.get("comments");
//                                                                                    //Do what you need to do with your ArrayList
//                                                                                    for (String s : arrayList) {
//                                                                                        if (s != "") {
//                                                                                            Log.d("comments", s);
//                                                                                            commentlist.add(s);
//                                                                                        }
//                                                                                    }
//
//                                                                                    adapter.clear();
//                                                                                    adapter.addAll(commentlist);
//                                                                                    commentlistview.setAdapter(adapter);
//                                                                                    adapter.notifyDataSetChanged();
//
//                                                                                }
//                                                                            }
//                                                                        }
//                                                                    });
//
//
//                                                        }
//                                                    })
//                                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                                                        @Override
//                                                        public void onClick(DialogInterface dialogInterface, int i) {
//                                                            adapter.notifyDataSetChanged();
//                                                        }
//                                                    })
//                                                    .create()
//                                                    .show();
//
//
//                                        }
//                                    }
//                                });
//
//                            }
//                        }
//                    }
//                });
    }
    private void initRecyclerView(String s) {
        recyclerView=findViewById(R.id.commentList);
        layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(recyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new CommentAdapter(commentList,listener);
        //userList.clear();
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    private void initData() {
        commentList= new ArrayList<>();
        //userList.add(new ModelClass(R.drawable.ic_baseline_person_outline_24,"fullscapdeveloper","time","comments"));


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
//        mDb.collection(MODULES)
//                .document(s)
//                .collection("thread")
//                .document(s1)
//                .update("comments", FieldValue.arrayUnion(c));
//        //adapter.notifyDataSetChanged();
//        //.add(p) .add generates random ID
//        ListView commentlistview = findViewById(R.id.commentlist);
////        adapter = new ArrayAdapter<String>(
//                ThreadActivity.this,//activity is this
//                android.R.layout.simple_list_item_1,//how it list(text view, display and module.toString
//                //if toString() method not defined in class, it will show class fullname@hex addr
//                new ArrayList<String>()
//        );
//        mDb.collection(MODULES)
//                .document(s)
//                .collection("thread")
//                .document(s1)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if (task.isSuccessful()) {
//                            ArrayList<String> commentlist = new ArrayList<>();
//                            DocumentSnapshot document = task.getResult();
//                            if (document.exists()) {
//                                ArrayList<String> arrayList = (ArrayList<String>) document.get("comments");
//                                //Do what you need to do with your ArrayList
//                                for (String s : arrayList) {
//                                    if(s!="")
//                                    {Log.d("comments", s);
//                                    commentlist.add(s);}
//                                }
//
//                                adapter.clear();
//                                adapter.addAll(commentlist);
//                                commentlistview.setAdapter(adapter);
//                                adapter.notifyDataSetChanged();
//                                commentEditText.getText().clear();
//
//                            }
//                        }
//                    }
//                });

    }

    }
