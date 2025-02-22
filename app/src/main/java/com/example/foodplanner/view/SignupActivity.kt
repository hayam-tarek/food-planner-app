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
import androidx.lifecycle.ViewModelProvider
import com.example.foodplanner.MainActivity
import com.example.foodplanner.R
import com.example.foodplanner.utils.AuthState
import com.example.foodplanner.viewModel.AuthViewModel
import es.dmoral.toasty.Toasty

class SignupActivity : AppCompatActivity() {
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var rePasswordInput: EditText
    private lateinit var signupButton: Button
    private lateinit var loginButton: TextView
    private lateinit var authViewModel: AuthViewModel
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
        setUpViewModel()
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
            authViewModel.signUp(emailQuery, passwordQuery)
        }
        authViewModel.errorMessage.observe(this) { message ->
            if (message != null) {
                Toasty.error(this, message).show()
            }
        }
        authViewModel.authState.observe(this) {
            when (it) {
                AuthState.AUTH_SUCCESS -> {
                    Toasty.success(this, "Sign up successful").show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                AuthState.AUTH_IN_PROGRESS -> {
                    signupButton.text = "Loading..."
                }

                AuthState.AUTH_FAILED -> {
                    signupButton.text = "Sign up"
                }
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

    private fun setUpViewModel() {
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
    }
}