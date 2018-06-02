package com.example.sujan.roundorsquare;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends WearableActivity {

    private TextView mTextView;

    public void roundOrSquare(View view) {
        if (getResources().getConfiguration().isScreenRound()) {
            Toast.makeText(this, "Round!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Square!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.text);

        // Enables Always-on
        setAmbientEnabled();
    }
}
