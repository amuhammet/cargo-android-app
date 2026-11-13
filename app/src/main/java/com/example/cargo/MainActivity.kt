package com.example.cargo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.cargo.data.local.AppDatabase
import com.example.cargo.data.model.User
import com.example.cargo.databinding.ActivityMainBinding
import com.example.cargo.login.CustomerLoginActivity
import kotlinx.coroutines.launch
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        lifecycleScope.launch {
            val user = AppDatabase.getDatabase(applicationContext).cargoDao().LoggedInUser()
            if (user == null || user.role.uppercase(Locale.ROOT) !in setOf("CUSTOMER", "COURIER")) {
                routeToLogin()
                return@launch
            }
            showDashboard(user)
        }
    }

    private fun showDashboard(user: User) {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as? NavHostFragment
            ?: return

        navController = navHostFragment.navController
        val graph = navController.navInflater.inflate(R.navigation.nav_graph)
        graph.setStartDestination(
            if (user.role.equals("COURIER", ignoreCase = true)) {
                R.id.courierDashboardFragment
            } else {
                R.id.customerDashboardFragment
            }
        )
        navController.graph = graph

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.customerDashboardFragment, R.id.courierDashboardFragment)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    private fun routeToLogin() {
        val intent = Intent(this, CustomerLoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        return if (::navController.isInitialized) {
            navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
        } else {
            super.onSupportNavigateUp()
        }
    }
}
