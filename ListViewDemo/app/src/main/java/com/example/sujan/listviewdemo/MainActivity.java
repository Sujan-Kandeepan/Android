package com.example.sujan.listviewdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import static java.util.Arrays.asList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView friendListView = (ListView) findViewById(R.id.friendListView);

        final ArrayList<String> myFriends = new ArrayList<String>(asList(
                "Jaison", "Dipinjit", "Jefin", "Yash", "Abhi", "Rahee"));

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, myFriends);

        friendListView.setAdapter(arrayAdapter);
        friendListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainActivity.this, "Hello " + myFriends.get(i), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
