package com.example.cargo.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cargo.R
import com.example.cargo.ui.dashboard.CourierDashboardFragment

class CourierLoginActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_courier_login)

        usernameEditText = findViewById(R.id.usernameEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)

        loginButton.setOnClickListener {
            loginCourier()
        }
    }

    private fun loginCourier() {
        val username = usernameEditText.text.toString()
        val password = passwordEditText.text.toString()

        if (username.isNotEmpty() && password.isNotEmpty()) {
            // Here you would typically validate the credentials with a backend service
            // For demonstration, we assume the login is successful
            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
            navigateToDashboard()
        } else {
            Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToDashboard() {
        val intent = Intent(this, CourierDashboardFragment::class.java)
        startActivity(intent)
        finish()
    }
}