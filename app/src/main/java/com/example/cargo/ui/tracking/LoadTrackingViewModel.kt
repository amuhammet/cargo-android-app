package com.example.cargo.ui.tracking

import androidx.lifecycle.*
import com.example.cargo.data.model.Load
import com.example.cargo.data.repository.CargoRepository
import kotlinx.coroutines.launch

class LoadTrackingViewModel(private val repository: CargoRepository) : ViewModel() {
    private val _loadStatus = MutableLiveData<Load?>()
    val loadStatus: LiveData<Load?> = _loadStatus

    fun trackLoad(loadId: String) {
        viewModelScope.launch {
            try {
                _loadStatus.value = repository.trackLoad(loadId)
            } catch (e: Exception) { _loadStatus.value = null }
        }
    }
}
