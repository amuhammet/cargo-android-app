package com.example.cargo.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "loads")
data class Load(
    @PrimaryKey
    @SerializedName("id")
    val id: String, // Kargo/Takip Numarası
    
    @SerializedName("description")
    val description: String, // Yük içeriği (Örn: "Elektronik Malzeme")
    
    @SerializedName("status")
    val status: String, // "PREPARING", "ON_THE_WAY", "DELIVERED", "CANCELLED"
    
    @SerializedName("current_latitude")
    val currentLatitude: Double, // Canlı haritada göstermek için anlık enlem
    
    @SerializedName("current_longitude")
    val currentLongitude: Double, // Canlı haritada göstermek için anlık boylam
    
    @SerializedName("courier_id")
    val courierId: String?, // Kargoyu taşıyan kuryenin benzersiz kimliği
    
    @SerializedName("updated_at")
    val updatedAt: Long // Son güncelleme zamanı (Timestamp)
)
