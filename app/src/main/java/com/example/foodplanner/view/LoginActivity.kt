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

class LoginActivity : AppCompatActivity() {
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginButton: Button
    private lateinit var signupButton: TextView
    private lateinit var authViewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initUi()
        setUpViewModel()
        signupButton.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
            finish()
        }
        loginButton.setOnClickListener {
            val emailQuery = emailInput.text.toString()
            val passwordQuery = passwordInput.text.toString()
            if (emailQuery.isEmpty() || !emailQuery.contains('@')) {
                emailInput.error = "Please enter a valid email"
                return@setOnClickListener
            }
            if (passwordQuery.isEmpty()) {
                passwordInput.error = "Please enter a password"
                return@setOnClickListener
            }
            authViewModel.signIn(emailQuery, passwordQuery)
        }
        authViewModel.errorMessage.observe(this) { message ->
            if (message != null) {
                Toasty.error(this, message).show()
            }
        }
        authViewModel.authState.observe(this) {
            when (it) {
                AuthState.AUTH_SUCCESS -> {
                    Toasty.success(this, "Login successful").show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                AuthState.AUTH_IN_PROGRESS -> {
                    loginButton.text = "Logging in..."
                }

                AuthState.AUTH_FAILED -> {
                    loginButton.text = "Login"
                }
            }
        }
    }

    private fun initUi() {
        emailInput = findViewById(R.id.loginEmailInput)
        passwordInput = findViewById(R.id.loginPasswordInput)
        loginButton = findViewById(R.id.loginLoginBtn)
        signupButton = findViewById(R.id.loginSignUpTxtBtn)
    }

    private fun setUpViewModel() {
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
    }
}