package com.example.foodplanner.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.foodplanner.R

class SignupActivity : AppCompatActivity() {
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var rePasswordInput: EditText
    private lateinit var signupButton: Button
    private lateinit var loginButton: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initUi()
        loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        signupButton.setOnClickListener {
            val emailQuery = emailInput.text.toString()
            val passwordQuery = passwordInput.text.toString()
            val rePasswordQuery = rePasswordInput.text.toString()
            if (emailQuery.isEmpty() || !emailQuery.contains('@')) {
                emailInput.error = "Please enter a valid email"
                return@setOnClickListener
            }
            if (passwordQuery.isEmpty() || passwordQuery.length < 8) {
                passwordInput.error = "Please enter a strong password"
                return@setOnClickListener
            }
            if (!rePasswordQuery.equals(passwordQuery)) {
                rePasswordInput.error = "Passwords don't match."
                return@setOnClickListener
            }

        }
    }

    private fun initUi() {
        emailInput = findViewById(R.id.signupEmailInput)
        passwordInput = findViewById(R.id.signupPasswordInput)
        rePasswordInput = findViewById(R.id.signupRePasswordInput)
        signupButton = findViewById(R.id.signupSignupBtn)
        loginButton = findViewById(R.id.signUpLoginTxtBtn)
    }
}