package com.example.sujan.arsafety;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Intent intent = getIntent();
        double score = intent.getDoubleExtra("score", 0);
        int id = intent.getIntExtra("ID", 0);
        String name = intent.getStringExtra("name");
        String description = intent.getStringExtra("description");

        name = name.substring(0, 1).toUpperCase() + name.substring(1);

        TextView itemTextView = findViewById(R.id.itemTextView);
        itemTextView.setText(name);

        TextView moreTextView = findViewById(R.id.moreTextView);
        moreTextView.setText("ID: " + id + "   Accuracy: " + Long.toString(Math.round(score * 100)) + "%");

        TextView descriptionTextView = findViewById(R.id.descriptionTextView);
        descriptionTextView.setText(description.toUpperCase());

    }
}
