package com.example.cargo.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.cargo.MainActivity
import com.example.cargo.data.local.AppDatabase
import com.example.cargo.data.repository.CargoRepository
import com.example.cargo.databinding.ActivityCustomerLoginBinding
import com.example.cargo.network.RetrofitClient
import com.example.cargo.util.isValidEmail
import com.example.cargo.util.showToast
import kotlinx.coroutines.launch

class CustomerLoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCustomerLoginBinding
    private val repository by lazy {
        CargoRepository(RetrofitClient.apiService, AppDatabase.getDatabase(applicationContext).cargoDao())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonCustomerLogin.setOnClickListener {
            val email = binding.editTextCustomerEmail.text?.toString()?.trim().orEmpty()
            val password = binding.editTextCustomerPassword.text?.toString().orEmpty()
            binding.inputLayoutCustomerEmail.error = null
            binding.inputLayoutCustomerPassword.error = null

            if (!email.isValidEmail()) {
                binding.inputLayoutCustomerEmail.error = "Geçerli bir e-posta girin."
                binding.editTextCustomerEmail.requestFocus()
                return@setOnClickListener
            }
            if (password.isBlank()) {
                binding.inputLayoutCustomerPassword.error = "Şifre boş olamaz."
                binding.editTextCustomerPassword.requestFocus()
                return@setOnClickListener
            }

            setLoading(true)
            lifecycleScope.launch {
                try {
                    val result = repository.login(email, password, ROLE_CUSTOMER)
                    result.onSuccess {
                        showToast("Giriş başarılı.")
                        startActivity(Intent(this@CustomerLoginActivity, MainActivity::class.java))
                        finish()
                    }.onFailure { error ->
                        val errorMessage = error.message ?: "Giriş yapılamadı. Sunucu bağlantınızı kontrol edin."
                        showToast(errorMessage)
                    }
                } finally {
                    if (!isFinishing) setLoading(false)
                }
            }
        }

        binding.textViewSwitchToCourier.setOnClickListener {
            startActivity(Intent(this, CourierLoginActivity::class.java))
            finish()
        }
    }

    private fun setLoading(loading: Boolean) {
        binding.buttonCustomerLogin.isEnabled = !loading
        binding.textViewSwitchToCourier.isEnabled = !loading
        binding.progressLogin.visibility = if (loading) View.VISIBLE else View.GONE
    }

    private companion object {
        const val ROLE_CUSTOMER = "CUSTOMER"
    }
}
