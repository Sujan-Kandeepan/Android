package com.example.sujan.databasedemo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            SQLiteDatabase myDatabase = this.openOrCreateDatabase("Users", MODE_PRIVATE, null);
            myDatabase.execSQL("CREATE TABLE IF NOT EXISTS newUsers (name VARCHAR, age INT(3), id INTEGER PRIMARY KEY)");
            myDatabase.execSQL("INSERT INTO newUsers (name, age) VALUES ('Sujan', 19)");
            myDatabase.execSQL("INSERT INTO newUsers (name, age) VALUES ('Janan', 13)");
            myDatabase.execSQL("DELETE FROM newUsers WHERE id = 2");

            Cursor c = myDatabase.rawQuery("SELECT * FROM newUsers", null);
            int nameIndex = c.getColumnIndex("name");
            int ageIndex = c.getColumnIndex("age");
            int idIndex = c.getColumnIndex("id");
            c.moveToFirst();

            while (c != null) {
                Log.i("Name", c.getString(nameIndex));
                Log.i("Age", c.getString(ageIndex));
                Log.i("Id", c.getString(idIndex));
                c.moveToNext();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        /*
        try {
            SQLiteDatabase myDatabase = this.openOrCreateDatabase("Events", MODE_PRIVATE, null);
            myDatabase.execSQL("CREATE TABLE IF NOT EXISTS events (name VARCHAR, year INT(4))");
            myDatabase.execSQL("INSERT INTO events (name, year) VALUES ('Sujan''s Birthday', 1998)");
            myDatabase.execSQL("INSERT INTO events (name, year) VALUES ('Janan''s Birthday', 2004)");
            myDatabase.execSQL("INSERT INTO events (name, year) VALUES ('Suji''s Birthday', 1967)");
            myDatabase.execSQL("INSERT INTO events (name, year) VALUES ('Kandee''s Birthday', 1964)");

            Cursor c = myDatabase.rawQuery("SELECT * FROM events", null);
            int nameIndex = c.getColumnIndex("name");
            int yearIndex = c.getColumnIndex("year");
            c.moveToFirst();

            while (c != null) {
                Log.i("Name", c.getString(nameIndex));
                Log.i("Year", c.getString(yearIndex));
                c.moveToNext();
            }
        }
        catch (Exception e) {

        }
        */
    }
}
