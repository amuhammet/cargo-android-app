package com.example.cargo.ui.tracking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

        trackingLoadId = args.loadId.trim()
        binding.textViewTrackingIdDisplay.text = "Takip Edilen Kargo: $trackingLoadId"

        if (trackingLoadId.isBlank()) {
            showToast("Geçersiz Takip Numarası!")
            findNavController().popBackStack()
            return
        }

        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap?.uiSettings?.isZoomControlsEnabled = true
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(41.0082, 28.9784), 10f))
        startRealTimeTracking(trackingLoadId)
    }

    private fun startRealTimeTracking(loadId: String) {
        trackingJob?.cancel()
        trackingJob = fragmentScope.launch {
            while (isActive) {
                val latestLocation = withContext(Dispatchers.IO) {
                    repository.getLatestLocationFromLocal()
                }

                val courierLatLng = if (latestLocation != null) {
                    LatLng(latestLocation.latitude, latestLocation.longitude)
                } else {
                    LatLng(41.0082, 28.9784)
                }

                updateCourierMarker(courierLatLng)
                delay(5000)
            }
        }
    }

    private fun updateCourierMarker(latLng: LatLng) {
        val map = googleMap ?: return

        if (courierMarker == null) {
            courierMarker = map.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title("Kurye Konumu")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            )
        } else {
            courierMarker?.position = latLng
        }

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))
    }

    override fun onDestroyView() {
        trackingJob?.cancel()
        trackingJob = null
        courierMarker = null
        googleMap = null
        _binding = null
        super.onDestroyView()
    }
}
