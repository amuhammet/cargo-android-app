package com.example.cargo.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "route_prices")
data class RoutePrice(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val route: String,
    val price: Double,
    val estimatedDeliveryTime: String
)
