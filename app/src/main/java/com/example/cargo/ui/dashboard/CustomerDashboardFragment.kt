package com.example.cargo.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cargo.databinding.FragmentCustomerDashboardBinding
import com.example.cargo.util.showToast

class CustomerDashboardFragment : Fragment() {

    private var _binding: FragmentCustomerDashboardBinding? = null
    private val binding get() = _binding!!

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
            val origin = binding.editTextOrigin.text.toString().trim()
            val destination = binding.editTextDestination.text.toString().trim()

            if (origin.isEmpty() || destination.isEmpty()) {
                showToast("Lütfen kalkış ve varış noktalarını doldurun.")
                return@setOnClickListener
            }

            val simulatedPrice = (150..500).random()
            binding.textViewPriceResult.text = "Tahmini Rota Fiyatı: $simulatedPrice TRY"
        }

        binding.buttonTrackLoad.setOnClickListener {
            val trackingId = binding.editTextTrackingId.text.toString().trim()

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
