package com.parse.starter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ViewRequestsActivity extends AppCompatActivity {

    ListView requestListView;

    ArrayList<String> requests;
    ArrayList<Double> requestLatitudes;
    ArrayList<Double> requestLongitudes;
    ArrayList<String> usernames;

    ArrayAdapter<String> arrayAdapter;
    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            0, 0, locationListener);

                    Location lastKnownLocation = locationManager
                            .getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    updateListView(lastKnownLocation);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_requests);

        setTitle("Nearby Requests");

        requests = new ArrayList<>(Collections.singletonList("Getting nearby requests..."));
        requestLatitudes = new ArrayList<>();
        requestLongitudes = new ArrayList<>();
        usernames = new ArrayList<>();

        arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, requests);

        requestListView = findViewById(R.id.requestListView);
        requestListView.setAdapter(arrayAdapter);
        requestListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (ContextCompat.checkSelfPermission(ViewRequestsActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ViewRequestsActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                } else {
                    Location lastKnownLocation = locationManager
                            .getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    if (i < requestLatitudes.size() && i < requestLongitudes.size()
                            && i < usernames.size() && lastKnownLocation != null) {
                        Intent intent = new Intent(getApplicationContext(),
                                DriverLocationActivity.class);
                        intent.putExtra("requestLatitude", requestLatitudes.get(i));
                        intent.putExtra("requestLongitude", requestLongitudes.get(i));
                        intent.putExtra("driverLatitude", lastKnownLocation.getLatitude());
                        intent.putExtra("driverLongitude", lastKnownLocation.getLongitude());
                        intent.putExtra("username", usernames.get(i));
                        startActivity(intent);
                    }
                }
            }
        });

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateListView(location);

                ParseUser.getCurrentUser().put("location", new ParseGeoPoint(
                        location.getLatitude(), location.getLongitude()));
                ParseUser.getCurrentUser().saveInBackground();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER,
                    0, 0, locationListener);

            Location lastKnownLocation = locationManager
                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (lastKnownLocation != null) {
                updateListView(lastKnownLocation);
            }
        }
    }

    public void updateListView(Location location) {
        if (location != null) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Request");
            final ParseGeoPoint geoPointLocation = new ParseGeoPoint(
                    location.getLatitude(), location.getLongitude());
            query.whereNear("location", geoPointLocation);
            query.whereDoesNotExist("driverUsername");
            query.setLimit(10);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        requests.clear();
                        requestLatitudes.clear();
                        requestLongitudes.clear();
                        usernames.clear();

                        if (!objects.isEmpty()) {
                            for (ParseObject object : objects) {
                                ParseGeoPoint requestLocation =
                                        (ParseGeoPoint) object.get("location");
                                if (requestLocation != null) {
                                    Double distance = geoPointLocation
                                            .distanceInMilesTo(requestLocation);
                                    distance = (double) Math.round(distance * 10) / 10;

                                    requests.add(distance.toString() + " miles");
                                    requestLatitudes.add(requestLocation.getLatitude());
                                    requestLongitudes.add(requestLocation.getLongitude());
                                    usernames.add(object.getString("username"));
                                }
                            }
                        } else {
                            requests = new ArrayList<>(
                                    Collections.singletonList("No active requests nearby."));
                        }

                        arrayAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
    }
}
