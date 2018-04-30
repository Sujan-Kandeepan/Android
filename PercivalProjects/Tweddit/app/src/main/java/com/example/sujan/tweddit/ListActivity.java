package com.example.sujan.tweddit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListActivity extends AppCompatActivity {

    ListView listView;
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Intent intent = getIntent();
        String multireddit = intent.getStringExtra("multireddit");
        String dataString = intent.getStringExtra("data");
        dataString = dataString.replace ("[", "");
        dataString = dataString.replace ("]", "");
        dataString = dataString.replace ("\"", "");
        final String[] subreddits = dataString.split(",");

        final String[] urls = new String[subreddits.length];
        for (int i = 0; i < subreddits.length; i++) {
            urls[i] = "https://www.reddit.com/r/" + subreddits[i].toLowerCase() + "/";
        }

        TextView multiredditTextView = (TextView) findViewById(R.id.multiredditTextView);
        Log.i("Multireddit", multireddit);
        if (multireddit.equals("") || dataString.equals("[]")) {
            multiredditTextView.setText("Multireddit link not available.");
        }
        else {
            multiredditTextView.setText(Html.fromHtml(
                    "<a href=\""+ multireddit +"\">Multireddit link</a> "));
            multiredditTextView.setMovementMethod(LinkMovementMethod.getInstance());
        }

        listView = (ListView) findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_2,
                android.R.id.text1, subreddits) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text1.setText(subreddits[position]);
                text1.setTextSize(24);
                text2.setText(urls[position]);
                return view;
            }
        };
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
                intent.putExtra("subreddit", subreddits[i]);
                startActivity(intent);
            }
        });
    }
}
