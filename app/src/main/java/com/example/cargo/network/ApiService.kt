package com.example.cargo.network

import com.example.cargo.data.model.RoutePrice
import com.example.cargo.data.model.Load
import com.example.cargo.data.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    // Kullanıcı (Müşteri veya Kurye) Girişi
    @POST("api/auth/login")
    suspend fun loginUser(
        @Body loginRequest: Map<String, String>
    ): Response<User>

    // Kurye Konum Güncelleme (Az önce yazdığımız LocationService içerisinden çağrılacak)
    @POST("api/courier/location")
    suspend fun updateLocation(
        @Body locationData: Map<String, Double> // Örn: {"latitude": 41.01, "longitude": 28.97}
    ): Response<Void>

    // Belirli Rotalar İçin Fiyat Bilgisi Getirme
    @GET("api/routes/price")
    suspend fun getRoutePrice(
        @Query("origin") origin: String,
        @Query("destination") destination: String
    ): Response<RoutePrice>

    // Aktif Yük Durumu / Canlı Takip Verisi Getirme
    @GET("api/loads/{loadId}")
    suspend fun getLoadTracking(
        @Path("loadId") loadId: String
    ): Response<Load>
}
