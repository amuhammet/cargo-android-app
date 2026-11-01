package com.example.cargo.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cargo.MainActivity
import com.example.cargo.databinding.ActivityCustomerLoginBinding
import com.example.cargo.util.showToast
import com.example.cargo.util.isValidEmail

class CustomerLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCustomerLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Giriş Butonu Tıklama Etkinliği
        binding.buttonCustomerLogin.setOnClickListener {
            val email = binding.editTextCustomerEmail.text.toString().trim()
            val password = binding.editTextCustomerPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                showToast("Lütfen tüm alanları doldurun.")
                return@setOnClickListener
            }

            if (!email.isValidEmail()) {
                showToast("Geçersiz e-posta adresi.")
                return@setOnClickListener
            }

            // TODO: CargoRepository üzerinden API girişi tetiklenecek.
            // Şimdilik simüle ederek ana akışa (Dashboard) yönlendiriyoruz.
            showToast("Müşteri girişi başarılı!")
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Kurye Giriş Ekranına Geçiş Butonu
        binding.textViewSwitchToCourier.setOnClickListener {
            val intent = Intent(this, CourierLoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
