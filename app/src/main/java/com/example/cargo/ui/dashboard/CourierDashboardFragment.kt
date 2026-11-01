package com.example.cargo.ui.dashboard

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cargo.databinding.FragmentCourierDashboardBinding
import com.example.cargo.service.LocationService
import com.example.cargo.util.showToast

class CourierDashboardFragment : Fragment() {

    private var _binding: FragmentCourierDashboardBinding? = null
    private val binding get() = _binding!!
    private var isTrackingActive = false

    // Konum İzin İstemi Yönetici Mekanizması
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseLocationGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

        if (fineLocationGranted || coarseLocationGranted) {
            toggleLocationService()
        } else {
            showToast("Kurye takibi için konum izni vermeniz zorunludur.")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCourierDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, view: Bundle?) {
        super.onViewCreated(view, view)

        // Şimdilik test amaçlı statik bir yük ID'si simüle ediyoruz
        val currentLoadId = "TRK-98342"
        binding.textViewActiveLoadId.text = "Aktif Yük No: $currentLoadId"

        // Canlı Konum Paylaşımı Başlatma / Durdurma Butonu
        binding.buttonToggleService.setOnClickListener {
            checkPermissionsAndToggle()
        }

        // Yük Detayına Gitme / Canlı Haritada Görme Butonu
        binding.buttonViewLoadDetails.setOnClickListener {
            val action = CourierDashboardFragmentDirections
                .actionCourierDashboardToLoadTracking(currentLoadId)
            findNavController().navigate(action)
        }
    }

    private fun checkPermissionsAndToggle() {
        val permissionsToRequest = mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        // Android 13+ ise bildirim izni de foreground service için istenir
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
        }

        val missingPermissions = permissionsToRequest.filter {
            ContextCompat.checkSelfPermission(requireContext(), it) != PackageManager.PERMISSION_GRANTED
        }

        if (missingPermissions.isEmpty()) {
            toggleLocationService()
        } else {
            requestPermissionLauncher.launch(missingPermissions.toTypedArray())
        }
    }

    private fun toggleLocationService() {
        val intent = Intent(requireContext(), LocationService::class.java)

        if (!isTrackingActive) {
            // Konum Servisini Başlat
            ContextCompat.startForegroundService(requireContext(), intent)
            isTrackingActive = true
            binding.buttonToggleService.text = "Canlı Konum Paylaşımını Durdur"
            binding.buttonToggleService.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.holo_red_dark))
            binding.textViewServiceStatus.text = "Durum: Konumunuz Canlı Paylaşılıyor"
            showToast("Arka plan kurye takip servisi başlatıldı.")
        } else {
            // Konum Servisini Durdur
            requireContext().stopService(intent)
            isTrackingActive = false
            binding.buttonToggleService.text = "Canlı Konum Paylaşımını Başlat"
            binding.buttonToggleService.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.holo_green_dark))
            binding.textViewServiceStatus.text = "Durum: Çevrimdışı"
            showToast("Kurye takip servisi durduruldu.")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
