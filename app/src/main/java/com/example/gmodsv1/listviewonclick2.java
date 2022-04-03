package com.example.gmodsv1;

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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class listviewonclick2 extends AppCompatActivity {
    private FirebaseFirestore mDb = FirebaseFirestore.getInstance();
    private static final String MODULES = "modules";
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    List<ModelClass> userList;
    Adapter adapter;
    private Adapter.RecyclerViewClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listviewonclick2);
        getSupportActionBar().hide();
        Intent intent = getIntent();
        String s = intent.getStringExtra("course");

        initData();
        initRecyclerView("text");





        mDb.collection(MODULES)
                .document(s)
                .collection("thread")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.exists()) {
                                    String title = document.get("message").toString();
                                    String user = document.get("userName").toString();
                                    //Do what you need to do with your ArrayList
                                    ArrayList<String> arrayListcomments = (ArrayList<String>) document.get("comments");
                                    for (String s : arrayListcomments) {
                                        Log.d("comments", s);
                                    }
                                    ModelClass m= new ModelClass(R.drawable.photo6208635785310221009,user, s+document.getId(), title);
                                    userList.add(m);
                                    initRecyclerView(s+document.getId());
                                    Log.d("name+id",s+document.getId());


                                }
                            }
                        }
                    }
                });
                /*.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ArrayList<String> arrayList = (ArrayList<String>) document.get("comments");
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Thread t = document.toObject(Thread.class);

                        }
                    }

                */

    }

    private void initRecyclerView(String s) {
        recyclerView=findViewById(R.id.recyclerviewheader);
        layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(recyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new Adapter(userList,listener);
        //userList.clear();
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        setOnClickListener(s);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
    }

    private void setOnClickListener(String s) {
        listener=new Adapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(listviewonclick2.this,ThreadActivity.class);
                intent.putExtra("threadtitle",userList.get(position).getTextview3());
                intent.putExtra("course",userList.get(position).getTextview2());
                startActivity(intent);
            }
        };
    }

    private void initData() {
        userList= new ArrayList<>();
        //userList.add(new ModelClass(R.drawable.ic_baseline_person_outline_24,"fullscapdeveloper","time","comments"));

        
    }
    public void onSubmitClickThread(View view){
        /*
        EditText user=findViewById(R.id.useradd);
        EditText thread=findViewById(R.id.threadadd);
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


        Map<String, String> data2 = new HashMap<>();
        data2.put("userName",u);
        data2.put("message",t);
        mDb.collection(MODULES)
                .document(s)
                .collection("thread")
                .document(id)
                .set(data2, SetOptions.merge());
        //initRecyclerView(s);

        */
        Intent intent=getIntent();
        String s = intent.getStringExtra("course");
        Intent intent2 = new Intent(listviewonclick2.this,ThreadAddActivity.class);
        intent2.putExtra("course",s);
        //intent.putExtra("course",s.toString().toUpperCase());
        startActivity(intent2);


    }
    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            Log.d("course123",userList.get(viewHolder.getAdapterPosition()).getTextview2().substring(0,6));
            Log.d("course123",userList.get(viewHolder.getAdapterPosition()).getTextview2().substring(6,26));
            mDb.collection(MODULES)
                    .document(userList.get(viewHolder.getAdapterPosition()).getTextview2().substring(0,6))
                    .collection("thread")
                    .document(userList.get(viewHolder.getAdapterPosition()).getTextview2().substring(6))
                    .delete();
            userList.remove(viewHolder.getAdapterPosition());//get position of the thread obj to remove
            adapter.notifyDataSetChanged();



        }
    };
}