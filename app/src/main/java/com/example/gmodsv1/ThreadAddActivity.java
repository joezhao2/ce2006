package com.example.gmodsv1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThreadAddActivity extends AppCompatActivity {
    private FirebaseFirestore mDb = FirebaseFirestore.getInstance();
    private static final String MODULES = "modules";
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    List<ModelClass> userList;
    Adapter adapter;
    private Adapter.RecyclerViewClickListener listener;
    private FirebaseUser fbuser = FirebaseAuth.getInstance().getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_add);
        getSupportActionBar().hide();
        Intent intent = getIntent();
        String s = intent.getStringExtra("course");
    }
    public void onSubmitClickThread2(View view){
        EditText user=findViewById(R.id.useradd2);
        EditText thread=findViewById(R.id.threadadd2);
        String u=user.getText().toString();
        String t=thread.getText().toString();
        Intent intent = getIntent();
        String s = intent.getStringExtra("course");
        Map<String, Object> data = new HashMap<>();

        String id = mDb.collection(MODULES).document(s).collection("thread").document().getId();
        Log.d("id",id);
        data.put("comments", Arrays.asList(""));
        mDb.collection(MODULES)
                .document(s)
                .collection("thread")
                .document(id)
                .set(data);

        // List for upvoted user ids
        Map<String, Object> upvotedUserIds = new HashMap<>();
        upvotedUserIds.put("upvotedUserids", Arrays.asList(""));
        mDb.collection(MODULES)
                .document(s)
                .collection("thread")
                .document(id)
                .set(upvotedUserIds, SetOptions.merge());

        // Upvote count
        Map<String, Integer> upvotes = new HashMap<>();
        upvotes.put("upvotes", 0);
        mDb.collection(MODULES)
                .document(s)
                .collection("thread")
                .document(id)
                .set(upvotes, SetOptions.merge());

        Map<String, String> data2 = new HashMap<>();
        data2.put("userName",u);
        data2.put("message",t);
        data2.put("userid",fbuser.getUid());
        mDb.collection(MODULES)
                .document(s)
                .collection("thread")
                .document(id)
                .set(data2, SetOptions.merge());
        //initRecyclerView(s);
        //adapter.notifyDataSetChanged();
        intent = new Intent(ThreadAddActivity.this,listviewonclick2.class);
        intent.putExtra("course",s);
        startActivity(intent);

    }
}