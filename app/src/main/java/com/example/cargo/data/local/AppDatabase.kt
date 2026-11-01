package com.example.cargo.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cargo.data.model.Load
import com.example.cargo.data.model.RoutePrice
import com.example.cargo.data.model.User

@Database(
    entities = [User::class, RoutePrice::class, Load::class], 
    version = 1, 
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun cargoDao(): CargoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // Eğer veritabanı daha önce oluşturulmuşsa mevcut olanı döndür, yoksa sıfırdan oluştur
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "cargo_database"
                )
                // Versiyon değişikliklerinde verilerin güvenli bir şekilde sıfırlanıp yeniden kurulması için
                .fallbackToDestructiveMigration() 
                .build()
                
                INSTANCE = instance
                instance
            }
        }
    }
}
