package com.example.gmodsv1;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FirestoreListActivity extends AppCompatActivity {

    private FirebaseFirestore mDb = FirebaseFirestore.getInstance();
    //private static final String TAG = "FirestoreListActivity";
    private static final String MODULES = "modules";

    private ArrayAdapter<moduleclass> adapter;

    OkHttpClient client = new OkHttpClient();

    class FetchUpdateSearchList extends AsyncTask<Request, Void, Response> {

        @Override
        protected Response doInBackground(Request... requests) {
            Response response = null;
            try {
                response = client.newCall(requests[0]).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);

            ListView moduleListView = findViewById(R.id.moduleListView);
            adapter = new ArrayAdapter<moduleclass>(
                    FirestoreListActivity.this,//activity is this
                    android.R.layout.simple_list_item_1,//how it list(text view, display and module.toString
                    //if toString() method not defined in class, it will show class fullname@hex addr
                    new ArrayList<moduleclass>()
            );
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firestore_list);
        getSupportActionBar().hide();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView_search);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        //set home selected
        bottomNavigationView.setSelectedItemId(R.id.search);

        //perform item selected listener , switching between the activities when clicked
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.home:
                        startActivity(new Intent(FirestoreListActivity.this, UserProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.settings:
                        startActivity(new Intent(FirestoreListActivity.this , SettingActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
//        EditText searchBar =findViewById(R.id.searchbar);
//        searchBar.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                Log.d("firebasesearchbar","search box has changed to "+ editable.toString());
//            }
//        });




    }
    /*

        class PatientAdapter extends ArrayAdapter<Patient> {

            ArrayList<Patient> patients;
            PatientAdapter(Context context, ArrayList<Patient> patients) {
                super(context, 0, patients);
                this.patients = patients;
            }

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.patient_list_item, parent, false);
                }
                //layoutinflater reads xml file and converts it into java widgets
                TextView patientName = convertView.findViewById(R.id.itemName);
                TextView patientAge = convertView.findViewById(R.id.itemAge);

                Patient p = patients.get(position);
                patientName.setText(p.getName());
                patientAge.setText(Integer.toString(p.getAge()));

                return convertView;
            }
        }
*/


    public void onRefreshClick(View view) {

        EditText searchBar =findViewById(R.id.searchbar);
        String s=searchBar.getText().toString();
        Log.d("click and i got",s);

//        String url = "https://5c4c-89-187-162-120.ngrok.io/search?";
//        String fetchUrl = url + "querystr=" + queryStr + "&results=" + numResults;
//        Response response = null;
//
//        Request request = new Request.Builder()
//                .url(url)
//                .build();
        ListView moduleListView = findViewById(R.id.moduleListView);
        adapter = new ArrayAdapter<moduleclass>(
                FirestoreListActivity.this,//activity is this
                android.R.layout.simple_list_item_1,//how it list(text view, display and module.toString
                //if toString() method not defined in class, it will show class fullname@hex addr
                new ArrayList<moduleclass>()
        );
        mDb.collection(MODULES)
                //.whereEqualTo("coursecode",s.toString().toUpperCase())
                .whereGreaterThanOrEqualTo( "coursecode", s.toString().toUpperCase())
                .whereLessThanOrEqualTo( "coursecode", s.toString().toUpperCase()+ '\uf8ff')
                .get()//.get(); get all item
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ArrayList<moduleclass> moduleslist = new ArrayList<>();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            moduleclass m =document.toObject(moduleclass.class);
                            moduleslist.add(m);
                            Log.d("firebase", m.getCoursecode() + " "+m.getName()+" "+m.getAU());
                        } //for loop
                        adapter.clear();
                        adapter.addAll(moduleslist);
                        moduleListView.setAdapter(adapter);

                        moduleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                if(true){
                                    //startActivity(new Intent(FirestoreListActivity.this, listviewonclick2.class));
                                    Log.d("whatisthis",(String)adapterView.getItemAtPosition(i).toString().substring(0,6));
                                    Intent intent = new Intent(FirestoreListActivity.this,listviewonclick2.class);
                                    intent.putExtra("course",(String)adapterView.getItemAtPosition(i).toString().substring(0,6) );//s.toString().toUpperCase()
                                    startActivity(intent);

                                }
                            }
                        });
                    }

                });

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                ListView moduleListView = findViewById(R.id.moduleListView);
                adapter = new ArrayAdapter<moduleclass>(
                        FirestoreListActivity.this,//activity is this
                        android.R.layout.simple_list_item_1,//how it list(text view, display and module.toString
                        //if toString() method not defined in class, it will show class fullname@hex addr
                        new ArrayList<moduleclass>()
                );
                //adapter = new PatientAdapter(this, new ArrayList<>());

                Log.d("firebasesearchbar","search box has changed to "+ editable.toString());
                mDb.collection(MODULES)
                        //.whereEqualTo("coursecode",editable.toString().toUpperCase())
                        .whereGreaterThanOrEqualTo( "coursecode", editable.toString().toUpperCase())
                        .whereLessThanOrEqualTo( "coursecode", editable.toString().toUpperCase()+ '\uf8ff')
                        .get()//.get(); get all item
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                ArrayList<moduleclass> moduleslist = new ArrayList<>();
                                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                    moduleclass m =document.toObject(moduleclass.class);
                                    moduleslist.add(m);
                                    Log.d("firebase", m.getCoursecode() + " "+m.getName()+" "+m.getAU());
                                } //for loop
                                adapter.clear();
                                adapter.addAll(moduleslist);
                                moduleListView.setAdapter(adapter);

                                moduleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        if(true){
                                            //startActivity(new Intent(FirestoreListActivity.this, listviewonclick2.class));
                                            Intent intent = new Intent(FirestoreListActivity.this,listviewonclick2.class);
                                            intent.putExtra("course",(String)adapterView.getItemAtPosition(i).toString().substring(0,6));
                                            startActivity(intent);

                                        }
                                    }
                                });
                            }

                        });
            }

        });








    }

    /*
    public void onSubmitClick(View view) {
        EditText nameEditText = findViewById(R.id.nameEditText);//get the name out from the "nameEditText"box
        EditText coursecodeEditText = findViewById(R.id.coursecodeEditText);//get the name out from the "coursecodeEditText"box
        EditText AUEditText =findViewById(R.id.AUEditText);

        String name = nameEditText.getText().toString();
        String coursecode = coursecodeEditText.getText().toString();
        int AU = Integer.parseInt(AUEditText.getText().toString());
        //int age = Integer.parseInt(ageString);

        moduleclass m = new moduleclass(AU, coursecode, name);
        //Log.d(TAG, "Submitted name: " + m.getName() + ", Cousecode: " + m.getCoursecode());
        mDb.collection(MODULES).add(m);
                //.add(p) .add generates random ID

    }
    */




}