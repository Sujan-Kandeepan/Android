package com.example.sujan.timestables;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;

import java.util.ArrayList;

import static java.util.Arrays.asList;

public class MainActivity extends AppCompatActivity {

    ListView timesTablesListView;

    public void generateTimesTable(int timesTableNumber) {
        final ArrayList<Integer> timesTableContent = new ArrayList<Integer>();
        for (int num = 1; num <= 20; num++) {
            timesTableContent.add(timesTableNumber * num);
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, timesTableContent);
        timesTablesListView.setAdapter(arrayAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timesTablesListView = (ListView) findViewById(R.id.timesTableListView);

        generateTimesTable(1);

        final SeekBar timesTablesSeekBar = (SeekBar) findViewById(R.id.timesTableSeekBar);
        timesTablesSeekBar.setMax(20);
        timesTablesSeekBar.setProgress(1);
        timesTablesSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int min = 1;
                int timesTableNumber;
                if (i < min) {
                    timesTableNumber = 1;
                    timesTablesSeekBar.setProgress(1);
                }
                else {
                    timesTableNumber = i;
                }

                Log.i("Seekbar position", Integer.toString(seekBar.getProgress()));
                generateTimesTable(timesTableNumber);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
