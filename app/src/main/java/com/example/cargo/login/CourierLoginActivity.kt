package com.example.cargo.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cargo.MainActivity
import com.example.cargo.databinding.ActivityCourierLoginBinding
import com.example.cargo.util.showToast
import com.example.cargo.util.isValidEmail

class CourierLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCourierLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCourierLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Giriş Butonu Tıklama Etkinliği
        binding.buttonCourierLogin.setOnClickListener {
            val email = binding.editTextCourierEmail.text.toString().trim()
            val password = binding.editTextCourierPassword.text.toString().trim()

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
            showToast("Kurye girişi başarılı!")
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Müşteri Giriş Ekranına Geçiş Butonu
        binding.textViewSwitchToCustomer.setOnClickListener {
            val intent = Intent(this, CustomerLoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
