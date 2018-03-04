package com.example.sujan.guessthecelebrity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> names = new ArrayList<String>();
    ArrayList<String> imageURLs = new ArrayList<String>();
    int correctChoice;
    int correctIndex;

    ImageView celebrityImageView;
    Button optionButton1;
    Button optionButton2;
    Button optionButton3;
    Button optionButton4;

    public void downloadImage(View view, String url) {
        celebrityImageView = (ImageView) findViewById(R.id.celebrityImageView);
        ImageDownloader downloader = new ImageDownloader();
        Bitmap image;
        try {
            image = downloader.execute(url).get();
            celebrityImageView.setImageBitmap(image);
            Log.i("Info", "Image downloaded");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void generateQuestion() {
        ArrayList<Integer> chosenFour = new ArrayList<Integer>();
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            int index;
            do {
                index = random.nextInt(imageURLs.size());
            } while (chosenFour.contains(index));
            chosenFour.add(index);
        }
        correctChoice = random.nextInt(4) + 1;
        correctIndex = chosenFour.get(correctChoice - 1);
        downloadImage(celebrityImageView, imageURLs.get(correctIndex));

        Button optionButton1 = (Button) findViewById(R.id.optionButton1);
        optionButton1.setText(names.get(chosenFour.get(0)));

        Button optionButton2 = (Button) findViewById(R.id.optionButton2);
        optionButton2.setText(names.get(chosenFour.get(1)));

        Button optionButton3 = (Button) findViewById(R.id.optionButton3);
        optionButton3.setText(names.get(chosenFour.get(2)));

        Button optionButton4 = (Button) findViewById(R.id.optionButton4);
        optionButton4.setText(names.get(chosenFour.get(3)));
    }

    public void buttonClicked(View view) {
        if (view.getTag().equals(Integer.toString(correctChoice))) {
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Wrong! It was " + names.get(correctIndex), Toast.LENGTH_SHORT).show();
        }
        generateQuestion();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DownloadTask downloadTask = new DownloadTask();
        String result = null;
        try {
            result = downloadTask.execute("http://www.posh24.se/kandisar").get().split("<div class=\"listedArticles\">")[0];
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        Pattern p = Pattern.compile("alt=\"(.*?)\"");
        Matcher m = p.matcher(result);

        while (m.find()) {
            names.add(m.group(1));
        }

        Log.i("Info", Integer.toString(names.size()) + " names added");

        p = Pattern.compile("img src=\"(.*?)\"");
        m = p.matcher(result);

        while (m.find()){
            imageURLs.add(m.group(1));
        }

        Log.i("Info", Integer.toString(imageURLs.size()) + " URLs added");

        generateQuestion();
    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection;
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

                Log.i("Info", "Html code parsed");

                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return "Failed";
            }
        }
    }

    public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                InputStream in = urlConnection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(in);
                return bitmap;
            } catch (Exception e) {
                return null;
            }
        }
    }
}
