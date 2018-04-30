package com.example.sujan.connect3game;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    String activePlayer = "yellow";
    String[] gameState = {"empty", "empty", "empty", "empty", "empty", "empty", "empty", "empty", "empty"};
    int[][] winningPositions = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};
    boolean gameActive = true;

    public void dropIn(View view) {
        ImageView counter = (ImageView) view;

        int tappedCounter = Integer.parseInt(counter.getTag().toString());

        if (gameState[tappedCounter].equals("empty") && gameActive) {

            gameState[tappedCounter] = activePlayer;

            counter.setTranslationY(-1500);

            if (activePlayer.equals("yellow")) {
                counter.setImageResource(R.drawable.yellow);
                activePlayer = "red";
            } else {
                counter.setImageResource(R.drawable.red);
                activePlayer = "yellow";
            }

            counter.animate().translationYBy(1500).rotation(3600).setDuration(300);

            for (int[] winningPosition : winningPositions) {
                if (gameState[winningPosition[0]].equals(gameState[winningPosition[1]]) &&
                        gameState[winningPosition[1]].equals(gameState[winningPosition[2]]) &&
                        !gameState[winningPosition[0]].equals("empty")) {
                    gameActive = false;

                    String winner;
                    if (activePlayer.equals("red")) {
                        winner = "Yellow";
                    } else {
                        winner = "Red";
                    }

                    TextView winnerTextView = (TextView) findViewById(R.id.winnerTextView);
                    Button playAgainButton = (Button) findViewById(R.id.playAgainButton);

                    winnerTextView.setText(winner + " has won!");
                    playAgainButton.setVisibility(View.VISIBLE);
                    winnerTextView.setVisibility(view.VISIBLE);
                }
            }
        }
        else if (gameActive) {
            Toast.makeText(this, "Space already occupied!", Toast.LENGTH_SHORT).show();
        }
    }

    public void playAgain(View view) {
        TextView winnerTextView = (TextView) findViewById(R.id.winnerTextView);
        Button playAgainButton = (Button) findViewById(R.id.playAgainButton);

        playAgainButton.setVisibility(View.INVISIBLE);
        winnerTextView.setVisibility(view.INVISIBLE);

        GridLayout gridLayout = (GridLayout) findViewById(R.id.gridLayout);
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            ImageView counter = (ImageView) gridLayout.getChildAt(i);
            counter.setImageDrawable(null);
        }

        for (int i = 0; i < 9; i++) {
            gameState[i] = "empty";
        }
        gameActive = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
