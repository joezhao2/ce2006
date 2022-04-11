package com.example.gmodsv1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.w3c.dom.Text;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ThreadActivity extends AppCompatActivity {
    private FirebaseFirestore mDb = FirebaseFirestore.getInstance();
    //private static final String TAG = "FirestoreListActivity";
    private static final String MODULES = "modules";
    private FirebaseUser fbuser = FirebaseAuth.getInstance().getCurrentUser();
    private String fbuserid=fbuser.getUid();

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    List<CommentModelClass> commentList;

    CommentAdapter adapter;
    private CommentAdapter.RecyclerViewClickListener listener;

    String courseId, documentId;

    private void updateCommentDisplay() {
        mDb.collection(MODULES)
                .document(courseId)
                .collection("thread")
                .document(documentId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<HashMap<String, String>> commentArrayList = new ArrayList<>();
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                try {
                                    commentArrayList = (ArrayList<HashMap<String, String>>) document.get("comments");
                                    Collections.reverse(commentArrayList);
                                    for (HashMap<String, String> commentObj: commentArrayList) {
                                        String username = commentObj.get("username");
                                        String userId = commentObj.get("userid");
                                        String content = commentObj.get("content");
                                        String upvoteCount = commentObj.get("upvoteCount");
                                        String time = commentObj.get("time");
                                        Log.d("comment", content);
                                        CommentModelClass tmpCommentObj = new CommentModelClass(R.drawable.photo6208635785310221009,
                                                username,
                                                content,
                                                courseId+document.getId(),
                                                0,
                                                true,
                                                TimeFormatter.getStringTimeDelta(Instant.parse(time), Instant.now()));
                                        commentList.add(tmpCommentObj);
                                        initRecyclerView(courseId+document.getId());

                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });
    }

    private void updateThreadDisplay() {
        mDb.collection(MODULES)
                .document(courseId)
                .collection("thread")
                .document(documentId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                try {
                                        String username = document.get("username").toString();
                                        String title = document.get("title").toString();
                                        String body = document.get("body").toString();
                                        TextView courseidtext = findViewById(R.id.courseIdText);
                                        TextView coursenametext = findViewById(R.id.courseNameText);
                                        courseidtext.setText(courseId);
                                        mDb.collection(MODULES).document(courseId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    String couseName = document.get("name").toString();
                                                    coursenametext.setText(couseName);
                                                }
                                            }
                                        });


                                        Instant time = Instant.parse(document.get("time").toString());
                                        LocalDateTime localDateTime = LocalDateTime.ofInstant(time, ZoneOffset.UTC);
                                        String timeDelta = TimeFormatter.getStringTimeDelta(time, Instant.now());
                                        String timeString = "on " + localDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                                        String upvoteCount = document.get("upvotes").toString();

                                        ArrayList<HashMap<String, String>> commentArrayList = (ArrayList<HashMap<String, String>>) document.get("comments");
                                        String replyCount = Integer.toString(commentArrayList.size() - 1);

                                        ArrayList<String> upvotedUserids = (ArrayList<String>) document.get("upvotedUserids");
                                        Boolean upvoted = upvotedUserids.contains(fbuser.getUid());

                                        TextView usernameText = findViewById(R.id.usernameText);
                                        TextView titleText = findViewById(R.id.threadTitle);
                                        TextView bodyText = findViewById(R.id.bodyText);

                                        TextView timeDeltaThreadText = findViewById(R.id.timeDeltaThreadText);
                                        TextView timeText = findViewById(R.id.timeText);

                                        ImageView upvoteOnIcon = findViewById(R.id.upvoteOnIcon);
                                        ImageView upvoteOffIcon = findViewById(R.id.upvoteOffIcon);
                                        TextView upvoteCountText = findViewById(R.id.upvoteCountText);

                                        TextView replyCountText = findViewById(R.id.replyCountText);
                                        TextView replyDisplayText = findViewById(R.id.replyDisplayText);

                                        usernameText.setText(username);
                                        titleText.setText(title);
                                        bodyText.setText(body);

                                        timeDeltaThreadText.setText(timeDelta);
                                        timeText.setText(timeString);

                                        // To be done: code upvote function
                                        if (!upvoted) {
                                            upvoteOnIcon.setVisibility(View.GONE);
                                            upvoteOffIcon.setVisibility(View.VISIBLE);
                                        }
                                        else {
                                            upvoteOnIcon.setVisibility(View.VISIBLE);
                                            upvoteOffIcon.setVisibility(View.GONE);
                                        }


                                        upvoteCountText.setText(upvoteCount);

                                        replyCountText.setText(replyCount);
                                        if (replyCount.equals("1")) {
                                            replyDisplayText.setText("Reply");
                                        }
                                        else {
                                            replyDisplayText.setText("Replies");
                                        }





                                } catch (Exception e) {
                                    e.printStackTrace();
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
    }
    public void onThreadUpvoteClick(View view) {
        mDb.collection(MODULES)
                .document(courseId)
                .collection("thread")
                .document(documentId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<HashMap<String, String>> commentArrayList = new ArrayList<>();
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                try {
                                    ArrayList<String> upvotedUserids = (ArrayList<String>) document.get("upvotedUserids");
                                    Boolean upvoted = upvotedUserids.contains(fbuser.getUid());

                                    String upvoteCount = document.get("upvotes").toString();


                                    if (!upvoted) {
                                        Map<String, String> upvotes = new HashMap<>();
                                        upvotes.put("upvotes", Integer.toString(Integer.parseInt(upvoteCount) + 1));
                                        mDb.collection(MODULES)
                                                .document(courseId)
                                                .collection("thread")
                                                .document(documentId)
                                                .set(upvotes, SetOptions.merge());
                                        mDb.collection(MODULES)
                                                .document(courseId)
                                                .collection("thread")
                                                .document(documentId)
                                                .update("upvotedUserids", FieldValue.arrayUnion(fbuser.getUid()))
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        updateThreadDisplay();
                                                    }
                                                });
                                    }
                                    else {
                                        Map<String, String> upvotes = new HashMap<>();
                                        upvotes.put("upvotes", Integer.toString(Integer.parseInt(upvoteCount) - 1));
                                        mDb.collection(MODULES)
                                                .document(courseId)
                                                .collection("thread")
                                                .document(documentId)
                                                .set(upvotes, SetOptions.merge());

                                        mDb.collection(MODULES)
                                                .document(courseId)
                                                .collection("thread")
                                                .document(documentId)
                                                .update("upvotedUserids", FieldValue.arrayRemove(fbuser.getUid()))
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        updateThreadDisplay();
                                                    }
                                                });
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);
        getSupportActionBar().hide();

        initData();
        initRecyclerView("text");
        Intent intent = getIntent();
        courseId = intent.getStringExtra("course").substring(0, 6);    // s
        documentId = intent.getStringExtra("course").substring(6, 26);   // s1

        Log.d("coursename", courseId);
        Log.d("documentId", documentId);

        updateThreadDisplay();
        updateCommentDisplay();


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
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

    }
    private void initData() {
        commentList= new ArrayList<>();
        //userList.add(new ModelClass(R.drawable.ic_baseline_person_outline_24,"fullscapdeveloper","time","comments"));


    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onSubmitClickComment(View view) {
        Intent intent = getIntent();
        //String title = intent.getStringExtra("threadtitle");
        courseId=intent.getStringExtra("course").substring(0,6);
        documentId=intent.getStringExtra("course").substring(6);
        EditText commentEditText = findViewById(R.id.commentBox);//get the name out from the "nameEditText"box


        String commentString = commentEditText.getText().toString();
        HashMap<String, String> commentObj = new HashMap<>();
        commentObj.put("content", commentString);
        commentObj.put("upvoteCount", "0");
        commentObj.put("userid", fbuser.getUid());
        commentObj.put("username", fbuser.getDisplayName());
        commentObj.put("time", Instant.now().toString());

//        int age = Integer.parseInt(ageString);

//        Log.d(TAG, "Submitted name: " + m.getName() + ", Cousecode: " + m.getCoursecode());
        mDb.collection(MODULES)
                .document(courseId)
                .collection("thread")
                .document(documentId)
                .update("comments", FieldValue.arrayUnion(commentObj));
        //.add(p) .add generates random ID
        RecyclerView commentlistview = findViewById(R.id.commentList);
        adapter.clear();
        updateCommentDisplay();
        updateThreadDisplay();


//        mDb.collection(MODULES)
//                .document(courseId)
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
    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            /*new AlertDialog.Builder(viewHolder.itemView.getContext())
                    .setMessage("Are you sure?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id) {
                // Get the position of the item to be deleted
                    int position = viewHolder.getAdapterPosition();
                // Then you can remove this item from the adapter
                    })*/
            Intent intent = getIntent();
            String courseId = intent.getStringExtra("course").substring(0, 6);    // s
            String documentId = intent.getStringExtra("course").substring(6, 26);   // s1


            mDb.collection(MODULES)
                    .document(courseId)
                    .collection("thread")
                    .document(documentId)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                String struserid=document.get("userid").toString();
                                Log.d("checkinguid(thread)",struserid) ;
                                Log.d("checkinguid(currentuser)",fbuserid) ;
                                ArrayList<HashMap<String, String>> commentArrayList = (ArrayList<HashMap<String, String>>) document.get("comments");
                                Collections.reverse(commentArrayList);
                                String struseridcomment=commentArrayList.get(viewHolder.getAdapterPosition()).get("userid");
                                Log.d("checkinguid(comment)",struseridcomment);
                                if(struseridcomment.equals(fbuserid)) {
                                    new AlertDialog.Builder(viewHolder.itemView.getContext())
                                            .setMessage("Deleting comment, are you sure?")
                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    //Log.d("course123", userList.get(viewHolder.getAdapterPosition()).getTextview2().substring(0, 6));
                                                    //Log.d("course123", userList.get(viewHolder.getAdapterPosition()).getTextview2().substring(6, 26));

                                                    //hi brook this is the part to remove the comment
                                                    //ok, sure
                                                    mDb.collection(MODULES)
                                                            .document(courseId)
                                                            .collection("thread")
                                                            .document(documentId)
                                                            .update("comments", FieldValue.arrayRemove(commentArrayList.get(viewHolder.getAdapterPosition())))
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    commentList.remove(viewHolder.getAdapterPosition());//get position of the thread obj to remove
                                                                    updateThreadDisplay();
                                                                    updateCommentDisplay();

                                                                }
                                                            });
                                                    Log.d("id", Integer.toString(viewHolder.getAdapterPosition()));



                                                }
                                            })
                                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    // User cancelled the dialog,
                                                    // so we will refresh the adapter to prevent hiding the item from UI
                                                    adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                                                }
                                            })
                                            .create()
                                            .show();
                                }
                                else{
                                    Log.d("checkinguid3","unable to delete!!!");
                                    adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                                }

                            }
                        }
                    });



            //if(fbuser.getUid()){}

        }




    };

    }
