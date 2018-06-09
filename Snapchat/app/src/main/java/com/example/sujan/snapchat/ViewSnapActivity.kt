package com.example.sujan.snapchat

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.net.HttpURLConnection
import java.net.URL

class ViewSnapActivity : AppCompatActivity() {

    var mAuth = FirebaseAuth.getInstance()

    var messageTextView: TextView? = null
    var snapImageView: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_snap)

        messageTextView = findViewById(R.id.messageTextView)
        snapImageView = findViewById(R.id.snapImageView)

        messageTextView!!.text = intent.getStringExtra("message")

        val downloader = ImageDownloader()
        val image: Bitmap
        try {
            image = downloader.execute(intent.getStringExtra("imageURL")).get()!!
            snapImageView!!.setImageBitmap(image)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()

        FirebaseDatabase.getInstance().reference.child("users").child(mAuth.currentUser!!.uid)
                .child("snaps").child(intent.getStringExtra("snapKey")).removeValue()

        FirebaseStorage.getInstance().reference.child("images")
                .child(intent.getStringExtra("imageName")).delete()
    }

    inner class ImageDownloader : AsyncTask<String, Void, Bitmap>() {

        override fun doInBackground(vararg urls: String): Bitmap? {
            try {
                val url = URL(urls[0])
                val urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.connect()
                val `in` = urlConnection.inputStream
                return BitmapFactory.decodeStream(`in`)
            } catch (e: Exception) {
                return null
            }

        }
    }
}
