package com.example.sujan.eggtimer;

import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    SeekBar durationSeekar;
    TextView timeRemainingTextView;
    Button startButton;
    CountDownTimer countDownTimer;
    MediaPlayer mediaPlayer;
    boolean timerRunning = false;

    public void displayTimeRemaining(int timeRemaining) {
        timeRemainingTextView.setText(String.format("%1d:%02d", timeRemaining / 60, timeRemaining % 60));
    }

    public void startTimer(View view) {
        if (timerRunning) {
            startButton.setText("Go!");
            timerRunning = false;
            displayTimeRemaining(30);
            durationSeekar.setEnabled(true);
            durationSeekar.setProgress(30);
            countDownTimer.cancel();
        }
        else {
            startButton.setText("Stop!");
            timerRunning = true;
            durationSeekar.setEnabled(false);
            mediaPlayer = MediaPlayer.create(this, R.raw.airhorn);

            countDownTimer = new CountDownTimer(
                    durationSeekar.getProgress() * 1000, 1000) {
                public void onTick(long millisecondsUntilDone) {
                    displayTimeRemaining((int) Math.ceil(millisecondsUntilDone / 1000.0));
                }

                public void onFinish() {
                    mediaPlayer.start();
                    startTimer(findViewById(R.id.startButton));
                }
            }.start();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        durationSeekar = (SeekBar) findViewById(R.id.durationSeekBar);
        durationSeekar.setMax(300);
        durationSeekar.setProgress(30);
        durationSeekar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                displayTimeRemaining(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        timeRemainingTextView = (TextView) findViewById(R.id.timeRemainingTextView);

        startButton = (Button) findViewById(R.id.startButton);

        displayTimeRemaining(30);
    }
}
