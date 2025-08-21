package com.motorlar.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    modifier: Modifier = Modifier
) {
    var mapView by remember { mutableStateOf<MapView?>(null) }
    var isMapReady by remember { mutableStateOf(false) }
    
    // İstanbul koordinatları (varsayılan)
    val istanbul = LatLng(41.0082, 28.9784)
    
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Üst bar
        TopAppBar(
            title = { Text("Harita", fontWeight = FontWeight.Bold) },
            actions = {
                IconButton(onClick = { /* Konum ayarları */ }) {
                    Icon(Icons.Default.LocationOn, "Konum")
                }
                IconButton(onClick = { /* Harita ayarları */ }) {
                    Icon(Icons.Default.Layers, "Katmanlar")
                }
                IconButton(onClick = { /* Filtreler */ }) {
                    Icon(Icons.Default.FilterList, "Filtreler")
                }
            }
        )
        
        // Harita
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            AndroidView(
                factory = { context ->
                    MapView(context).apply {
                        onCreate(null)
                        mapView = this
                    }
                },
                modifier = Modifier.fillMaxSize(),
                update = { mapView ->
                    mapView.getMapAsync { googleMap ->
                        // Harita ayarları
                        googleMap.uiSettings.apply {
                            isZoomControlsEnabled = true
                            isMyLocationButtonEnabled = true
                            isCompassEnabled = true
                        }
                        
                        // Varsayılan konuma git
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(istanbul, 10f))
                        
                        // Örnek rotalar ekle
                        val sampleRoutes = listOf(
                            LatLng(41.0082, 28.9784) to "İstanbul",
                            LatLng(40.9862, 29.1244) to "Sapanca Gölü",
                            LatLng(40.7392, 29.6111) to "İzmit"
                        )
                        
                        sampleRoutes.forEach { (latLng, title) ->
                            googleMap.addMarker(
                                MarkerOptions()
                                    .position(latLng)
                                    .title(title)
                                    .snippet("Motor rotası")
                            )
                        }
                        
                        isMapReady = true
                    }
                }
            )
            
            // Harita üzerinde floating action button
            if (isMapReady) {
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Rota çizme butonu
                    FloatingActionButton(
                        onClick = { /* Rota çizmeye başla */ },
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(Icons.Default.Timeline, "Rota Çiz")
                    }
                    
                    // Yakın rotalar butonu
                    FloatingActionButton(
                        onClick = { /* Yakın rotaları göster */ },
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(Icons.Default.NearMe, "Yakın Rotalar")
                    }
                    
                    // Konum butonu
                    FloatingActionButton(
                        onClick = { /* Mevcut konuma git */ },
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(Icons.Default.MyLocation, "Konumum")
                    }
                }
            }
        }
        
        // Alt bilgi paneli
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Harita Kontrolleri",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Rota arama
                    OutlinedButton(
                        onClick = { /* Rota ara */ }
                    ) {
                        Icon(Icons.Default.Search, "Ara")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Rota Ara")
                    }
                    
                    // Kayıtlı rotalar
                    OutlinedButton(
                        onClick = { /* Kayıtlı rotalar */ }
                    ) {
                        Icon(Icons.Default.Bookmark, "Kayıtlı")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Kayıtlılar")
                    }
                    
                    // Trafik
                    OutlinedButton(
                        onClick = { /* Trafik durumu */ }
                    ) {
                        Icon(Icons.Default.Traffic, "Trafik")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Trafik")
                    }
                }
            }
        }
    }
}