package com.example.sujan.androidweardemo;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends WearableActivity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.text);

        if (getResources().getConfiguration().isScreenRound()) {
            Log.i("Device", "Round!");
        } else {
            Log.i("Device", "Square!");
        }

        // Enables Always-on
        setAmbientEnabled();
    }
}
