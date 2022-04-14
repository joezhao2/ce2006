package com.example.gmodsv1;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.time.Instant;
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
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onSubmitClickThread2(View view){
        EditText user=findViewById(R.id.useradd2);
        EditText thread=findViewById(R.id.threadadd2);
        String b=user.getText().toString();
        String t=thread.getText().toString();
        final Intent[] intent = {getIntent()};
        String s = intent[0].getStringExtra("course");
        Map<String, Object> data = new HashMap<>();

        if (t.length() > 300) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ThreadAddActivity.this);
            builder.setMessage("Please limit title length to under 300 characters")
                    .setPositiveButton("OK", null)
                    .create()
                    .show();
        }
        else if (t.equals("")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ThreadAddActivity.this);
            builder.setMessage("Please enter a title")
                    .setPositiveButton("OK", null)
                    .create()
                    .show();
        }
        else if (b.length() > 300) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ThreadAddActivity.this);
            builder.setMessage("Please limit content length to under 300 characters")
                    .setPositiveButton("OK", null)
                    .create()
                    .show();
        }
        else {
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
            Map<String, String> upvotes = new HashMap<>();
            upvotes.put("upvotes", "0");
            mDb.collection(MODULES)
                    .document(s)
                    .collection("thread")
                    .document(id)
                    .set(upvotes, SetOptions.merge());

            Map<String, String> data2 = new HashMap<>();
            data2.put("username",fbuser.getDisplayName());
            data2.put("title",t);
            data2.put("userid",fbuser.getUid());
            data2.put("body",b);
            data2.put("time", Instant.now().toString());
            mDb.collection(MODULES)
                    .document(s)
                    .collection("thread")
                    .document(id)
                    .set(data2, SetOptions.merge())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            intent[0] = new Intent(ThreadAddActivity.this,listviewonclick2.class);
                            intent[0].putExtra("course",s);
                            startActivity(intent[0]);
                        }
                    });
            //initRecyclerView(s);
            //adapter.notifyDataSetChanged();
        }
    }
}