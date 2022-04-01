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
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpEntity;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpResponse;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.NameValuePair;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.HttpClient;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.entity.UrlEncodedFormEntity;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.methods.HttpPost;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.impl.client.HttpClients;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.message.BasicNameValuePair;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class testActivity extends AppCompatActivity {
    private ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }
    public void onTestButtonClick(View view){
        findViewById(R.id.buttonTest);
        ListView testListView = findViewById(R.id.testListView);
        adapter = new ArrayAdapter<String>(
                testActivity.this,//activity is this
                android.R.layout.simple_list_item_1,//how it list(text view, display and module.toString
                //if toString() method not defined in class, it will show class fullname@hex addr
                new ArrayList<String>()
        );
        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost("http://5952-89-187-162-120.ngrok.io/new-thread");
        // Request parameters and other properties.
        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair("user_id", "000001"));
        params.add(new BasicNameValuePair("course_id", "ce2006"));
        params.add(new BasicNameValuePair("content", "dummy data"));
        //httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

//Execute and get the response.
        //HttpResponse response = httpclient.execute(httppost);
        //HttpEntity entity = response.getEntity();
    }
}