package com.example.sujan.animations;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    public void fade (View view) {
        Log.i("Info", "ImageView tapped");

        ImageView bartImageview = (ImageView) findViewById(R.id.bartImageView);
        ImageView homerImageView = (ImageView) findViewById(R.id.homerImageView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView bartImageview = (ImageView) findViewById(R.id.bartImageView);
        bartImageview.setX(-1000);
        bartImageview.animate().translationXBy(1000).rotation(3600).setDuration(2000);
    }
}
