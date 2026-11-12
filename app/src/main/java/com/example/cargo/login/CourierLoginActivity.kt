package com.example.cargo.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.cargo.MainActivity
import com.example.cargo.data.local.AppDatabase
import com.example.cargo.data.repository.CargoRepository
import com.example.cargo.databinding.ActivityCourierLoginBinding
import com.example.cargo.network.RetrofitClient
import com.example.cargo.util.isValidEmail
import com.example.cargo.util.showToast
import kotlinx.coroutines.launch

class CourierLoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCourierLoginBinding
    private val repository by lazy {
        CargoRepository(RetrofitClient.apiService, AppDatabase.getDatabase(applicationContext).cargoDao())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCourierLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonCourierLogin.setOnClickListener {
            val email = binding.editTextCourierEmail.text?.toString()?.trim().orEmpty()
            val password = binding.editTextCourierPassword.text?.toString().orEmpty()
            binding.inputLayoutCourierEmail.error = null
            binding.inputLayoutCourierPassword.error = null

            if (!email.isValidEmail()) {
                binding.inputLayoutCourierEmail.error = "Geçerli bir e-posta girin."
                return@setOnClickListener
            }
            if (password.isBlank()) {
                binding.inputLayoutCourierPassword.error = "Şifre boş olamaz."
                return@setOnClickListener
            }

            binding.buttonCourierLogin.isEnabled = false
            lifecycleScope.launch {
                val result = repository.login(email, password, ROLE_COURIER)
                binding.buttonCourierLogin.isEnabled = true
                result.onSuccess {
                    showToast("Giriş başarılı.")
                    startActivity(Intent(this@CourierLoginActivity, MainActivity::class.java))
                    finish()
                }.onFailure { error ->
                    showToast(error.message ?: "Giriş yapılamadı. Sunucu bağlantınızı kontrol edin.")
                }
            }
        }

        binding.textViewSwitchToCustomer.setOnClickListener {
            startActivity(Intent(this, CustomerLoginActivity::class.java))
            finish()
        }
    }

    private companion object {
        const val ROLE_COURIER = "COURIER"
    }
}
