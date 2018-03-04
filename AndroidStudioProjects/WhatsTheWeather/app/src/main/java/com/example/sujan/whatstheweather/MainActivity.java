package com.example.sujan.whatstheweather;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    EditText cityEditText;
    TextView infoTextView;

    public void checkWeather(View view) {
        DownloadTask downloadTask = new DownloadTask();
        try {
            String url = "http://openweathermap.org/data/2.5/weather?q=" + cityEditText.getText().toString() + "&appid=b6907d289e10d714a6e88b30761fae22";
            downloadTask.execute(url).get();
            Log.i("City", cityEditText.getText().toString());
        }
        catch (Exception e) {
            e.printStackTrace();
            infoTextView.setText("Could not find weather!");
        }

        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(cityEditText.getWindowToken(), 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityEditText = (EditText) findViewById(R.id.cityEditText);
        infoTextView = (TextView) findViewById(R.id.infoTextView);
    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;
            }
            catch (Exception e) {
                e.printStackTrace();
                infoTextView.setText("Could not find weather!");
                return "Failed";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jsonObject = new JSONObject(s);

                String weatherInfo = jsonObject.getString("weather");
                Log.i("Weather info", weatherInfo);

                JSONArray array = new JSONArray(weatherInfo);
                String output = "";
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonPart = array.getJSONObject(i);

                    String main = jsonPart.getString("main");
                    String description = jsonPart.getString("description");
                    if (!(main.equals("") || description.equals(""))) {
                        output += main.substring(0, 1).toUpperCase() + main.substring(1).toLowerCase() + ": " + description;
                    }
                    else {
                        infoTextView.setText("Could not find weather!");
                    }
                }
                if (!output.equals("")) {
                    infoTextView.setText(output);
                }
                else {
                    infoTextView.setText("Could not find weather!");
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                infoTextView.setText("Could not find weather!");
            }
        }
    }
}
