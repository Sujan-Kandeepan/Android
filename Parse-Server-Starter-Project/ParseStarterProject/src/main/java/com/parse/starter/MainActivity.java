/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;

import static com.parse.starter.MainActivity.Status.LOGIN;
import static com.parse.starter.MainActivity.Status.SIGNUP;


public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnKeyListener {

    public enum Status {SIGNUP, LOGIN}

    Status status;

    RelativeLayout backgroundLayout;
    ImageView logoImageView;
    EditText usernameEditText;
    EditText passwordEditText;
    Button primaryButton;
    Button otherButton;

    public void showUserList() {
        Intent intent = new Intent(getApplicationContext(), UserListActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Instagram");

        /*
        ParseObject score = new ParseObject("Score");
        score.put("username", "jaison");
        score.put("score", 65);
        score.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    // OK
                    Log.i("Success", "We saved the score");
                } else {
                    e.printStackTrace();
                }
            }
        });

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Score");
        query.getInBackground("rfAihB7hMD", new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null && object != null) {
                    object.put("score", 85);
                    object.saveInBackground();

                    Log.i("username", object.getString("username"));
                    Log.i("score", Integer.toString(object.getInt("score")));
                }
            }
        });
        */

        /*
        ParseObject tweet = new ParseObject("Tweet");
        tweet.put("username", "sujan");
        tweet.put("tweet", "Hello world!");
        tweet.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.i("Success","We saved the tweet");
                } else {
                    e.printStackTrace();
                }
            }
        });

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Tweet");
        query.getInBackground("Greo8zQwr6", new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null && object != null) {
                    object.put("tweet", "Parse is cool!");
                    object.saveInBackground();

                    Log.i("username", object.getString("username"));
                    Log.i("tweet", object.getString("tweet"));
                }
            }
        });
        */

        /*
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Score");

        query.whereEqualTo("username", "jaison");
        query.setLimit(1);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && !objects.isEmpty()) {
                    for (ParseObject object : objects) {
                        Log.i("username", object.getString("username"));
                        Log.i("score", Integer.toString(object.getInt("score")));
                    }
                }
            }
        });
        */

        /*
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Score");
        query.whereGreaterThan("score", 50);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && !objects.isEmpty()) {
                    for (ParseObject object : objects) {
                        object.put("score", object.getInt("score") + 20);
                        object.saveInBackground();

                        Log.i("username", object.getString("username"));
                        Log.i("score", Integer.toString(object.getInt("score")));
                    }
                }
            }
        });
        */

        /*
        ParseUser user = new ParseUser();
        user.setUsername("sujan");
        user.setPassword("password");
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.i("Sign up", "Success!");
                } else {
                    e.printStackTrace();
                }
            }
        });
        */

        /*
        ParseUser.logInInBackground("sujan", "password", new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null && user != null) {
                    Log.i("Success", "We logged in!");
                } else {
                    e.printStackTrace();
                }
            }
        });
        */

        /*
        ParseUser.logOut();
        if (ParseUser.getCurrentUser() != null) {
            Log.i("Logged in", ParseUser.getCurrentUser().getUsername());
        } else {
          Log.i("No luck", "Not signed in.");
        }
        */

        backgroundLayout = (RelativeLayout) findViewById(R.id.backgroundLayout);
        logoImageView = (ImageView) findViewById(R.id.logoImageView);
        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        primaryButton = (Button) findViewById(R.id.primaryButton);
        otherButton = (Button) findViewById(R.id.otherButton);

        backgroundLayout.setOnClickListener(this);
        logoImageView.setOnClickListener(this);
        passwordEditText.setOnKeyListener(this);

        status = SIGNUP;

        if (ParseUser.getCurrentUser() != null) {
            showUserList();
        }

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.backgroundLayout || view.getId() == R.id.logoImageView) {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
            trigger(view);
        }

        return false;
    }

    public void trigger(View view) {
        if (usernameEditText.getText().toString().isEmpty()
                || passwordEditText.getText().toString().isEmpty()) {
            Toast.makeText(this, "A username and a password are required.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        switch (status) {
            case SIGNUP:
                signUp();
                break;
            case LOGIN:
                login();
                break;
        }
    }

    public void change(View view) {
        switch (status) {
            case SIGNUP:
                status = LOGIN;
                primaryButton.setText("Login");
                otherButton.setText("or, Sign Up");
                break;
            case LOGIN:
                status = SIGNUP;
                primaryButton.setText("Sign Up");
                otherButton.setText("or, Login");
                break;
        }
    }

    public void signUp() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
        query.whereEqualTo("username", usernameEditText.getText().toString());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    ParseUser user = new ParseUser();
                    user.setUsername(usernameEditText.getText().toString());
                    user.setPassword(passwordEditText.getText().toString());
                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Log.i("Signup", "Successful!");
                                showUserList();
                            } else {
                                Toast.makeText(MainActivity.this,
                                        e.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    Toast.makeText(MainActivity.this, e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }

    public void login() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
        query.whereEqualTo("username", usernameEditText.getText().toString());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    ParseUser.logInInBackground(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString(), new LogInCallback() {
                                @Override
                                public void done(ParseUser user, ParseException e) {
                                    if (e == null && user != null) {
                                        Log.i("Login", "Successful!");
                                        showUserList();
                                    } else {
                                        Toast.makeText(MainActivity.this, e.getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                        e.printStackTrace();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(MainActivity.this, e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }

}