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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import static com.parse.starter.MainActivity.AuthMode.LOGIN;
import static com.parse.starter.MainActivity.AuthMode.SIGNUP;


public class MainActivity extends AppCompatActivity {

    EditText usernameEditText;
    EditText passwordEditText;
    Button authenticationButton;
    TextView toggleAuthModeTextView;

    public enum AuthMode {SIGNUP, LOGIN}

    AuthMode authMode = SIGNUP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("WhatsApp Login");

        redirect();

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        authenticationButton = findViewById(R.id.authenticationButton);
        toggleAuthModeTextView = findViewById(R.id.toggleAuthModeTextView);

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }

    public void redirect() {
        if (ParseUser.getCurrentUser() != null) {
            Intent intent = new Intent(getApplicationContext(), UserListActivity.class);
            startActivity(intent);
        }
    }

    public void toggleAuthMode(View view) {
        Log.i("Status", "Mode toggled!");
        if (authMode == SIGNUP) {
            authMode = LOGIN;
            authenticationButton.setText("Log In");
            toggleAuthModeTextView.setText("Or, Sign Up");
        } else if (authMode == LOGIN) {
            authMode = SIGNUP;
            authenticationButton.setText("Sign Up");
            toggleAuthModeTextView.setText("Or, Login");
        }
    }

    public void authenticate(View view) {
        if (authMode == SIGNUP) {
            ParseUser user = new ParseUser();
            user.setUsername(usernameEditText.getText().toString());
            user.setPassword(passwordEditText.getText().toString());
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Log.i("Status", "User signed up!");
                        redirect();
                    } else {
                        Throwable error = e;
                        while (error.getCause() != null) {
                            error = error.getCause();
                        }
                        Toast.makeText(MainActivity.this,
                                error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else if (authMode == LOGIN) {
            ParseUser.logInInBackground(usernameEditText.getText().toString(),
                    passwordEditText.getText().toString(), new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (e == null) {
                        Log.i("Status", "User logged in!");
                        redirect();
                    } else {
                        Throwable error = e;
                        while (error.getCause() != null) {
                            error = error.getCause();
                        }
                        Toast.makeText(MainActivity.this,
                                error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}