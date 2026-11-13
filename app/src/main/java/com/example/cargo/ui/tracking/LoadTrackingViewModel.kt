package com.example.cargo.ui.tracking

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cargo.data.model.Load
import com.example.cargo.data.repository.CargoRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

class LoadTrackingViewModel(private val repository: CargoRepository) : ViewModel() {
    private val _loadStatus = MutableLiveData<Load?>()
    val loadStatus: LiveData<Load?> = _loadStatus

    fun trackLoad(loadId: String) {
        viewModelScope.launch {
            try {
                _loadStatus.value = repository.getLoadTracking(loadId).getOrNull()
            } catch (e: CancellationException) {
                throw e
            } catch (_: Exception) {
                _loadStatus.value = null
            }
        }
    }
}
