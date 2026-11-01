package com.example.cargo.data.repository

import com.example.cargo.data.model.*
import com.example.cargo.network.ApiService

class CargoRepository(private val apiService: ApiService) {
    suspend fun getRoutePrice(start: String, end: String) = apiService.getRoutePrice(start, end)
    suspend fun trackLoad(loadId: String) = apiService.trackLoad(loadId)
    suspend fun getUserData(userId: String) = apiService.getUserData(userId)
}
