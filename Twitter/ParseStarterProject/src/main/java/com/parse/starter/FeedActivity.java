package com.parse.starter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        setTitle("Twitter: Your Feed");

        final List<Map<String, String>> tweetData = new ArrayList<>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Tweet");
        query.whereContainedIn("user", ParseUser.getCurrentUser().getList("following"));
        query.orderByDescending("createdAt");
        query.setLimit(20);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    for (ParseObject tweet : objects) {
                        Map<String, String> tweetInfo = new HashMap<>();
                        tweetInfo.put("content", tweet.getString("tweet"));
                        tweetInfo.put("user", tweet.getString("user"));
                        tweetData.add(tweetInfo);
                    }

                    SimpleAdapter adapter = new SimpleAdapter(FeedActivity.this, tweetData,
                            android.R.layout.simple_list_item_2,
                            new String[] {"content", "user"},
                            new int[] {android.R.id.text1, android.R.id.text2});

                    ListView listView = findViewById(R.id.listView);
                    listView.setAdapter(adapter);
                }
            }
        });
    }
}
