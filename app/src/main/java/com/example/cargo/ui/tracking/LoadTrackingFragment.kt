package com.example.cargo.ui.tracking

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cargo.R
import com.example.cargo.databinding.FragmentLoadTrackingBinding
import com.example.cargo.data.repository.CargoRepository
import com.example.cargo.network.RetrofitClient

class LoadTrackingFragment : Fragment() {
    private var _binding: FragmentLoadTrackingBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: LoadTrackingViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLoadTrackingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val repository = CargoRepository(RetrofitClient.apiService)
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return LoadTrackingViewModel(repository) as T
            }
        }).get(LoadTrackingViewModel::class.java)

        val adapter = LoadAdapter(emptyList())
        binding.recyclerViewLoadTracking.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewLoadTracking.adapter = adapter

        val loadId = arguments?.getString("loadId") ?: "123"
        viewModel.trackLoad(loadId)

        viewModel.loadStatus.observe(viewLifecycleOwner) { load ->
            if (load != null) adapter.updateData(listOf(load))
        }

        binding.buttonRefresh.setOnClickListener { viewModel.trackLoad(loadId) }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
