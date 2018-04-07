package cas.xb3.safe_driver;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
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
import com.google.android.gms.maps.model.ButtCap;
import com.google.android.gms.maps.model.CustomCap;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class which defines the behaviour of the main map interface of the application,
 * handling requests for both route line and collision clusters, and controlling
 * what is displayed to the user as they interact with the service.
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    // Define UI elements
    LinearLayout linearLayout;
    EditText startEditText;
    EditText endEditText;
    Button navigateButton;

    // Map instance variable
    private GoogleMap mMap;

    // Boolean to prevent repetitive resizing
    boolean appLaunched = false;

    // Server subdomain name for non-static ngrok url
    String subdomain;

    // Bounding box coordinate variables
    double startLat, startLng, endLat, endLng;

    // Array list of polygons, route line
    ArrayList<Polygon> polygons = new ArrayList<>();
    Polyline routeLine;

    // Map bounds JSON object
    JSONObject mapBounds;

    // Builder for camera boundaries
    LatLngBounds.Builder builder = new LatLngBounds.Builder();

    /**
     * Define behaviour on app startup.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Initialize app, set view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Instantiate view variables
        linearLayout = findViewById(R.id.linearLayout);
        startEditText = findViewById(R.id.startEditText);
        endEditText = findViewById(R.id.endEditText);
        navigateButton = findViewById(R.id.navigateButton);

        info(findViewById(R.id.infoButton));
    }

    /**
     * Define and adjust UI elements upon display.
     * @param hasFocus State of whether app is running in foreground.
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // Resize UI elements in header
        if (appLaunched) {
            Log.i("Button width", Integer.toString(navigateButton.getMeasuredWidth()));
            startEditText.setWidth((Resources.getSystem().getDisplayMetrics().widthPixels
                    - navigateButton.getWidth()) / 2);
            endEditText.setWidth(startEditText.getWidth());

            appLaunched = true;
        }

        // Change focus to end location after start location entered
        startEditText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
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

        // Trigger navigation button function after end location entered
        endEditText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
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

    /**
     * Define behaviour once map is fetched and ready.
     * @param googleMap Map on which to overlay data.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Fetch and initialize map
        mMap = googleMap;

        // Move the camera to New York
        LatLng newyork = new LatLng(40.7484, -73.9857);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newyork, 15));

        // Enter mock start and end locations
        mockText();

        // Specify subdomain on startup
        setSubdomain();
    }

    /**
     * Display dialog box for entering ngrok URL subdomain.
     */
    public void setSubdomain() {
        // Initialize dialog box
        AlertDialog.Builder builder = new AlertDialog.Builder(this,
                AlertDialog.THEME_DEVICE_DEFAULT_DARK);
        builder.setTitle("Enter the server subdomain name: \nCancel to use previous.");

        // Set up the input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        input.setTextColor(Color.WHITE);
        builder.setView(input);

        // Get shared preferences where subdomain is stored
        final SharedPreferences sharedPreferences = this.getSharedPreferences(
                "cas.xb3.safe_driver", Context.MODE_PRIVATE);

        // Set up confirmation button
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                subdomain = input.getText().toString();
                sharedPreferences.edit().putString("subdomain", subdomain).apply();
                if (input.getText().toString().isEmpty()) {
                    Toast.makeText(MapsActivity.this, "Subdomain name cannot be empty!",
                            Toast.LENGTH_SHORT).show();
                    setSubdomain();
                }
            }
        });

        // Set up cancel button
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (sharedPreferences.contains("subdomain")) {
                    subdomain = sharedPreferences.getString("subdomain", "");
                }
                else {
                    subdomain = "";
                }

                if (subdomain.equals("")) {
                    Toast.makeText(MapsActivity.this, "No server subdomain name found!",
                            Toast.LENGTH_SHORT).show();
                    setSubdomain();
                }

                dialog.cancel();
            }
        });

        // Tweak margins and better fitting
        FrameLayout container = new FrameLayout(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = 75;
        params.rightMargin = 75;
        input.setLayoutParams(params);
        container.addView(input);
        builder.setView(container);

        // Show dialog
        AlertDialog dialog = builder.create();
        dialog.show();

        // Change button colours
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        positiveButton.setTextColor(getResources().getColor(R.color.colorAccent));
        negativeButton.setTextColor(getResources().getColor(R.color.colorAccent));
    }

    /**
     * Start intent for displaying user guide.
     * @param view UI element which starts the intent.
     */
    public void info(View view) {
        Intent intent = new Intent(getApplicationContext(), InfoActivity.class);
        startActivity(intent);
    }

    /**
     * Random start and end locations quickly entered.
     */
    public void mockText() {
        startEditText.setText("40.61,-74.18");
        endEditText.setText("40.63,-74.12");
        //startEditText.setText("40.36,-73.57");
        //endEditText.setText("40.32,-75.96");
    }

    /**
     * Random display of shapes, shows colours and appearance.
     * @param newyork Location of New York as specified earlier.
     */
    public void mockShapes(LatLng newyork) {
        // Plot nine sample shapes
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                // Find location of Empire State Building
                LatLng point = new LatLng(newyork.latitude + i * 0.002,
                        newyork.longitude + j * 0.002);

                // Generate array list of points
                ArrayList<LatLng> shape = new ArrayList<>();
                shape.add(new LatLng(point.latitude - 0.001, point.longitude - 0.001));
                shape.add(new LatLng(point.latitude + 0.001, point.longitude - 0.001));
                shape.add(new LatLng(point.latitude + 0.001, point.longitude + 0.001));
                shape.add(new LatLng(point.latitude - 0.001, point.longitude + 0.001));

                // Draw shape on map
                drawPolygon(shape, (i + 1) * 6 + (j + 1) * 2);
            }
        }
    }

    /**
     * Generate cluster using mock response.
     */
    public void mockCluster() {
        try {
            // Initialize JSON object from response received in terminal
            JSONObject mockresponse = new JSONObject("{\"clusters\":[{\"id\":1," +
                    "\"polygon\":[{\"latitude\":40.7867127,\"longitude\":-73.9765953}," +
                    "{\"latitude\":40.785994625,\"longitude\":-73.9512014}," +
                    "{\"latitude\":40.7838404,\"longitude\":-73.9584568}," +
                    "{\"latitude\":40.7752235,\"longitude\":-73.9548291}]," +
                    "\"num_data_points\":10}]}");

            // Generate cluster based on mock response
            drawClusters(mockresponse);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Draw collision cluster polygon, given perimeter points and intensity.
     * @param shape Perimeter points of shape to be drawn.
     * @param intensity Number of collision points within shape.
     */
    public void drawPolygon(ArrayList<LatLng> shape, int intensity) {
        // Set colour by intensity, more red given higher value
        final int alphaAdjustment = 0x77000000;
        intensity = (int) intensity / 2;
        int colourShift = intensity * 0x1100; // Varying intensity between 0-8 collision points
        if (colourShift > 0x8800) {
            colourShift = 0x8800;
        }
        colourShift = 0x8800 - colourShift;

        // Log colour values
        Log.i("Intensity", Integer.toString(intensity));
        Log.i("Colour shift", Integer.toString(colourShift));

        // Draw polygon on map
        if (shape.size() > 0) {
            shape.add(shape.get(0));
            Polygon p = mMap.addPolygon(new PolygonOptions()
                    .addAll(shape)
                    .fillColor(Color.RED - alphaAdjustment + colourShift)
                    .strokeColor(Color.RED + colourShift)
                    .strokeWidth(5));
            polygons.add(p);
        }
    }

    /**
     * Generate URL from which to obtain route line.
     * @param origin Start point of route.
     * @param dest End point of route.
     * @return Generated Google Maps URL used to make request.
     */
    private String getDirectionsUrl(LatLng origin, LatLng dest) {


        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }

    /**
     * Button pressed, start navigation.
     * @param view UI element with which triggers the function.
     */
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
            for (double pt : new double[]{startLat, endLat}) {
                if (pt < -90 || pt > 90) {
                    inRange = false;
                }
            }

            // Checking if longitude in range
            for (double pt : new double[]{startLng, endLng}) {
                if (pt < -180 || pt > 180) {
                    inRange = false;
                }
            }

            // Navigate if coordinates are within map bounds
            if (inRange) {
                generateJSON();
                getClusters();

                //mockCluster();

                String directionsURL = getDirectionsUrl(new LatLng(startLat, startLng),
                        new LatLng(endLat, endLng));
                Log.i("Directions URL", directionsURL);

                // Close soft keyboard during transition
                View thisview = this.getCurrentFocus();
                if (thisview != null) {
                    InputMethodManager imm =
                            (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(thisview.getWindowToken(), 0);
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

    /**
     * Create JSON object with map bounds surrounding given route.
     */
    public void generateJSON() {
        // List of four corner labels
        String[] cornerlabels = new String[]{"TL", "TR", "BL", "BR"};

        // Compute left, right, bottom and top of bounding box
        double boxwidth = Math.abs(startLat - endLat), boxheight = Math.abs(startLng - endLng);
        double boxleft = Math.min(startLat, endLat) - boxwidth * 0.25;
        double boxright = Math.max(startLat, endLat) + boxwidth * 0.25;
        double boxbottom = Math.min(startLng, endLng) - boxheight * 0.25;
        double boxtop = Math.max(startLng, endLng) + boxheight * 0.25;

        // Generate JSON object to store data
        mapBounds = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < 4; i++) {
            // Pass data for a single polygon vertex
            JSONObject cornerObject = new JSONObject();
            try {
                // Specify corner label and coordinates
                cornerObject.put("vertex", cornerlabels[i]);
                cornerObject.put("latitude", (int) (i / 2) == 0 ? boxright : boxleft);
                cornerObject.put("longitude", i % 2 == 0 ? boxbottom : boxtop);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Add vertex info to JSON object
            Log.i("Corner JSON", cornerObject.toString());
            jsonArray.put(cornerObject);
        }

        // Finish constructing entire JSON object
        try {
            mapBounds.put("map_boundaries", jsonArray);
            Log.i("Map bounds", mapBounds.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Draw route as the crow flies.
     */
    public void drawDottedRoute() {
        // Determine location and appearance of route line
        LatLng startPoint = new LatLng(startLat, startLng);
        LatLng endPoint = new LatLng(endLat, endLng);
        Matrix m = new Matrix();
        m.postRotate(90);
        Bitmap car = Bitmap.createScaledBitmap(BitmapFactory
                        .decodeResource(getResources(), R.drawable.car),
                150, 150, false);
        car = Bitmap.createBitmap(car, 0, 0,
                car.getWidth(), car.getHeight(), m, true);
        Bitmap finish = Bitmap.createScaledBitmap(BitmapFactory.
                        decodeResource(getResources(), R.drawable.finish),
                150, 150, false);

        // Draw dotted route line on map from start to end
        if (routeLine != null) routeLine.remove();
        routeLine = mMap.addPolyline(new PolylineOptions()
                .add(startPoint, endPoint)
                .width(15)
                .color(Color.DKGRAY));
        routeLine.setPattern(Arrays.<PatternItem>asList(new Gap(20), new Dash(20)));
        routeLine.setStartCap(new CustomCap(BitmapDescriptorFactory.fromBitmap(car)));
        routeLine.setEndCap(new CustomCap(BitmapDescriptorFactory.fromBitmap(finish)));
    }

    /**
     * Draw collision clusters on map surrounding route as calculated.
     * @param response JSON response specifying cluster information.
     */
    public void drawClusters(JSONObject response) {
        // Clear old polygons
        for (int j = 0; j < polygons.size(); j++) {
            polygons.get(j).remove();
        }
        polygons.clear();

        try {
            // Receive array of clusters
            JSONArray clusters = response.getJSONArray("clusters");
            builder = new LatLngBounds.Builder();
            for (int i = 0; i < clusters.length(); i++) {
                // Get cluster, initialize polygon, consider map bounds
                JSONObject cluster = clusters.getJSONObject(i);
                ArrayList<LatLng> polygon = new ArrayList<>();
                JSONArray points = cluster.getJSONArray("polygon");

                // Add individual points to shape
                for (int j = 0; j < points.length(); j++) {
                    // Receive points in shape
                    JSONObject point = points.getJSONObject(j);
                    double latitude = point.getDouble("latitude");
                    double longitude = point.getDouble("longitude");

                    // Add points to shape and scope of map bounds
                    LatLng latLng = new LatLng(latitude, longitude);
                    polygon.add(latLng);
                    builder.include(latLng);
                    Log.i("Polygon point", latLng.toString());
                }

                // Get number of collisions within cluster
                int numDataPoints = cluster.getInt("num_data_points");

                // Draw collision cluster polygon on map
                drawPolygon(polygon, numDataPoints);
            }

            // Include start and end points within map bounds
            builder.include(new LatLng(startLat, startLng));
            builder.include(new LatLng(endLat, endLng));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Make POST request to server for clusters.
     */
    public void getClusters() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String ip = "10.0.2.2", port = "8000";
        //String url = "http://" + ip + ":" + port + "/api/v1/route";
        //String url = "https://emilyhorsman.com/safe-driver/api/v1/route.json";
        String url = "http://" + subdomain + ".ngrok.io/api/v1/route";

        if (Math.abs(startLat - endLat) > 10 || Math.abs(startLng - endLng) > 10) {
            Toast.makeText(this, "Route distance too far!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Send POST request to server, receive a response
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, mapBounds,

                // Listening for response with workable polygon information
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Draw clusters and start retrieving route
                        Log.d("Cluster JSON", response.toString());
                        drawClusters(response);
                        getRoute();
                    }
                },

                // Listening for response if errors occur
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle Error
                        Log.i("Error response", error.toString());

                        NetworkResponse response = error.networkResponse;
                        if(response != null && response.data != null){
                            switch(response.statusCode){
                                case 404:
                                    Toast.makeText(MapsActivity.this,
                                            "Enter a new subdomain.",
                                            Toast.LENGTH_SHORT).show();
                                    setSubdomain();
                                    break;
                            }
                        }

                        polygons.clear();
                    }
                }) {

            // Add headers to POST request as needed
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

            // Specify information about data being sent
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        // Add POST request to request queue
        queue.add(postRequest);
    }

    /**
     * Make POST request to server for route line.
     */
    public void getRoute() {

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getDirectionsUrl(new LatLng(startLat, startLng), new LatLng(endLat, endLng));

        // Send POST request to server, receive a response
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.GET, url, mapBounds,

                // Listening for response with workable polygon information
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Process response and generate route lines
                        Log.d("Route JSON", response.toString());
                        ParserTask parserTask = new ParserTask();
                        parserTask.execute(response.toString());
                    }
                },

                // Listening for response if errors occur
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle Error
                        Log.i("Error response", error.toString());
                    }
                }) {

            // Add headers to POST request as needed
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
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

    /**
     * A class to parse the Google Places in JSON format.
     */
    private class ParserTask extends AsyncTask<String, Integer,
            List<List<HashMap<String, String>>>> {

        /**
         * Parse data and organize before drawing to map.
         * @param jsonData Received JSON data.
         * @return Data to be converted and displayed as route lines on map.
         */
        //
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        /**
         * Generate route lines once parsed and display on map.
         * @param result
         */
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            // Initialize points and options
            points = new ArrayList<LatLng>();
            lineOptions = new PolylineOptions();

            // Fetching first route if found
            if (!result.isEmpty()) {
                List<HashMap<String, String>> path = result.get(0);

                // Fetching all the points in first route
                for (int i = 0; i < path.size(); i++) {
                    HashMap<String, String> point = path.get(i);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                    builder.include(position);
                }
            }

            // Adding all the points in the route to LineOptions
            lineOptions.addAll(points);
            lineOptions.width(15);
            lineOptions.color(Color.DKGRAY);

            // Drawing polyline in the Google Map for the i-th route
            if (routeLine != null) routeLine.remove();
            if (!result.isEmpty()) {
                routeLine = mMap.addPolyline(lineOptions);

                // Putting arrowhead at end of line
                Bitmap arrow = Bitmap.createScaledBitmap(BitmapFactory.
                                decodeResource(getResources(), R.drawable.arrow),
                        40, 40, false);

                // Draw dotted route line on map from start to end
                routeLine.setPattern(Arrays.<PatternItem>asList(
                        new Gap(25), new Dash(50)));
                routeLine.setStartCap(new RoundCap());
                routeLine.setEndCap(new CustomCap(BitmapDescriptorFactory.fromBitmap(arrow)));
            }

            // Display message if no route was found
            if (result.isEmpty()) {
                Toast.makeText(MapsActivity.this, "Route could not be generated!",
                        Toast.LENGTH_SHORT).show();
            }

            // Display message if no clusters were found
            else if (polygons.isEmpty()) {
                Toast.makeText(MapsActivity.this, "No collision data found!",
                        Toast.LENGTH_SHORT).show();
            }

            // Determine map bounds around map, adjust and move camera
            LatLngBounds bounds = builder.build();
            int padding = 250; // offset from edges of the map in pixels
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            if (!result.isEmpty() && !polygons.isEmpty()) {
                mMap.animateCamera(cu);
            }
        }
    }
}
