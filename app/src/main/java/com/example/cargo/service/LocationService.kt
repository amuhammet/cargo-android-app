package com.example.cargo.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.cargo.data.local.AppDatabase
import com.example.cargo.data.repository.CargoRepository
import com.example.cargo.network.RetrofitClient
import com.google.android.gms.location.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Cancelable
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class LocationService : Service() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var repository: CargoRepository
    private val serviceScope = CoroutineScope(Dispatchers.IO)

    companion object {
        private const val CHANNEL_ID = "LocationServiceChannel"
        private const val NOTIFICATION_ID = 101
        private const val TAG = "LocationService"
    }

    override fun onCreate() {
        super.onCreate()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        
        // Veritabanı ve Repository bağımlılıklarının başlatılması
        val database = AppDatabase.getDatabase(applicationContext)
        repository = CargoRepository(RetrofitClient.apiService, database.cargoDao())

        // GPS sinyali değiştikçe tetiklenen geri çağırım mekanizması
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                val location = locationResult.lastLocation
                if (location != null) {
                    Log.d(TAG, "Konum Alındı: Lat: ${location.latitude}, Lng: ${location.longitude}")
                    sendLocationToServer(location.latitude, location.longitude)
                }
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
        
        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Kargo Takip Sistemi")
            .setContentText("Kurye konumunuz canlı olarak güncelleniyor...")
            .setSmallIcon(android.R.drawable.ic_menu_mylocation)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()

        startForeground(NOTIFICATION_ID, notification)
        startLocationUpdates()

        return START_STICKY
    }

    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, TimeUnit.SECONDS.toMillis(10))
            .setMinUpdateIntervalMillis(TimeUnit.SECONDS.toMillis(5))
            .build()

        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } catch (unlikely: SecurityException) {
            Log.e(TAG, "Konum izinleri eksik, takip başlatılamadı: $unlikely")
        }
    }

    private fun sendLocationToServer(lat: Double, lng: Double) {
        // Ağ isteğini ana arayüzü kilitlememesi için IO thread havuzunda koşturuyoruz
        serviceScope.launch {
            repository.updateCourierLocation(lat, lng).onSuccess {
                Log.d(TAG, "Koordinatlar API sunucusuna başarıyla basıldı.")
            }.onFailure { istisna ->
                Log.e(TAG, "Koordinat basılırken ağ hatası meydana geldi: ${istisna.localizedMessage}")
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Kurye Konum Servisi",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(serviceChannel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationClient.removeLocationUpdates(locationCallback)
        Log.d(TAG, "Arka plan izleme servisi kapatıldı.")
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
