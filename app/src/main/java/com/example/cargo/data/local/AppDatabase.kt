package com.example.cargo.data.local

import android.content.Context
import androidx.room.*
import com.example.cargo.data.model.*

@Database(entities = [User::class, Load::class, RoutePrice::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun loadDao(): LoadDao
    abstract fun routePriceDao(): RoutePriceDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, "cargo_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

// Basit DAO tanımlamaları (Eksik kalmaması için)
@Dao interface UserDao { @Query("SELECT * FROM users") suspend fun getAll(): List<User> }
@Dao interface LoadDao { @Query("SELECT * FROM loads") suspend fun getAll(): List<Load> }
@Dao interface RoutePriceDao { @Query("SELECT * FROM route_prices") suspend fun getAll(): List<RoutePrice> }
