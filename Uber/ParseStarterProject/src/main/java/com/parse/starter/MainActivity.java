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
import android.widget.Switch;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;


public class MainActivity extends AppCompatActivity {

    Switch userTypeSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        userTypeSwitch = findViewById(R.id.userTypeSwitch);

        if (ParseUser.getCurrentUser() == null) {
            ParseAnonymousUtils.logIn(new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (e == null) {
                        Log.i("Status", "Anonymous login successful!");
                    } else {
                        Log.i("Status", "Anonymous login failed!");
                    }
                }
            });
        } else {
            if (ParseUser.getCurrentUser().get("riderOrDriver") != null) {
                Log.i("Status", "Redirecting as "
                        + ParseUser.getCurrentUser().get("riderOrDriver"));

                redirectActivity();
            }
        }

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }

    public void getStarted(View view) {
        Log.i("User switch value", String.valueOf(userTypeSwitch.isChecked()));

        String userType = userTypeSwitch.isChecked() ? "driver" : "rider";

        ParseUser.getCurrentUser().put("riderOrDriver", userType);
        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    redirectActivity();
                } else {
                    Toast.makeText(MainActivity.this, "Something went wrong!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        Log.i("Status", "Redirecting as " + userType);
    }

    public void redirectActivity() {
        if (ParseUser.getCurrentUser().get("riderOrDriver").equals("rider")) {
            Intent intent = new Intent(getApplicationContext(), RiderActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getApplicationContext(), ViewRequestsActivity.class);
            startActivity(intent);
        }
    }

}