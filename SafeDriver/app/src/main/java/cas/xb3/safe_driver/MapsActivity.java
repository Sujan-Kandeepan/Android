package cas.xb3.safe_driver;

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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    // Define UI elements
    LinearLayout linearLayout;
    EditText startEditText;
    EditText endEditText;
    Button navigateButton;

    // Map instance variable
    private GoogleMap mMap;

    // Colour adjustment constant
    private final static int ALPHA_ADJUSTMENT = 0x77000000;

    // Define behaviour on app startup
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    // Define and adjust UI elements upon display
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

    // Define behaviour once map is fetched and ready
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Fetch and initialize map
        mMap = googleMap;

        // Add a marker in New York and move the camera
        LatLng newyork = new LatLng(40.7484, -73.9857);
        mMap.addMarker(new MarkerOptions().position(newyork).title("Marker in New York")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newyork, 15));

        // Plot nine sample shapes
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                LatLng point = new LatLng(newyork.latitude + i * 0.002,
                        newyork.longitude + j * 0.002);
                ArrayList<LatLng> shape = new ArrayList<>();
                shape.add(new LatLng(point.latitude - 0.001, point.longitude - 0.001));
                shape.add(new LatLng(point.latitude + 0.001, point.longitude - 0.001));
                shape.add(new LatLng(point.latitude + 0.001, point.longitude + 0.001));
                shape.add(new LatLng(point.latitude - 0.001, point.longitude + 0.001));
                drawPolygon(shape, (i+1)*3 + (j+1));
            }
        }
    }

    // Draw collision cluster polygon, given perimeter points and intensity
    public void drawPolygon(ArrayList<LatLng> shape, int intensity) {
        // Set colour by intensity, more red given higher value
        int colourShift = intensity * 0x1100; // Varying intensity between 0-8 collision points
        if (colourShift > 0x8800) {
            colourShift = 0x8800;
        }
        colourShift = 0x8800 - colourShift;

        // Log colour values
        Log.i("Intensity", Integer.toString(intensity));
        Log.i("Colour shift", Integer.toString(colourShift));

        // Draw polygon on map
        shape.add(shape.get(0));
        mMap.addPolygon(new PolygonOptions()
                .addAll(shape)
                .fillColor(Color.RED - ALPHA_ADJUSTMENT + colourShift)
                .strokeColor(Color.RED + colourShift)
                .strokeWidth(5));
    }

    // Button pressed, start navigation
    public void navigate(View view) {
        // Extract coordinate information from text fields
        String startPoint = startEditText.getText().toString();
        String endPoint = endEditText.getText().toString();
        double startLat, startLng, endLat, endLng;

        // Attempt to start navigation with given data
        try {
            // Obtain coordinate information from inputted text
            startLat = Double.parseDouble(startPoint.split(",")[0]);
            startLng = Double.parseDouble(startPoint.split(",")[1]);
            endLat = Double.parseDouble(endPoint.split(",")[0]);
            endLng = Double.parseDouble(endPoint.split(",")[1]);

            // Checking if latitude in range
            boolean inRange = true;
            for (double pt : new double[] {startLat, endLat}) {
                if (pt < -90 || pt > 90) {
                    inRange = false;
                }
            }

            // Checking if longitude in range
            for (double pt : new double[] {startLng, endLng}) {
                if (pt < -180 || pt > 180) {
                    inRange = false;
                }
            }

            // Navigate if coordinates are within map bounds
            if (inRange) {
                Toast.makeText(this, "Navigate!", Toast.LENGTH_SHORT).show();
            }

            // Show error message if coordinates out of bounds
            else {
                Toast.makeText(this, "Location coordinates out of range",
                        Toast.LENGTH_SHORT).show();
            }
        }

        // Invalid text, issues parsing if empty or formatted incorrectly
        catch (Exception e) {
            // One or both text fields empty, display error message
            if (startPoint.isEmpty() || endPoint.equals("")) {
                Toast.makeText(this, "One or both text fields empty",
                        Toast.LENGTH_SHORT).show();
            }

            // Invalid location input format, display error message
            else {
                Toast.makeText(this, "Enter start/end points with format: lat,lng",
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}
