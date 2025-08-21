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
import com.google.android.gms.maps.model.PolylineOptions
import com.motorlar.app.data.model.Route
import com.motorlar.app.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteDetailScreen(
    routeId: String,
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    var mapView by remember { mutableStateOf<MapView?>(null) }
    var isMapReady by remember { mutableStateOf(false) }
    var currentLocation by remember { mutableStateOf(LatLng(41.0082, 28.9784)) } // İstanbul
    
    // Örnek rota verisi (gerçek uygulamada routeId ile alınacak)
    val route = remember {
        Route(
            name = "İstanbul - Sapanca Gölü",
            description = "Güzel manzaralı rota, virajlı yollar",
            creatorId = 1,
            creatorName = "Ahmet",
            motorcycleType = com.motorlar.app.data.model.MotorcycleType.SPORT,
            startLocation = "İstanbul",
            endLocation = "Sapanca",
            distance = 120.0,
            duration = 7200000L,
            difficulty = com.motorlar.app.data.model.RouteDifficulty.MEDIUM,
            rating = 4.5f,
            reviewCount = 28
        )
    }
    
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Üst bar
        TopAppBar(
            title = { Text(route.name, fontWeight = FontWeight.Bold) },
            navigationIcon = {
                IconButton(onClick = { /* Geri git */ }) {
                    Icon(Icons.Default.ArrowBack, "Geri")
                }
            },
            actions = {
                IconButton(onClick = { /* Paylaş */ }) {
                    Icon(Icons.Default.Share, "Paylaş")
                }
                IconButton(onClick = { /* Favori */ }) {
                    Icon(Icons.Default.FavoriteBorder, "Favori")
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
                        
                        // Kullanıcının mevcut konumu (gerçek uygulamada GPS'ten alınacak)
                        val userLocation = currentLocation
                        
                        // Rota başlangıç ve bitiş noktaları
                        val startLatLng = when (route.startLocation) {
                            "İstanbul" -> LatLng(41.0082, 28.9784)
                            "Sapanca" -> LatLng(40.9862, 29.1244)
                            "Bursa" -> LatLng(40.1885, 29.0610)
                            "İzmit" -> LatLng(40.7392, 29.6111)
                            else -> LatLng(41.0082, 28.9784)
                        }
                        
                        val endLatLng = when (route.endLocation) {
                            "Sapanca" -> LatLng(40.9862, 29.1244)
                            "Bursa" -> LatLng(40.1885, 29.0610)
                            "İzmit" -> LatLng(40.7392, 29.6111)
                            else -> LatLng(41.0082, 28.9784)
                        }
                        
                        // Kullanıcının konumu marker'ı
                        googleMap.addMarker(
                            MarkerOptions()
                                .position(userLocation)
                                .title("Konumunuz")
                                .snippet("Başlangıç noktası")
                        )
                        
                        // Rota başlangıç marker'ı
                        googleMap.addMarker(
                            MarkerOptions()
                                .position(startLatLng)
                                .title(route.name)
                                .snippet("Başlangıç: ${route.startLocation}")
                        )
                        
                        // Rota bitiş marker'ı
                        googleMap.addMarker(
                            MarkerOptions()
                                .position(endLatLng)
                                .title(route.name)
                                .snippet("Bitiş: ${route.endLocation}")
                        )
                        
                        // Kullanıcıdan rotaya giden çizgi
                        googleMap.addPolyline(
                            PolylineOptions()
                                .add(userLocation, startLatLng)
                                .width(8f)
                                .color(0xFF4CAF50.toInt()) // Yeşil - kullanıcıdan rotaya
                        )
                        
                        // Rota çizgisi
                        googleMap.addPolyline(
                            PolylineOptions()
                                .add(startLatLng, endLatLng)
                                .width(8f)
                                .color(0xFF2196F3.toInt()) // Mavi - rota
                        )
                        
                        // Haritayı kullanıcının konumuna odakla
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 10f))
                        
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
                    // Navigasyon başlat butonu
                    FloatingActionButton(
                        onClick = { /* Navigasyon başlat */ },
                        modifier = Modifier.size(56.dp),
                        containerColor = MaterialTheme.colorScheme.primary
                    ) {
                        Icon(Icons.Default.Navigation, "Navigasyon Başlat")
                    }
                    
                    // Konum butonu
                    FloatingActionButton(
                        onClick = { 
                            mapView?.getMapAsync { map ->
                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f))
                            }
                        },
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
                    text = "Rota Bilgileri",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Mesafe", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("${route.distance} km", fontWeight = FontWeight.Bold)
                    }
                    Column {
                        Text("Süre", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("${route.duration / 60000} dk", fontWeight = FontWeight.Bold)
                    }
                    Column {
                        Text("Zorluk", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(route.difficulty.name, fontWeight = FontWeight.Bold)
                    }
                    Column {
                        Text("Motor", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(route.motorcycleType.displayName, fontWeight = FontWeight.Bold)
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Yol tarifi
                Text(
                    text = "Yol Tarifi",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text("1. Mevcut konumunuzdan ${route.startLocation} merkezine gidin")
                Text("2. D100 karayoluna girin")
                Text("3. ${route.endLocation} yönünde devam edin")
                Text("4. ${route.endLocation} merkezine ulaşın")
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Aksiyon butonları
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OutlinedButton(
                        onClick = { /* Navigasyon başlat */ }
                    ) {
                        Icon(Icons.Default.Navigation, "Navigasyon")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Navigasyon Başlat")
                    }
                    
                    OutlinedButton(
                        onClick = { /* Rota kaydet */ }
                    ) {
                        Icon(Icons.Default.BookmarkBorder, "Kaydet")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Kaydet")
                    }
                }
            }
        }
    }
}