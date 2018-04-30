package com.example.sujan.arsafety;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    ListView listView;
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Intent intent = getIntent();
        try {
            final ArrayList<String> objects = (ArrayList<String>) ObjectSerializer.deserialize(intent.getStringExtra("objects"));
            ArrayList<String> names = new ArrayList<String>();
            for (int i = 0; i < objects.size(); i++) {
                JSONObject jsonObject = new JSONObject(objects.get(i));
                String name = jsonObject.getString("name");
                name = name.substring(0, 1).toUpperCase() + name.substring(1);
                names.add(name);
            }
            listView = (ListView) findViewById(R.id.listView);

            arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, names);

            listView.setAdapter(arrayAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(getApplicationContext(), InfoActivity.class);
                    try {
                        JSONObject jsonObject = new JSONObject(objects.get(i));
                        intent.putExtra("score", jsonObject.getDouble("score"));
                        intent.putExtra("ID", jsonObject.getInt("ID"));
                        intent.putExtra("name", jsonObject.getString("name"));
                        intent.putExtra("description", jsonObject.getString("description"));
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
