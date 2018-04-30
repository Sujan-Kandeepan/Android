package com.example.sujan.hiddenuielementsdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public void show(View view) {
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setVisibility(View.VISIBLE);

        Button showButton = (Button) findViewById(R.id.showButton);
        showButton.setEnabled(false);

        Button hideButton = (Button) findViewById(R.id.hideButton);
        hideButton.setEnabled(true);
    }

    public void hide(View view) {
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setVisibility(View.INVISIBLE);

        Button showButton = (Button) findViewById(R.id.showButton);
        showButton.setEnabled(true);

        Button hideButton = (Button) findViewById(R.id.hideButton);
        hideButton.setEnabled(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        show(findViewById(R.id.showButton));
    }
}
