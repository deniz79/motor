package com.motorlar.app.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class MainUiState(
    val isLoggedIn: Boolean = false,
    val currentUser: String? = null,
    val showLocationPermissionDialog: Boolean = false
)

class MainViewModel : ViewModel() {
    
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()
    
    fun loginUser(username: String) {
        _uiState.value = _uiState.value.copy(
            isLoggedIn = true,
            currentUser = username
        )
    }
    
    fun logoutUser() {
        _uiState.value = _uiState.value.copy(
            isLoggedIn = false,
            currentUser = null
        )
    }
    
    fun onLocationPermissionGranted() {
        // İzin verildi
    }
    
    fun onLocationPermissionDenied() {
        _uiState.value = _uiState.value.copy(
            showLocationPermissionDialog = true
        )
    }
    
    fun onAppResume() {
        // Uygulama ön plana geldi
    }
    
    fun onAppPause() {
        // Uygulama arka plana gitti
    }
}
