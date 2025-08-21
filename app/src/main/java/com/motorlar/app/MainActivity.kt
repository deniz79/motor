package com.motorlar.app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.motorlar.app.ui.navigation.MotorlarNavGraph
import com.motorlar.app.ui.theme.MotorlarTheme
import com.motorlar.app.viewmodel.MainViewModel
import com.motorlar.app.data.local.AppDatabase

class MainActivity : ComponentActivity() {
    
    private lateinit var viewModel: MainViewModel
    
    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) ||
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // İzin verildi
                viewModel.onLocationPermissionGranted()
            }
            else -> {
                // İzin reddedildi
                viewModel.onLocationPermissionDenied()
            }
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Veritabanını başlat
        val database = AppDatabase.getDatabase(this)
        
        viewModel = MainViewModel()
        checkAndRequestPermissions()
        
        setContent {
            MotorlarTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    
                    MotorlarNavGraph(
                        navController = navController
                    )
                }
            }
        }
    }
    
    private fun checkAndRequestPermissions() {
        val hasLocationPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        
        if (!hasLocationPermission) {
            locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        } else {
            viewModel.onLocationPermissionGranted()
        }
    }
    
    override fun onResume() {
        super.onResume()
        viewModel.onAppResume()
    }
    
    override fun onPause() {
        super.onPause()
        viewModel.onAppPause()
    }
}
