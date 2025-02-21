package com.example.foodplanner.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.foodplanner.MainActivity
import com.example.foodplanner.R
import com.example.foodplanner.utils.SharedPrefManager

class AnimatedSplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_animated_splash_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        SharedPrefManager.init(this)
        val userUID = SharedPrefManager.getUserUID()
        Handler(Looper.getMainLooper()).postDelayed({
            if (userUID != null) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, WelcomeActivity::class.java)
                startActivity(intent)
            }
            finish()
        }, 6100)
    }
}