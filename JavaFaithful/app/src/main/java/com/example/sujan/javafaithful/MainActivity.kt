package com.example.sujan.javafaithful

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val myDawg = Dog("Nick", 5)

        val list = ArrayList<String>()
        list.add("A")
        list.add("B")
        list.add("C")
    }
}
