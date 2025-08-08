package com.example.mytask.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.example.mytask.MainActivity
import com.example.mytask.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private val prefs by lazy { getSharedPreferences("MyAppPrefs", MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        // Clear the placeholder text when user starts typing
        binding.emailEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.emailEditText.text.toString() == "user@example.com") {
                binding.emailEditText.setText("")
            }
        }

        binding.passwordEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.passwordEditText.text.toString() == "At least 6 characters") {
                binding.passwordEditText.setText("")
            }
        }

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (validateInput(email, password)) {
                loginUser(email, password)
            }
        }

        binding.signUpLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        var isValid = true

        // Clear previous errors
        binding.emailInputLayout.error = null
        binding.passwordInputLayout.error = null

        if (email.isEmpty() || email == "user@example.com") {
            binding.emailInputLayout.error = "Please enter a valid email"
            isValid = false
        }

        if (password.isEmpty() || password == "At least 6 characters") {
            binding.passwordInputLayout.error = "Please enter your password"
            isValid = false
        } else if (password.length < 6) {
            binding.passwordInputLayout.error = "Password must be at least 6 characters"
            isValid = false
        }

        return isValid
    }

    private fun loginUser(email: String, password: String) {
        binding.loginButton.isEnabled = false
        binding.loginButton.text = "Logging in..."

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                binding.loginButton.isEnabled = true
                binding.loginButton.text = "Login"

                if (task.isSuccessful) {
                    // Save login state
                    prefs.edit {
                        putBoolean("isLoggedIn", true)
                        putString("userEmail", email)
                    }

                    Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }
}