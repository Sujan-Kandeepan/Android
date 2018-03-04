package com.example.sujan.timerdemo;

import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new CountDownTimer(10000, 100) {
            public void onTick(long millisecondsUntilDone) {
                Log.i("Seconds left", String.valueOf(millisecondsUntilDone / 1000));
            }

            public void onFinish() {
                Log.i("We're done", "No more countdown");
            }
        }.start();

        /*
        final Handler handler = new Handler();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Log.i("Info", "A second has passed by");

                handler.postDelayed(this, 1000);
            }
        };

        handler.post(runnable);
        */
    }
}
