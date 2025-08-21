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
    
    init {
        // Uygulama başladığında kaydedilmiş kullanıcı bilgilerini kontrol et
        checkSavedUser()
    }

    private fun checkSavedUser() {
        // Burada SharedPreferences'dan kaydedilmiş kullanıcı bilgileri alınacak
        // Şimdilik varsayılan olarak giriş yapılmamış durumda
    }
    
    fun loginUser(username: String) {
        _uiState.value = _uiState.value.copy(
            isLoggedIn = true,
            currentUser = username
        )
        // Kullanıcı bilgilerini kaydet
        saveUserCredentials(username)
    }
    
    fun logoutUser() {
        _uiState.value = _uiState.value.copy(
            isLoggedIn = false,
            currentUser = null
        )
        // Kullanıcı bilgilerini temizle
        clearUserCredentials()
    }

    private fun saveUserCredentials(username: String) {
        // SharedPreferences'a kullanıcı bilgilerini kaydet
        // Bu gerçek uygulamada implement edilecek
    }

    private fun clearUserCredentials() {
        // SharedPreferences'dan kullanıcı bilgilerini temizle
        // Bu gerçek uygulamada implement edilecek
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
