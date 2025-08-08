package com.example.mytask.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.example.mytask.MainActivity
import com.example.mytask.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private val prefs by lazy { getSharedPreferences("MyAppPrefs", MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        // Clear placeholder text when user focuses
        binding.nameEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.nameEditText.text.toString() == "Enter your full name") {
                binding.nameEditText.setText("")
            }
        }

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

        binding.createAccountButton.setOnClickListener {
            val name = binding.nameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (validateInput(name, email, password)) {
                registerUser(name, email, password)
            }
        }

        binding.signInLink.setOnClickListener {
            finish() // Go back to login
        }
    }

    private fun validateInput(name: String, email: String, password: String): Boolean {
        var isValid = true

        // Clear previous errors
        binding.nameInputLayout.error = null
        binding.emailInputLayout.error = null
        binding.passwordInputLayout.error = null

        if (name.isEmpty() || name == "Enter your full name") {
            binding.nameInputLayout.error = "Please enter your name"
            isValid = false
        }

        if (email.isEmpty() || email == "user@example.com") {
            binding.emailInputLayout.error = "Please enter a valid email"
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailInputLayout.error = "Please enter a valid email address"
            isValid = false
        }

        if (password.isEmpty() || password == "At least 6 characters") {
            binding.passwordInputLayout.error = "Please enter a password"
            isValid = false
        } else if (password.length < 6) {
            binding.passwordInputLayout.error = "Password must be at least 6 characters"
            isValid = false
        }

        return isValid
    }

    private fun registerUser(name: String, email: String, password: String) {
        binding.createAccountButton.isEnabled = false
        binding.createAccountButton.text = "Creating Account..."

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                binding.createAccountButton.isEnabled = true
                binding.createAccountButton.text = "Create Account"

                if (task.isSuccessful) {
                    // Save user info and login state
                    prefs.edit {
                        putBoolean("isLoggedIn", true)
                        putString("userEmail", email)
                        putString("userName", name)
                    }

                    Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }
}