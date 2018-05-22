package com.example.sujan.pipfun;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onPictureInPictureModeChanged(
            boolean isInPictureInPictureMode, Configuration newConfig) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);

        TextView textView = findViewById(R.id.textView);
        Button pipButton = findViewById(R.id.pipButton);

        if (isInPictureInPictureMode) { // Going into PiP
            pipButton.setVisibility(View.GONE);
            getSupportActionBar().hide();
            textView.setText("Now in PiP mode!");
        } else { // Going out of PiP
            pipButton.setVisibility(View.VISIBLE);
            getSupportActionBar().show();
            textView.setText("Back in full screen!");
        }
    }

    public void goPiP (View view) {
        enterPictureInPictureMode();
    }
}
