package com.example.cargo.data.local

import androidx.room.*
import com.example.cargo.data.model.Load
import com.example.cargo.data.model.RoutePrice
import com.example.cargo.data.model.User
import com.example.cargo.data.model.CourierLocation

@Dao
interface CargoDao {

    // --- Kullanıcı İşlemleri ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users LIMIT 1")
    suspend fun LoggedInUser(): User?

    @Query("DELETE FROM users")
    suspend fun clearUser()

    // --- Rota ve Fiyat İşlemleri ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoutePrices(prices: List<RoutePrice>)

    @Query("SELECT * FROM route_prices WHERE origin = :origin AND destination = :destination LIMIT 1")
    suspend fun getPriceForRoute(origin: String, destination: String): RoutePrice?

    // --- Yük / Kargo Takip İşlemleri ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLoad(load: Load)

    @Query("SELECT * FROM loads WHERE id = :loadId")
    suspend fun getLoadById(loadId: String): Load?

    // --- Kurye Canlı Konum İşlemleri ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(location: CourierLocation)

    @Query("SELECT * FROM courier_location WHERE id = 1")
    suspend fun getLatestLocation(): CourierLocation?
}
