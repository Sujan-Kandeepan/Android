package com.example.sujan.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import static java.util.Arrays.asList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.sujan.sharedpreferences", Context.MODE_PRIVATE);

        /*
        sharedPreferences.edit().putString("username", "sujan").apply();
        String username = sharedPreferences.getString("username", "");
        Log.i("Username", username);
        */

        ArrayList<String> friends = new ArrayList<>(asList("Jaison", "Dipinjit", "Jefin", "Yash", "Abhi", "Rahee"));
        try {
            sharedPreferences.edit().putString("friends", ObjectSerializer.serialize(friends)).apply();
            Log.i("Friends", ObjectSerializer.serialize(friends));
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList newFriends = new ArrayList<String>();
        try {
            newFriends = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("friends", ObjectSerializer.serialize(new ArrayList<String>())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("New Friends", newFriends.toString());
    }
}
