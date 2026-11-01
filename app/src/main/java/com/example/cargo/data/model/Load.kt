package com.example.cargo.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "loads")
data class Load(
    @PrimaryKey val id: String,
    val weight: Double,
    val dimensions: String,
    val origin: String,
    val destination: String,
    val status: String,
    val trackingNumber: String
)

