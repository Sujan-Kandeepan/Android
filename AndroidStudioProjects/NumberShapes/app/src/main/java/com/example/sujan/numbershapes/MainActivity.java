package com.example.sujan.numbershapes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    class Number {
        int value;

        public boolean isTriangular() {
            int triangularNumber = 0, iterator = 1;
            while (value >= triangularNumber) {
                triangularNumber += iterator;
                iterator++;
                if (value == triangularNumber) {
                  return true;
                }
            }
            return false;
        }

        public boolean isSquare() {
            return Math.floor(Math.sqrt(value)) == Math.sqrt(value);
        }
    }

    public void determineShape(View view) {
        Log.i("Info", "Button pressed");
        EditText editText = (EditText) findViewById(R.id.editText);
        Log.i("Number entered", editText.getText().toString());

        String message;
        if (editText.getText().toString().isEmpty()) {
            message = "Please enter a number";
        }
        else {
            Number number = new Number();
            number.value = Integer.parseInt(editText.getText().toString());
            Log.i("Is triangular", Boolean.toString(number.isTriangular()));
            Log.i("Is square", Boolean.toString((number.isSquare())));

            message = editText.getText().toString();
            if (number.isTriangular() & number.isSquare()) {
                message += " is both triangular and square";
            }
            else if (number.isTriangular()) {
                message += " is triangular but not square";
            }
            else if (number.isSquare()) {
                message += " is not triangular but square";
            }
            else {
                message += " is neither triangular nor square";
            }
        }
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
