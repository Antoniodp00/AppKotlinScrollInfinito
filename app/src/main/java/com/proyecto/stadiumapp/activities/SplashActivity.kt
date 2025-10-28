package com.proyecto.stadiumapp.activities

import android.content.Intent
import android.os.Bundle
import com.proyecto.stadiumapp.activities.MainActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
       val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()

    }
}