package com.example.cargo.ui.tracking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.cargo.R
import com.example.cargo.data.local.AppDatabase
import com.example.cargo.data.repository.CargoRepository
import com.example.cargo.databinding.FragmentLoadTrackingBinding
import com.example.cargo.network.RetrofitClient
import com.example.cargo.util.showToast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.*

class LoadTrackingFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentLoadTrackingBinding? = null
    private val binding get() = _binding!!
    
    private val args: LoadTrackingFragmentArgs by navArgs()
    private var googleMap: GoogleMap? = null
    private var courierMarker: Marker? = null
    private var trackingLoadId: String = ""
    
    private lateinit var repository: CargoRepository
    private val fragmentScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var trackingJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Repository ve Veritabanı bağlantısı
        val database = AppDatabase.getDatabase(requireContext().applicationContext)
        repository = CargoRepository(RetrofitClient.apiService, database.cargoDao())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoadTrackingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        trackingLoadId = args.loadId
        binding.textViewTrackingIdDisplay.text = "Takip Edilen Kargo: $trackingLoadId"

        if (trackingLoadId.isEmpty()) {
            showToast("Geçersiz Takip Numarası!")
            return
        }

        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap?.uiSettings?.isZoomControlsEnabled = true

        // Harita hazır olduğunda sunucudan/veritabanından anlık konum çekme döngüsünü başlatıyoruz
        startRealTimeTracking(trackingLoadId)
    }

    /**
     * Her 5 saniyede bir kuryenin güncel koordinatlarını takip eden coroutine döngüsü
     */
    private fun startRealTimeTracking(loadId: String) {
        trackingJob?.cancel()
        trackingJob = fragmentScope.launch {
            while (isActive) {
                // Öncellikle yerel veritabanı veya API'den en son kurye konumunu çekiyoruz
                val latestLocation = withContext(Dispatchers.IO) {
                    repository.getLatestLocationFromLocal()
                }

                if (latestLocation != null) {
                    val courierLatLng = LatLng(latestLocation.latitude, latestLocation.longitude)
                    updateCourierMarker(courierLatLng)
                } else {
                    // Eğer veritabanı henüz boşsa simüle başlangıç konumu atayabilirsiniz
                    val fallbackLatLng = LatLng(41.0082, 28.9784)
                    updateCourierMarker(fallbackLatLng)
                }
                
                delay(5000) // 5 saniyede bir güncelle
            }
        }
    }

    /**
     * Harita üzerindeki kurye işaretçisini (Marker) akıcı bir şekilde günceller
     */
    private fun updateCourierMarker(latLng: LatLng) {
        if (courierMarker == null) {
            // İşaretçi haritada yoksa ilk defa oluştur ve kamerayı odakla
            val markerOptions = MarkerOptions()
                .position(latLng)
                .title("Kurye Konumu")
                .snippet("Kargonuz yolda ($trackingLoadId)")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
            
            courierMarker = googleMap?.addMarker(markerOptions)
            googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14f))
        } else {
            // İşaretçi zaten varsa sadece konumunu güncelle (Ekranın titremesini önler)
            courierMarker?.position = latLng
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        trackingJob?.cancel() // Fragment ekranı kapandığında arka plan ağ/veritabanı sorgularını durdurur
        _binding = null
    }
}
