package com.example.sujan.memorableplaces;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    ArrayList<Marker> markers = new ArrayList<Marker>();

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                    Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    LatLng sydney = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 18));
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapLongClickListener(this);

        Intent intent = getIntent();
        final int listViewIndex = intent.getIntExtra("ListView index" ,0);
        if (listViewIndex == 0) {
            locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(userLocation).title("Your Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    for (int i = 0; i < MainActivity.locations.size(); i++) {
                        if (i != listViewIndex - 1) {
                            mMap.addMarker(new MarkerOptions().position(MainActivity.locations.get(i)).title(MainActivity.locationNames.get(i + 1)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
                        }
                    }
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 18));
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

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                LatLng userLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(userLocation).title("Your Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                for (int i = 0; i < MainActivity.locations.size(); i++) {
                    if (i != listViewIndex - 1) {
                        mMap.addMarker(new MarkerOptions().position(MainActivity.locations.get(i)).title(MainActivity.locationNames.get(i + 1)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
                    }
                }
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 18));
            }
        }
        else {
            LatLng savedLocation = MainActivity.locations.get(listViewIndex - 1);
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(savedLocation).title(MainActivity.locationNames.get(listViewIndex)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            for (int i = 0; i < MainActivity.locations.size(); i++) {
                if (i != listViewIndex - 1) {
                    mMap.addMarker(new MarkerOptions().position(MainActivity.locations.get(i)).title(MainActivity.locationNames.get(i + 1)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
                }
            }
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(savedLocation, 18));
        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        String placeName = Calendar.getInstance().getTime().toString();
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

            if (addressList != null && addressList.size() > 0 && addressList.get(0).getAddressLine(0) != null) {
                placeName = addressList.get(0).getAddressLine(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.i("Place name", placeName);
        mMap.addMarker(new MarkerOptions().position(latLng).title(placeName));

        MainActivity.addLocation(latLng, placeName);

        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.sujan.memorableplaces", Context.MODE_PRIVATE);
        try {
            ArrayList<String> latitudes = new ArrayList<String>();
            ArrayList<String> longitudes = new ArrayList<String>();
            for (LatLng location : MainActivity.locations) {
                latitudes.add(Double.toString(location.latitude));
                longitudes.add(Double.toString(location.longitude));
            }
            sharedPreferences.edit().putString("latitudes", ObjectSerializer.serialize(latitudes)).apply();
            sharedPreferences.edit().putString( "longitudes", ObjectSerializer.serialize(longitudes)).apply();
            sharedPreferences.edit().putString("locationNames", ObjectSerializer.serialize(MainActivity.locationNames)).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Toast.makeText(MapsActivity.this, "Location saved!", Toast.LENGTH_SHORT).show();
    }
}
