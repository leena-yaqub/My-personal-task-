package com.example.mytask.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.mytask.MainActivity
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    private val prefs by lazy { getSharedPreferences("MyAppPrefs", MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Add a small delay to see splash screen
        Handler(Looper.getMainLooper()).postDelayed({
            checkUserLoginStatus()
        }, 1000) // 1 second delay
    }

    private fun checkUserLoginStatus() {
        try {
            val isLoggedIn = prefs.getBoolean("isLoggedIn", false)
            val firebaseUser = FirebaseAuth.getInstance().currentUser

            val nextActivity = if (isLoggedIn && firebaseUser != null) {
                // User is logged in, go to MainActivity
                MainActivity::class.java
            } else {
                // User not logged in, go to LoginActivity
                LoginActivity::class.java
            }

            val intent = Intent(this, nextActivity)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()

        } catch (e: Exception) {
            // If anything goes wrong, go to login
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}