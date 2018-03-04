package com.example.sujan.arsafety;

import android.Manifest;
import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    // UI elements
    ImageView mImageView;
    ImageView dangerImageView;
    TextView textView;
    Button pictureButton;
    Button infoButton;

    // Global variables
    String mCurrentPhotoPath;
    Bitmap bitmap;
    JSONObject capturedObject;
    JSONArray imageData;

    // Static global variable
    static final int REQUEST_TAKE_PHOTO = 1;

    // Generate image file to be processed
    private File createImageFile() throws IOException {
        // Remove older image files
        for (File file : getExternalFilesDir(Environment.DIRECTORY_PICTURES).listFiles()) {
            if (file.getName().startsWith("ARSafety")) {
                Log.i("File deleted", file.getName());
                file.delete();
            }
        }

        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "ARSafety_JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    // Deploy camera, capture and store photo
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    // Photo received, further actions to store, analyze and retrieve info about image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            try {
                // Store picture upon capture
                galleryAddPic();
            }
            catch (Exception e) {
                textView.setText("One more time, please...");
            }

            // Encode image, prepare to be sent to server
            encodeImage();

            // POST request to retrieve item information
            apiCall();
        } catch (Exception e) {
            // Something went wrong
            e.printStackTrace();
        }
    }

    // Store picture in pictures directory
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    // Image made accessible only by app to local storage, turned into JSON object
    private void encodeImage() {
        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

        // Determine how much to scale down the image
        int scaleFactor = 8;//Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        // Generate image as bitmap
        bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        Matrix m = new Matrix();
        m.postRotate(neededRotation(new File(mCurrentPhotoPath)));
        bitmap = Bitmap.createBitmap(bitmap,
                0, 0, bitmap.getWidth(), bitmap.getHeight(),
                m, true);

        // Update UI
        textView.setText("Loading...");
        mImageView.setVisibility(View.INVISIBLE);
        textView.setVisibility(View.VISIBLE);
        pictureButton.setText("Take another picture");
        infoButton.setVisibility(View.GONE);

        // Base64 encoding of image
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        String base64 = Base64.encodeToString(byteArray, Base64.DEFAULT);

        // Deliver base64 as JSON object
        capturedObject = new JSONObject();
        try {
            capturedObject.put("img", base64);
            Log.i("Filepath", mCurrentPhotoPath);
            Log.i("JSON", capturedObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Pass image as JSON object to sersver, retrieve information and display to user
    public void apiCall() {

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://arsafety.herokuapp.com/predict";

        // Send POST request to server, receive a response
        JsonObjectRequest postRequest = new JsonObjectRequest( Request.Method.POST, url, capturedObject,

                // Listening for response with workable image information
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Interpreting base64 as bitmap image
                        Log.d("Returned JSON", response.toString());
                        byte[] decodedString = new byte[0];
                        try {
                            decodedString = Base64.decode(response.getString("img"), Base64.DEFAULT);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                        // Update UI
                        mImageView.setImageBitmap(bitmap);
                        mImageView.setVisibility(View.VISIBLE);
                        dangerImageView.setVisibility(View.INVISIBLE);
                        textView.setVisibility(View.INVISIBLE);
                        pictureButton.setText("Take another picture");
                        infoButton.setVisibility(View.VISIBLE);

                        // Store image data
                        try {
                            imageData = response.getJSONArray("predictions");
                            Log.i("Image data", imageData.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                // Listening for response if errors occur
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle Error
                        Log.i("Error",error.toString());
                        mImageView.setVisibility(View.INVISIBLE);
                        dangerImageView.setVisibility(View.VISIBLE);
                        textView.setVisibility(View.VISIBLE);
                        pictureButton.setText("Try again");
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
        queue.add(postRequest);
    }

    // Tell if image needs to be rotated, using orientation tags
    public static int neededRotation(File ff) {
        try {
            ExifInterface exif = new ExifInterface(ff.getAbsolutePath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            if (orientation == ExifInterface.ORIENTATION_ROTATE_270) return 270;
            if (orientation == ExifInterface.ORIENTATION_ROTATE_180) return 180;
            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) return 90;
            return 0;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Call function to take picture
    public void takePicture(View view) {
        dispatchTakePictureIntent();
    }

    // Button pressed for more info, start new activity with list of items
    public void moreInfo(View view) {
        // Check that stored information is not empty
        Log.i("Info", "More info requested");
        if (imageData != null) {
            //Initialize array list to store information about each item recognized
            Log.i("Image Data JSON", imageData.toString());
            textView.setVisibility(View.INVISIBLE);
            ArrayList<String> objects = new ArrayList<String>();
            try {
                // Interpret information from JSON object and store into array list
                JSONArray array = new JSONArray(imageData.toString());
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonPart = array.getJSONObject(i);
                    objects.add(jsonPart.toString());
                }

                // Send information to next activity about image
                Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                try {
                    intent.putExtra("objects", ObjectSerializer.serialize(objects));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Start list item activity
                startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
                textView.setText("Something went wrong!");
                textView.setVisibility(View.VISIBLE);
            }
        }
        else {
            // Handle case in which no objects were recognized
            textView.setText("No objects recognized!");
            textView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Change action bar colour to gray
        ActionBar bar = getActionBar();
        if (bar != null) {
            bar.setBackgroundDrawable(new ColorDrawable(1));
        }

        // Initialize UI elements
        mImageView = findViewById(R.id.mImageView);
        dangerImageView = findViewById(R.id.dangerImageView);
        textView = findViewById(R.id.textView);
        pictureButton = findViewById(R.id.pictureButton);
        infoButton = findViewById(R.id.infoButton);
        infoButton.setVisibility(View.GONE);

        // Request permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }
}