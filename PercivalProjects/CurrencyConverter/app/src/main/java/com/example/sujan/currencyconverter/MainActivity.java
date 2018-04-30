package com.example.sujan.currencyconverter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public void convertCurrency(View view) {

        Log.i("Info", "Button pressed");

        EditText editText = (EditText) findViewById(R.id.editText);

        String pounds = editText.getText().toString();

        double poundsDouble = Double.parseDouble(pounds);

        double dollarsDouble = poundsDouble * 1.3;

        String dollars = String.format("%.2f", dollarsDouble);
        
        Toast.makeText(this, "£" + pounds + " is $" + dollars, Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
