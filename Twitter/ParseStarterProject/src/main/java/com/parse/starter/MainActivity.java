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
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;


public class MainActivity extends AppCompatActivity {

  EditText usernameEditText;
  EditText passwordEditText;
  Button authenticationButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    usernameEditText = findViewById(R.id.usernameEditText);
    passwordEditText = findViewById(R.id.passwordEditText);
    authenticationButton = findViewById(R.id.authenticationButton);

    setTitle("Twitter: Authentication");
    redirectUser();

    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }

  public void redirectUser() {
      if (ParseUser.getCurrentUser() != null) {
          Intent intent = new Intent(getApplicationContext(), UsersActivity.class);
          startActivity(intent);
          finish();
      }
  }

  public void authenticate(View view) {
    ParseUser.logInInBackground(usernameEditText.getText().toString(),
            passwordEditText.getText().toString(), new LogInCallback() {
              @Override
              public void done(ParseUser user, ParseException e) {
                if (e == null) {
                    Log.i("Login", "Success!");
                    redirectUser();
                } else {
                    ParseUser newUser = new ParseUser();
                    newUser.setUsername(usernameEditText.getText().toString());
                    newUser.setPassword(passwordEditText.getText().toString());
                    newUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Log.i("Signup", "Success!");
                                redirectUser();
                            } else {
                                Toast.makeText(MainActivity.this,
                                        e.getMessage().contains("java") ? e.getMessage()
                                                .substring(e.getMessage().indexOf(" "))
                                                : e.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
              }
            });
  }

}