package com.parse.starter;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    ListView chatListView;
    EditText messageEditText;
    Button sendButton;

    String activeUser;
    ArrayList<String> messages;
    ArrayAdapter<String> adapter;
    Handler handler;

    public void getMessages() {
        ParseQuery<ParseObject> sentQuery = ParseQuery.getQuery("Message");
        sentQuery.whereEqualTo("sender", ParseUser.getCurrentUser().getUsername());
        sentQuery.whereEqualTo("recipient", activeUser);

        ParseQuery<ParseObject> receivedQuery = ParseQuery.getQuery("Message");
        receivedQuery.whereEqualTo("sender", activeUser);
        receivedQuery.whereEqualTo("recipient", ParseUser.getCurrentUser().getUsername());

        ArrayList<ParseQuery<ParseObject>> queries = new
                ArrayList<>(Arrays.asList(sentQuery, receivedQuery));

        ParseQuery<ParseObject> query = ParseQuery.or(queries);
        query.orderByAscending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && !objects.isEmpty()) {
                    messages.clear();
                    for (ParseObject message : objects) {
                        String messageContent = message.getString("message");
                        if (message.getString("sender")
                                .equals(ParseUser.getCurrentUser().getUsername())) {
                            messageContent = ParseUser.getCurrentUser().getUsername()
                                    + ": " + messageContent;
                        } else {
                            messageContent = activeUser + ": " + messageContent;
                        }
                        messages.add(messageContent);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getMessages();
                Log.i("Status", "Requested update");
            }
        }, 2000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatListView = findViewById(R.id.chatListView);
        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);

        Intent intent = getIntent();
        activeUser = intent.getStringExtra("username");
        setTitle("Chat with " + activeUser);
        Log.i("Active User", activeUser);

        messages = new ArrayList<>();
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, messages);
        chatListView.setAdapter(adapter);

        getMessages();
    }

    public void sendMessage(View view) {
        ParseObject message = new ParseObject("Message");
        message.put("sender", ParseUser.getCurrentUser().getUsername());
        message.put("recipient", activeUser);
        message.put("message", messageEditText.getText().toString());
        message.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    messages.add(ParseUser.getCurrentUser().getUsername()
                            + ": " + messageEditText.getText().toString());
                    messageEditText.setText("");
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ChatActivity.this,
                            "Message not sent!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
