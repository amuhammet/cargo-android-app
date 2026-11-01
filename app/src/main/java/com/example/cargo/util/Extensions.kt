package com.example.cargo.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment

/**
 * Context üzerinden kolayca kısa süreli Toast mesajı gösterir.
 */
fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

/**
 * Fragment üzerinden kolayca kısa süreli Toast mesajı gösterir.
 */
fun Fragment.showToast(message: String) {
    context?.showToast(message)
}

/**
 * Görünümü (View) görünür (VISIBLE) yapar.
 */
fun View.show() {
    this.visibility = View.VISIBLE
}

/**
 * Görünümü (View) gizler (GONE) ve ekranda yer kaplamasını engeller.
 */
fun View.hide() {
    this.visibility = View.GONE
}

/**
 * Açık olan sanal klavyeyi programatik olarak kapatır.
 */
fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

/**
 * String ifadenin geçerli bir e-posta adresi olup olmadığını doğrular.
 */
fun String.isValidEmail(): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}
