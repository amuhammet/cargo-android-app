package com.example.cargo.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "courier_location")
data class CourierLocation(
    @PrimaryKey val id: Int = 1, // Tek bir kurye konumu tutulacağı için sabit ID
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long = System.currentTimeMillis()
)

data class LocationUpdateRequest(
    val latitude: Double,
    val longitude: Double
)
