package com.example.cargo.network

import com.example.cargo.data.model.CourierLocation
import com.example.cargo.data.model.Load
import com.example.cargo.data.model.RoutePrice
import com.example.cargo.data.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("user/login")
    suspend fun loginUser(
        @Body loginRequest: Map<String, String>
    ): Response<User>

    @POST("courier/update-location")
    suspend fun updateLocation(
        @Body locationRequest: CourierLocation
    ): Response<Void>

    @GET("route/price")
    suspend fun getRoutePrice(
        @Query("origin") origin: String,
        @Query("destination") destination: String
    ): Response<List<RoutePrice>>

    @GET("load/details")
    suspend fun getLoadDetails(
        @Query("loadId") loadId: String
    ): Response<Load>

    @GET("load/tracking")
    suspend fun getLoadTracking(
        @Query("loadId") loadId: String
    ): Response<Load>
}
