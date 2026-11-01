package com.example.cargo

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.cargo.databinding.ActivityMainBinding
import com.example.cargo.login.CustomerLoginActivity
import com.example.cargo.login.CourierLoginActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userType = intent.getStringExtra("USER_TYPE")

        if (userType == null) {
            binding.customerLoginButton.setOnClickListener {
                startActivity(Intent(this, CustomerLoginActivity::class.java))
            }
            binding.courierLoginButton.setOnClickListener {
                startActivity(Intent(this, CourierLoginActivity::class.java))
            }
        } else {
            binding.root.visibility = View.GONE // Giriş butonlarını gizle
            val navController = findNavController(R.id.nav_host_fragment)
            if (userType == "CUSTOMER") {
                navController.navigate(R.id.customerDashboardFragment)
            } else {
                navController.navigate(R.id.courierDashboardFragment)
            }
        }
    }
}
