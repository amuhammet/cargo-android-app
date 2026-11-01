package com.example.cargo.util

fun String.toFormattedPrice(): String {
    return "$$this"
}

fun Int.toFormattedLoadWeight(): String {
    return "$this kg"
}

fun String.isValidEmail(): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isValidPassword(): Boolean {
    return this.length >= 6
}