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
import com.google.android.gms.location.*
import java.util.concurrent.TimeUnit

class LocationService : Service() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    companion object {
        private const val CHANNEL_ID = "LocationServiceChannel"
        private const val NOTIFICATION_ID = 101
        private const val TAG = "LocationService"
    }

    override fun onCreate() {
        super.onCreate()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Konum güncellemeleri geldiğinde tetiklenecek fonksiyon
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                val location = locationResult.lastLocation
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    Log.d(TAG, "Yeni Koordinat: Lat: $latitude, Lng: $longitude")
                    
                    // TODO: Buradan ApiService veya Repository aracılığıyla sunucuya istek atılacak
                    sendLocationToServer(latitude, longitude)
                }
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
        
        // Kullanıcıya gösterilecek zorunlu bildirim (Foreground Service için)
        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Kargo Takip Sistemi")
            .setContentText("Kurye konumunuz canlı olarak güncelleniyor...")
            .setSmallIcon(android.R.drawable.ic_menu_mylocation)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()

        // Servisi ön planda başlat
        startForeground(NOTIFICATION_ID, notification)
        
        // Konum takibini başlat
        startLocationUpdates()

        return START_STICKY // Servis beklenmedik şekilde kapanırsa OS tarafından yeniden başlatılır
    }

    private fun startLocationUpdates() {
        // Her 10 saniyede bir, en hızlı ise 5 saniyede bir konum iste
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
            Log.e(TAG, "Konum izni alınamadığı için servis başlatılamadı: $unlikely")
        }
    }

    private fun sendLocationToServer(lat: Double, lng: Double) {
        // ApiService kurulduğunda burası doldurulacak
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
        // Servis durdurulduğunda pil tüketimini önlemek için GPS'i kapat
        fusedLocationClient.removeLocationUpdates(locationCallback)
        Log.d(TAG, "Konum servisi durduruldu.")
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null // Bound service kullanmıyoruz
    }
}
