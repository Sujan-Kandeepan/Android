package com.example.sujan.notes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

public class EditActivity extends AppCompatActivity {

    EditText contentEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Intent intent = getIntent();
        final int index = intent.getIntExtra("index", -1);
        String content = intent.getStringExtra("content");

        contentEditText = (EditText) findViewById(R.id.contentEditText);
        contentEditText.setText(content);
        contentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (index != -1) {
                    MainActivity.notes.set(index, contentEditText.getText().toString());
                    MainActivity.arrayAdapter.notifyDataSetChanged();
                    MainActivity.notesListView.invalidateViews();
                    try {
                        MainActivity.sharedPreferences.edit().putString("notes", ObjectSerializer.serialize(MainActivity.notes)).apply();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(EditActivity.this, "Note not updated!", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(EditActivity.this, "Note not found!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
