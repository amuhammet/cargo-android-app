package com.example.cargo.data.repository

import com.example.cargo.data.local.CargoDao
import com.example.cargo.data.model.CourierLocation
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

    suspend fun login(email: String, password: String, expectedRole: String): Result<User> =
        withContext(Dispatchers.IO) {
            try {
                val response = apiService.loginUser(
                    mapOf("email" to email, "password" to password, "role" to expectedRole)
                )
                val user = response.body()
                if (!response.isSuccessful) {
                    return@withContext Result.failure(
                        Exception("Giriş başarısız (HTTP ${response.code()}). E-posta veya şifrenizi kontrol edin.")
                    )
                }
                if (user == null) {
                    return@withContext Result.failure(Exception("Sunucu boş kullanıcı yanıtı döndürdü."))
                }
                if (!user.role.equals(expectedRole, ignoreCase = true)) {
                    return@withContext Result.failure(Exception("Bu hesap ${expectedRole.lowercase()} girişi için yetkili değil."))
                }
                cargoDao.clearUser()
                cargoDao.insertUser(user)
                Result.success(user)
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

    suspend fun updateCourierLocation(lat: Double, lng: Double): Result<Void?> = withContext(Dispatchers.IO) {
        try {
            val locationData = CourierLocation(latitude = lat, longitude = lng)
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

    suspend fun getRoutePrice(origin: String, destination: String): Result<RoutePrice> = withContext(Dispatchers.IO) {
        try {
            val localPrice = cargoDao.getPriceForRoute(origin, destination)
            if (localPrice != null) {
                return@withContext Result.success(localPrice)
            }

            val response = apiService.getRoutePrice(origin, destination)
            val routePrices = response.body()
            if (response.isSuccessful && !routePrices.isNullOrEmpty()) {
                val routePrice = routePrices.first()
                cargoDao.insertRoutePrices(routePrices)
                Result.success(routePrice)
            } else {
                Result.failure(Exception("Fiyat bilgisi alınamadı: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

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
