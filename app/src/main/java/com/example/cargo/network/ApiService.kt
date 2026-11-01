package com.example.cargo.network

import com.example.cargo.data.model.LocationUpdateRequest
import com.example.cargo.data.model.RoutePrice
import com.example.cargo.data.model.Load
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    // =================================================================================
    // 1. Kurye Canlı Konum İşlemleri
    // =================================================================================
    
    /**
     * Kuryenin anlık GPS koordinatlarını sunucuya gönderir.
     */
    @POST("courier/update-location")
    suspend fun updateLocation(
        @Body locationRequest: LocationUpdateRequest
    ): Response<Void>

    // =================================================================================
    // 2. Rota ve Fiyatlandırma İşlemleri
    // =================================================================================

    /**
     * Başlangıç ve bitiş noktalarına göre tahmini rota fiyatlandırma detaylarını çeker.
     */
    @GET("route/price")
    suspend fun getRoutePrice(
        @Query("origin") origin: String,
        @Query("destination") destination: String
    ): Response<List<RoutePrice>>

    // =================================================================================
    // 3. Yük / Kargo Takip İşlemleri
    // =================================================================================

    /**
     * Belirli bir kargonun (Load) durumunu ve canlı takip bilgilerini getirir.
     */
    @GET("load/details")
    suspend fun getLoadDetails(
        @Query("loadId") loadId: String
    ): Response<Load>
}
