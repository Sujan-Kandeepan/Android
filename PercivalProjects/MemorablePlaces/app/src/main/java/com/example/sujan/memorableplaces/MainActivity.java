package com.example.sujan.memorableplaces;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static java.util.Arrays.asList;

public class MainActivity extends AppCompatActivity {

    static ListView listView;
    static ArrayAdapter arrayAdapter;
    static ArrayList<LatLng> locations = new ArrayList<LatLng>();
    static ArrayList<String> locationNames = new ArrayList<String>();

    public static void addLocation(LatLng latLng, String address) {
        locations.add(latLng);
        locationNames.add(address);
        listView.invalidateViews();
        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);

        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.sujan.memorableplaces", Context.MODE_PRIVATE);

        ArrayList<String> latitudes = new ArrayList<String>();
        ArrayList<String> longitudes = new ArrayList<String>();

        try {
            latitudes.clear();
            longitudes.clear();
            locations.clear();
            locationNames.clear();

            latitudes = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("latitudes", ObjectSerializer.serialize(new ArrayList<String>())));
            longitudes = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("longitudes", ObjectSerializer.serialize(new ArrayList<String>())));
            locationNames = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("locationNames", ObjectSerializer.serialize(new ArrayList<String>(asList("Add a new location")))));
            Log.i("Names", locationNames.toString());
            Log.i("Latitudes", latitudes.toString());
            Log.i("Longitudes", longitudes.toString());

            if (latitudes.size() > 0 && longitudes.size() > 0 && locationNames.size() > 1) {
                if (latitudes.size() + 1 == locationNames.size() && longitudes.size() + 1 == locationNames.size()) {
                    for (int i = 0; i < latitudes.size(); i++) {
                        locations.add(new LatLng(Double.parseDouble(latitudes.get(i)), Double.parseDouble(longitudes.get(i))));
                    }
                }
            }
            Log.i("Number of locations", Integer.toString(locations.size()));
        } catch (IOException e) {
            e.printStackTrace();
        }


        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, locationNames);

        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("ListView index", i);
                startActivity(intent);
            }
        });
    }
}
