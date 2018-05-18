package com.example.sujan.braintrainer;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Button goButton;
    TextView timerTextView;
    TextView scoreTextView;
    TextView questionTextView;
    Button optionsButton1;
    Button optionsButton2;
    Button optionsButton3;
    Button optionsButton4;
    TextView statusTextView;
    Button playAgainButton;

    boolean gameActive = true;
    int correctAnswerLocation;
    int totalCorrect = 0;
    int totalAttempted = 0;

    CountDownTimer countDownTimer = new CountDownTimer(30000, 1000) {
        @Override
        public void onTick(long l) {
            timerTextView.setText((int) Math.ceil(l / 1000.0) + "s");
        }

        @Override
        public void onFinish() {
            gameActive = false;
            timerTextView.setText("0s");
            statusTextView.setVisibility(View.VISIBLE);
            statusTextView.setText("Done!");
            playAgainButton.setVisibility(View.VISIBLE);
        }
    };

    public void generateQuestion() {
        Random random = new Random();

        int operand1 = random.nextInt(21), operand2 = random.nextInt(21);
        questionTextView.setText(Integer.toString(operand1) + " + " + Integer.toString(operand2));

        int[] options = new int[4];
        for (int i = 0; i < 4; i++) {
            int wrongAnswer;
            do {
                wrongAnswer = random.nextInt(40) + 1;
                options[i] = wrongAnswer;
            } while (wrongAnswer == operand1 + operand2);
        }

        correctAnswerLocation = random.nextInt(4);
        options[correctAnswerLocation] = operand1 + operand2;

        optionsButton1.setText(Integer.toString(options[0]));
        optionsButton2.setText(Integer.toString(options[1]));
        optionsButton3.setText(Integer.toString(options[2]));
        optionsButton4.setText(Integer.toString(options[3]));
    }

    public void startGame(View view) {
        goButton.setVisibility(View.GONE);
        timerTextView.setVisibility(View.VISIBLE);
        scoreTextView.setVisibility(View.VISIBLE);
        questionTextView.setVisibility(View.VISIBLE);
        optionsButton1.setVisibility(View.VISIBLE);
        optionsButton2.setVisibility(View.VISIBLE);
        optionsButton3.setVisibility(View.VISIBLE);
        optionsButton4.setVisibility(View.VISIBLE);
        countDownTimer.start();
    }

    public void optionSelected(View view) {
        if (gameActive) {
            String correctTag = "option" + Integer.toString(correctAnswerLocation + 1);
            statusTextView.setVisibility(View.VISIBLE);
            if (view.getTag().equals(correctTag)) {
                totalCorrect++;
                statusTextView.setText("Correct!");
            }
            else {
                statusTextView.setText("Wrong :(");
            }
            totalAttempted++;
            scoreTextView.setText(Integer.toString(totalCorrect) + "/" + Integer.toString(totalAttempted));
            if (totalAttempted >= 10) scoreTextView.setTextSize(36);
            if (totalAttempted >= 100) scoreTextView.setTextSize(24);
            generateQuestion();
        }
    }

    public void playAgain(View view) {
        gameActive = true;
        statusTextView.setVisibility(View.INVISIBLE);
        playAgainButton.setVisibility(View.INVISIBLE);
        totalCorrect = 0;
        totalAttempted = 0;
        countDownTimer.cancel();
        countDownTimer.start();
        scoreTextView.setText("0/0");
        scoreTextView.setTextSize(48);
        generateQuestion();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        goButton = (Button) findViewById(R.id.goButton);
        goButton.setVisibility(View.VISIBLE);

        timerTextView = (TextView) findViewById(R.id.timerTextView);
        timerTextView.setVisibility(View.INVISIBLE);

        scoreTextView = (TextView) findViewById(R.id.scoreTextView);
        scoreTextView.setVisibility(View.INVISIBLE);

        questionTextView = (TextView) findViewById(R.id.questionTextView);
        questionTextView.setVisibility(View.INVISIBLE);

        optionsButton1 = (Button) findViewById(R.id.optionButton1);
        optionsButton1.setVisibility(View.INVISIBLE);

        optionsButton2 = (Button) findViewById(R.id.optionButton2);
        optionsButton2.setVisibility(View.INVISIBLE);

        optionsButton3 = (Button) findViewById(R.id.optionButton3);
        optionsButton3.setVisibility(View.INVISIBLE);

        optionsButton4 = (Button) findViewById(R.id.optionButton4);
        optionsButton4.setVisibility(View.INVISIBLE);

        statusTextView = (TextView) findViewById(R.id.statusTextView);
        statusTextView.setVisibility(View.INVISIBLE);

        playAgainButton = (Button) findViewById(R.id.playAgainButton);
        playAgainButton.setVisibility(View.INVISIBLE);

        generateQuestion();
    }
}
