package com.example.myapplication

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.initialize

class MyApplicationApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize Firebase
        Firebase.initialize(this)
    }
}


