package com.example.gmodsv1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.cardview.widget.CardView;
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
    private FirebaseUser fbuser = FirebaseAuth.getInstance().getCurrentUser();
    private String fbuserid =fbuser.getUid();

    @Override
    protected void onResume() {
        super.onResume();
        adapter.clear();
        updateThreadList();
        Log.d("uh", "resumed");
    }

    String courseId;
    public void updateThreadList() {
        adapter.clear();
        final boolean[] noThreads = {true};
        mDb.collection(MODULES)
                .document(courseId)
                .collection("thread")
                .orderBy("time")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.exists()) {
                                    try {
                                        document = document;
                                        String title = document.get("title").toString();
                                        String user = document.get("username").toString();
                                        String timeStr = TimeFormatter.getStringTimeDelta(Instant.parse(document.get("time").toString()), Instant.now());
                                        String upvotes = document.get("upvotes").toString();
                                        ArrayList<HashMap<String, String>> commentArrayList = (ArrayList<HashMap<String, String>>) document.get("comments");
                                        String replies = Integer.toString(commentArrayList.size() - 1);

                                        ModelClass m= new ModelClass(R.drawable.default_profile_pic,user, courseId+document.getId(), title, upvotes, replies, timeStr);
                                        userList.add(m);
                                        initRecyclerView(courseId+document.getId());
                                        Log.d("name+id",courseId+document.getId());
                                        noThreads[0] = false;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                        Log.d("thread", String.valueOf(noThreads[0]));
                        if(noThreads[0]) {
                            CardView noCommentDisplay = findViewById(R.id.noThreadDisplay);
                            noCommentDisplay.setVisibility(View.VISIBLE);

                            RecyclerView threadsList = findViewById(R.id.recyclerviewheader);
                            threadsList.setVisibility(View.GONE);
                        }
                        else {
                            CardView noCommentDisplay = findViewById(R.id.noThreadDisplay);
                            noCommentDisplay.setVisibility(View.GONE);

                            RecyclerView threadsList = findViewById(R.id.recyclerviewheader);
                            threadsList.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listviewonclick2);
        getSupportActionBar().hide();
        Intent intent = getIntent();
        courseId = intent.getStringExtra("course");
        TextView coursecodebig = findViewById(R.id.mainview);
        coursecodebig.setText(courseId);
        initData();
        initRecyclerView("text");
        TextView coursenameundercoursecode =findViewById(R.id.textView5);
        mDb.collection(MODULES)
                .document(courseId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            String coursename = document.get("name").toString();
                            coursenameundercoursecode.setText(coursename);
                        }
                    }
                });
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
        initRecyclerView(s);
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

        mDb.collection(MODULES)
                    .document(userList.get(viewHolder.getAdapterPosition()).getTextview2().substring(0, 6))
                    .collection("thread")
                    .document(userList.get(viewHolder.getAdapterPosition()).getTextview2().substring(6))
                    //.collection("userid")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                               @Override
                                               public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                   if (task.isSuccessful()) {
                                                       DocumentSnapshot document = task.getResult();
                                                       String struserid=document.get("userid").toString();
                                                       Log.d("checkinguid",struserid) ;
                                                       Log.d("checkinguid2",fbuserid) ;
                                                       if(struserid.equals(fbuserid)) {
                                                           new AlertDialog.Builder(viewHolder.itemView.getContext())
                                                                   .setMessage("Deleting thread, are you sure?")
                                                                   .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                                       @Override
                                                                       public void onClick(DialogInterface dialogInterface, int i) {
                                                                           Log.d("course123", userList.get(viewHolder.getAdapterPosition()).getTextview2().substring(0, 6));
                                                                           Log.d("course123", userList.get(viewHolder.getAdapterPosition()).getTextview2().substring(6, 26));
                                                                           mDb.collection(MODULES)
                                                                                   .document(userList.get(viewHolder.getAdapterPosition()).getTextview2().substring(0, 6))
                                                                                   .collection("thread")
                                                                                   .document(userList.get(viewHolder.getAdapterPosition()).getTextview2().substring(6))
                                                                                   .delete();
                                                                           userList.remove(viewHolder.getAdapterPosition());//get position of the thread obj to remove
                                                                           adapter.notifyDataSetChanged();


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
                                                           AlertDialog.Builder builder = new AlertDialog.Builder(listviewonclick2.this);
                                                           builder.setMessage("Unable to delete other user's thread")
                                                                   .setPositiveButton("OK", null)
                                                                   .create()
                                                                   .show();
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