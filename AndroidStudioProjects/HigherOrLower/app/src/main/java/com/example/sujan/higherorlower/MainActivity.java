package com.example.sujan.higherorlower;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    int correctNum;

    public void generateRandomNumber() {
        Random rand = new Random();
        correctNum = rand.nextInt(20) + 1;
    }

    public void compare(View view) {
        EditText editText = (EditText) findViewById(R.id.editText);
        Log.i("Correct number", Integer.toString(correctNum));
        int guessedNum = Integer.parseInt(editText.getText().toString());

        String message;
        if (correctNum > guessedNum) {
            message = "Higher!";
        }
        else if (correctNum < guessedNum) {
            message = "Lower!";
        }
        else {
            message = "You got it right! Try again!";
            generateRandomNumber();
        }
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        generateRandomNumber();
    }
}
