package com.example.sujan.languagepreferences;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    public void setLanguage(String language) {
        sharedPreferences.edit().putString("language", language).apply();
        String storedLanguage = sharedPreferences.getString("language", "None");
        TextView languageTextView = (TextView) findViewById(R.id.languageTextView);
        languageTextView.setText(storedLanguage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.english:
                setLanguage("English");
                return true;
            case R.id.french:
                setLanguage("French");
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("com.example.sujan.languagepreferences", Context.MODE_PRIVATE);
        String language = sharedPreferences.getString("language", "None");

        if (language.equals("None")) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_btn_speak_now)
                    .setTitle("Set language preferences")
                    .setMessage("Please select a language")
                    .setPositiveButton("French", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            setLanguage("French");
                        }
                    })
                    .setNegativeButton("English", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            setLanguage("English");
                        }
                    })
                    .show();
        }
        else {
            TextView languageTextView = (TextView) findViewById(R.id.languageTextView);
            languageTextView.setText(sharedPreferences.getString("language", "None"));
        }

    }
}
