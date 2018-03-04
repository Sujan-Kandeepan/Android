package com.example.sujan.basicphrases;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    public void speakPhrase(View view) {
        Button buttonpressed = (Button) view;

        Log.i("Button pressed", buttonpressed.getText().toString());

        MediaPlayer.create(this, getResources().getIdentifier(buttonpressed.getTag().toString(),
                "raw", getPackageName())).start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
