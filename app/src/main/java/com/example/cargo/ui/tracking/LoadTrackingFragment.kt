package com.example.cargo.ui.tracking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.cargo.R
import com.example.cargo.databinding.FragmentLoadTrackingBinding
import com.example.cargo.util.showToast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class LoadTrackingFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentLoadTrackingBinding? = null
    private val binding get() = _binding!!
    
    // Safe Args ile gelen kargo takip ID'sini yakalıyoruz
    private val args: LoadTrackingFragmentArgs by navArgs()
    private var googleMap: GoogleMap? = null
    private var trackingLoadId: String = ""

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

        // XML içindeki SupportMapFragment bileşenini bulup haritayı yüklüyoruz
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // Harita ayarlarını yapılandır (Yakınlaştırma butonları vb.)
        googleMap?.uiSettings?.isZoomControlsEnabled = true

        // TODO: CargoRepository üzerinden kargonun anlık koordinatları ApiService ile çekilecek.
        // Şimdilik harita üzerinde simüle bir kargo konumu (Örn: İstanbul koordinatları) gösteriyoruz.
        val simulatedCargoLocation = LatLng(41.0082, 28.9784) 

        // Haritaya kurye/kargo konumunu belirten bir işaretçi ekle
        googleMap?.addMarker(
            MarkerOptions()
                .position(simulatedCargoLocation)
                .title("Kargo Burada")
                .snippet("Durum: Dağıtımda ($trackingLoadId)")
        )

        // Kamerayı kargonun konumuna yakınlaştırarak odakla
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(simulatedCargoLocation, 12f))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
