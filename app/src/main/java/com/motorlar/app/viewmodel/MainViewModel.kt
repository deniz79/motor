package com.motorlar.app.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class MainUiState(
    val hasLocationPermission: Boolean = false,
    val isLocationEnabled: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)

class MainViewModel : ViewModel() {
    
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState
    
    fun onLocationPermissionGranted() {
        _uiState.value = _uiState.value.copy(
            hasLocationPermission = true,
            isLocationEnabled = true
        )
    }
    
    fun onLocationPermissionDenied() {
        _uiState.value = _uiState.value.copy(
            hasLocationPermission = false,
            isLocationEnabled = false
        )
    }
    
    fun onAppResume() {
        // Location servisleri burada başlatılacak
    }
    
    fun onAppPause() {
        // Location servisleri burada durdurulacak
    }
}
