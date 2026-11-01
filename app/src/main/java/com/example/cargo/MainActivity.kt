package com.example.cargo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.cargo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // ViewBinding kurulumu
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Toolbar'ı uygulamanın ActionBar'ı olarak ayarla
        setSupportActionBar(binding.toolbar)

        // NavHostFragment üzerinden NavController erişimi
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Geri butonunun hangi ekranlarda çıkmayacağını (Ana ekranlar) belirle
        appBarConfiguration = AppBarConfiguration(navController.graph)

        // ActionBar'ı Jetpack Navigation ile bağla (Başlıklar otomatik güncellenir)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    /**
     * Sol üstteki geri (up) butonuna basıldığında NavGraph akışına göre geri gidilmesini sağlar.
     */
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
