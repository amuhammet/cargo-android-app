package com.example.cargo.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "route_prices")
data class RoutePrice(
    @PrimaryKey(autoGenerate = true)
    val localId: Int = 0, // Yerel veritabanı için otomatik artan ID

    @SerializedName("origin")
    val origin: String, // Çıkış noktası (Örn: "İstanbul")
    
    @SerializedName("destination")
    val destination: String, // Varış noktası (Örn: "Ankara")
    
    @SerializedName("distance_km")
    val distanceKm: Double, // Mesafe
    
    @SerializedName("price")
    val price: Double, // Taşıma ücreti
    
    @SerializedName("currency")
    val currency: String = "TRY" // Para birimi
)
