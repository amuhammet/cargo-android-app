// Gerekli import satırlarını dosyanın üstüne ekleyin
import com.example.cargo.network.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// ... sınıfın diğer kodları ...

private fun sendLocationToServer(lat: Double, lng: Double) {
    // Servis arka planda çalıştığı için API isteğini Coroutine kullanarak IO Thread'inde başlatıyoruz
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val locationData = mapOf("latitude" to lat, "longitude" to lng)
            val response = RetrofitClient.apiService.updateLocation(locationData)
            
            if (response.isSuccessful) {
                Log.d("LocationService", "Konum sunucuda başarıyla güncellendi: $lat, $lng")
            } else {
                Log.e("LocationService", "Konum güncellenirken sunucu hatası oluştu: ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("LocationService", "Konum sunucuya gönderilirken ağ hatası oluştu: ${e.localizedMessage}")
        }
    }
}
