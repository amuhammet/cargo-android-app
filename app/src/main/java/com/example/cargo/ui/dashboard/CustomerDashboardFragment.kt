package com.example.cargo.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.cargo.data.local.AppDatabase
import com.example.cargo.data.repository.CargoRepository
import com.example.cargo.databinding.FragmentCustomerDashboardBinding
import com.example.cargo.network.RetrofitClient
import com.example.cargo.util.showToast
import kotlinx.coroutines.launch

class CustomerDashboardFragment : Fragment() {

    private var _binding: FragmentCustomerDashboardBinding? = null
    private val binding get() = _binding!!

    private val repository by lazy {
        val database = AppDatabase.getDatabase(requireContext().applicationContext)
        CargoRepository(RetrofitClient.apiService, database.cargoDao())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCustomerDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonCalculatePrice.setOnClickListener {
            val origin = binding.editTextOrigin.text?.toString()?.trim().orEmpty()
            val destination = binding.editTextDestination.text?.toString()?.trim().orEmpty()

            if (origin.isEmpty() || destination.isEmpty()) {
                showToast("Lütfen kalkış ve varış noktalarını doldurun.")
                return@setOnClickListener
            }

            binding.textViewPriceResult.text = "Fiyat hesaplanıyor..."
            viewLifecycleOwner.lifecycleScope.launch {
                val result = repository.getRoutePrice(origin, destination)
                result.onSuccess { routePrice ->
                    binding.textViewPriceResult.text = 
                        "Tahmini Rota Fiyatı: ${routePrice.price} ${routePrice.currency}"
                }.onFailure { error ->
                    showToast(error.message ?: "Fiyat bilgisi alınamadı.")
                    binding.textViewPriceResult.text = "Fiyat bilgisi alınamadı."
                }
            }
        }

        binding.buttonTrackLoad.setOnClickListener {
            val trackingId = binding.editTextTrackingId.text?.toString()?.trim().orEmpty()

            if (trackingId.isEmpty()) {
                showToast("Lütfen geçerli bir takip numarası girin.")
                return@setOnClickListener
            }

            val action = CustomerDashboardFragmentDirections
                .actionCustomerDashboardToLoadTracking(trackingId)
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
