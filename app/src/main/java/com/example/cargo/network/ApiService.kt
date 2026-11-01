package com.example.cargo.network

import com.example.cargo.data.model.*
import retrofit2.http.*

interface ApiService {
    @GET("routes/{start}/{end}/price")
    suspend fun getRoutePrice(@Path("start") start: String, @Path("end") end: String): RoutePrice

    @GET("loads/{loadId}/track")
    suspend fun trackLoad(@Path("loadId") loadId: String): Load

    @GET("users/{userId}")
    suspend fun getUserData(@Path("userId") userId: String): User
}
