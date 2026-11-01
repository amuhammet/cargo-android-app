package com.example.cargo.data.repository

import com.example.cargo.data.local.CargoDao
import com.example.cargo.data.model.Load
import com.example.cargo.data.model.RoutePrice
import com.example.cargo.data.model.User
import com.example.cargo.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CargoRepository(
    private val apiService: ApiService,
    private val cargoDao: CargoDao
) {

    // --- KULLANICI GİRİŞ İŞLEMLERİ ---
    suspend fun login(loginRequest: Map<String, String>): Result<User> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.loginUser(loginRequest)
            if (response.isSuccessful && response.body() != null) {
                val user = response.body()!!
                cargoDao.insertUser(user)
                Result.success(user)
            } else {
                Result.failure(Exception("Giriş başarısız: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getLocalUser(): User? = withContext(Dispatchers.IO) {
        cargoDao.LoggedInUser()
    }

    suspend fun logout() = withContext(Dispatchers.IO) {
        cargoDao.clearUser()
    }

    // --- KURYENİN ANLIK KONUM GÜNCELLEMESİ ---
    suspend fun updateCourierLocation(lat: Double, lng: Double): Result<Void?> = withContext(Dispatchers.IO) {
        try {
            val locationData = mapOf("latitude" to lat, "longitude" to lng)
            val response = apiService.updateLocation(locationData)
            if (response.isSuccessful) {
                Result.success(null)
            } else {
                Result.failure(Exception("Konum sunucuya gönderilemedi: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // --- ROTA VE FİYAT SORGULAMA ---
    suspend fun getRoutePrice(origin: String, destination: String): Result<RoutePrice> = withContext(Dispatchers.IO) {
        try {
            val localPrice = cargoDao.getPriceForRoute(origin, destination)
            if (localPrice != null) {
                return@withContext Result.success(localPrice)
            }

            val response = apiService.getRoutePrice(origin, destination)
            if (response.isSuccessful && response.body() != null) {
                val routePrice = response.body()!!
                cargoDao.insertRoutePrices(listOf(routePrice))
                Result.success(routePrice)
            } else {
                Result.failure(Exception("Fiyat bilgisi alınamadı: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // --- KARGO / YÜK CANLI TAKİP ---
    suspend fun getLoadTracking(loadId: String): Result<Load> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getLoadTracking(loadId)
            if (response.isSuccessful && response.body() != null) {
                val load = response.body()!!
                cargoDao.insertLoad(load)
                Result.success(load)
            } else {
                val localLoad = cargoDao.getLoadById(loadId)
                if (localLoad != null) {
                    Result.success(localLoad)
                } else {
                    Result.failure(Exception("Kargo bulunamadı: ${response.message()}"))
                }
            }
        } catch (e: Exception) {
            val localLoad = cargoDao.getLoadById(loadId)
            if (localLoad != null) {
                Result.success(localLoad)
            } else {
                Result.failure(e)
            }
        }
    }
}
