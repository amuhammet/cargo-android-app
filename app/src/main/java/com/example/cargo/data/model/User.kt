package com.example.cargo.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    @SerializedName("id")
    val id: String,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("email")
    val email: String,
    
    @SerializedName("role") 
    val role: String, // "CUSTOMER" veya "COURIER"
    
    @SerializedName("token")
    val token: String? = null // API isteklerinde yetkilendirme için JWT Token
)
