package com.example.cargo.ui.tracking

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cargo.data.model.Load
import com.example.cargo.data.repository.CargoRepository
import kotlinx.coroutines.launch

class LoadTrackingViewModel(
    private val repository: CargoRepository
) : ViewModel() {

    private val _loadStatus = MutableLiveData<Load?>()
    val loadStatus: LiveData<Load?> = _loadStatus

    fun trackLoad(loadId: String) {
        viewModelScope.launch {
            try {
                val result = repository.getLoadTracking(loadId)
                _loadStatus.value = result.getOrNull()
            } catch (e: Exception) {
                _loadStatus.value = null
            }
        }
    }
}
