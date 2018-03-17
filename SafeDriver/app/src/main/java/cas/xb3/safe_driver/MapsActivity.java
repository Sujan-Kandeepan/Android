package cas.xb3.safe_driver;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    // Define UI elements
    LinearLayout linearLayout;
    EditText startEditText;
    EditText endEditText;
    Button navigateButton;

    // Boolean to prevent repetitive resizing
    boolean appLaunched = false;

    // Map instance variable
    private GoogleMap mMap;

    // Colour adjustment constant
    private final static int ALPHA_ADJUSTMENT = 0x77000000;

    // Bounding box coordinate variables
    double startLat, startLng, endLat, endLng;

    //Map bounds JSON object
    JSONObject mapBounds;

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

        if (appLaunched) {
            Log.i("Button width", Integer.toString(navigateButton.getMeasuredWidth()));
            startEditText.setWidth((Resources.getSystem().getDisplayMetrics().widthPixels
                    - navigateButton.getWidth()) / 2);
            endEditText.setWidth(startEditText.getWidth());

            appLaunched = true;
        }

        startEditText.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            startEditText.clearFocus();
                            endEditText.requestFocus();
                            endEditText.setCursorVisible(true);
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        endEditText.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            navigate(navigateButton);
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
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
                generateJSON();
                //apiCall();

                try {
                    JSONObject mockresponse = new JSONObject("{\"clusters\":[{\"id\":1," +
                            "\"polygon\":[{\"latitude\":40.7867127,\"longitude\":-73.9765953}," +
                            "{\"latitude\":40.785994625,\"longitude\":-73.9512014}," +
                            "{\"latitude\":40.7838404,\"longitude\":-73.9584568}," +
                            "{\"latitude\":40.7752235,\"longitude\":-73.9548291}]," +
                            "\"num_data_points\":10}]}");
                    processResponse(mockresponse);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Check if no view has focus:
                View thisview = this.getCurrentFocus();
                if (thisview != null) {
                    InputMethodManager imm =
                            (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
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

    public void generateJSON() {
        String[] cornerlabels = new String[] {"TL", "TR", "BL", "BR"};
        double boxwidth = Math.abs(startLat - endLat), boxheight = Math.abs(startLng - endLng);
        double boxleft = Math.min(startLat, endLat) - boxwidth * 0.1;
        double boxright = Math.max(startLat, endLat) + boxwidth * 0.1;
        double boxbottom = Math.min(startLng, endLng) - boxheight * 0.1;
        double boxtop = Math.max(startLng, endLng) + boxheight * 0.1;

        mapBounds = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < 4; i++) {
            JSONObject cornerObject = new JSONObject();
            try {
                cornerObject.put("vertex", cornerlabels[i]);
                cornerObject.put("latitude", i % 2 == 0 ? boxleft : boxright);
                cornerObject.put("longitude", (int)(i / 2) == 0 ? boxtop : boxbottom);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("Corner JSON", cornerObject.toString());
            jsonArray.put(cornerObject);
        }
        try {
            mapBounds.put("map_boundaries", jsonArray);
            Log.i("JSON string", mapBounds.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void processResponse(JSONObject response) {
        try {
            JSONArray clusters = response.getJSONArray("clusters");
            for (int i = 0; i < clusters.length(); i++) {
                JSONObject cluster = clusters.getJSONObject(i);
                ArrayList<LatLng> polygon = new ArrayList<>();
                JSONArray points = cluster.getJSONArray("polygon");
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (int j = 0; j < points.length(); j++) {
                    JSONObject point = points.getJSONObject(j);
                    double latitude = point.getDouble("latitude");
                    double longitude = point.getDouble("longitude");

                    LatLng latLng = new LatLng(latitude, longitude);
                    polygon.add(latLng);
                    builder.include(latLng);
                    Log.i("Polygon point", latLng.toString());

                }
                int numdatapoints = cluster.getInt("num_data_points");
                drawPolygon(polygon, numdatapoints);

                LatLngBounds bounds = builder.build();
                int padding = 100; // offset from edges of the map in pixels
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                mMap.animateCamera(cu);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Make POST request to server
    public void apiCall() {

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String ip = "192.168.0.23", port = "8000";
        String url = "http://" + ip + ":" + port + "/api/v1/route";

        // Send POST request to server, receive a response
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, mapBounds,

                // Listening for response with workable polygon information
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Interpreting base64 as bitmap image
                        Log.d("Returned JSON", response.toString());
                        processResponse(response);
                    }
                },

                // Listening for response if errors occur
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle Error
                        Log.i("Error response",error.toString());
                    }
                }) {

            // Add headers to POST request as needed
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String,String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

            // Specify information about data being sent
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        // Set timeout to 30 seconds
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Add POST request to request queue
        queue.add(postRequest);
    }
}
