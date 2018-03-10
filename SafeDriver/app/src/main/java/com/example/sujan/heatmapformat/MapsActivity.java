package com.example.sujan.heatmapformat;

import android.content.res.Resources;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.Random;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    LinearLayout linearLayout;
    EditText startEditText;
    EditText endEditText;
    Button navigateButton;

    private GoogleMap mMap;

    private final static int ALPHA_ADJUSTMENT = 0x77000000;

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
    public void onWindowFocusChanged(boolean hasFocus) {
        linearLayout = findViewById(R.id.linearLayout);
        startEditText = findViewById(R.id.startEditText);
        endEditText = findViewById(R.id.endEditText);
        navigateButton = findViewById(R.id.navigateButton);
        Log.i("Button width", Integer.toString(navigateButton.getMeasuredWidth()));
        startEditText.setWidth((Resources.getSystem().getDisplayMetrics().widthPixels
                - navigateButton.getWidth()) / 2);
        endEditText.setWidth(startEditText.getWidth());
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in New York and move the camera
        LatLng newyork = new LatLng(40.7484, -73.9857);
        mMap.addMarker(new MarkerOptions().position(newyork).title("Marker in New York")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newyork, 15));

        ArrayList<LatLng> shape = new ArrayList<>();
        shape.add(new LatLng(newyork.latitude - 0.005,newyork.longitude - 0.004));
        shape.add(new LatLng(newyork.latitude + 0.005,newyork.longitude + 0.006));
        shape.add(new LatLng(newyork.latitude - 0.005,newyork.longitude + 0.006));
        drawPolygon(shape);

        ArrayList<LatLng> shape2 = new ArrayList<>();
        shape2.add(new LatLng(newyork.latitude - 0.005,newyork.longitude - 0.006));
        shape2.add(new LatLng(newyork.latitude + 0.005,newyork.longitude - 0.006));
        shape2.add(new LatLng(newyork.latitude + 0.005,newyork.longitude + 0.004));
        drawPolygon(shape2);
    }

    public void drawPolygon(ArrayList<LatLng> shape) {
        Random random = new Random();
        int colourShift = random.nextInt(0x88) * 0x100;

        shape.add(shape.get(0));
        mMap.addPolygon(new PolygonOptions()
                .addAll(shape)
                .fillColor(Color.RED - ALPHA_ADJUSTMENT + colourShift)
                .strokeColor(Color.RED + colourShift)
                .strokeWidth(5));
    }

    public void navigate(View view) {
        String startPoint = startEditText.getText().toString();
        String endPoint = endEditText.getText().toString();
        double startLat, startLng, endLat, endLng;
        try {
            startLat = Double.parseDouble(startPoint.split(",")[0]);
            startLng = Double.parseDouble(startPoint.split(",")[1]);
            endLat = Double.parseDouble(endPoint.split(",")[0]);
            endLng = Double.parseDouble(endPoint.split(",")[1]);

            boolean inRange = true;
            for (double pt : new double[] {startLat, endLat}) {
                if (pt < -90 || pt > 90) {
                    inRange = false;
                }
            }
            for (double pt : new double[] {startLng, endLng}) {
                if (pt < -180 || pt > 180) {
                    inRange = false;
                }
            }

            if (inRange) {
                Toast.makeText(this, "Navigate!", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Location coordinates out of range",
                        Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            if (startPoint.equals("") || endPoint.equals("")) {
                Toast.makeText(this, "One or both text fields empty",
                        Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Enter start/end points with format: lat,lng",
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}
