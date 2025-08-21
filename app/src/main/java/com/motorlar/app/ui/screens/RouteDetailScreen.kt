package com.motorlar.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
    var currentLocation by remember { mutableStateOf(LatLng(41.0082, 28.9784)) } // Varsayılan İstanbul
    
    // Gerçek konum alma (gerçek uygulamada GPS'ten alınacak)
    LaunchedEffect(Unit) {
        // Burada gerçek GPS konumu alınacak
        // Şimdilik İstanbul'da kalıyor
    }
    
    // Navigasyon durumu
    var isNavigationActive by remember { mutableStateOf(false) }
    var currentStep by remember { mutableStateOf(0) }
    var showNavigationDialog by remember { mutableStateOf(false) }
    
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
    
    // Detaylı yol tarifi adımları
    val navigationSteps = remember {
        listOf(
            NavigationStep(
                step = 1,
                instruction = "İstanbul merkezinden çıkın",
                distance = "0 km",
                duration = "0 dk",
                turnDirection = "Başlangıç"
            ),
            NavigationStep(
                step = 2,
                instruction = "D100 karayoluna girin",
                distance = "5 km",
                duration = "10 dk",
                turnDirection = "Sağa dön"
            ),
            NavigationStep(
                step = 3,
                instruction = "Gebze yönünde devam edin",
                distance = "25 km",
                duration = "30 dk",
                turnDirection = "Düz devam"
            ),
            NavigationStep(
                step = 4,
                instruction = "İzmit yönünde devam edin",
                distance = "45 km",
                duration = "45 dk",
                turnDirection = "Sola dön"
            ),
            NavigationStep(
                step = 5,
                instruction = "Sapanca yönünde devam edin",
                distance = "80 km",
                duration = "60 dk",
                turnDirection = "Sağa dön"
            ),
            NavigationStep(
                step = 6,
                instruction = "Sapanca Gölü'ne ulaşın",
                distance = "120 km",
                duration = "120 dk",
                turnDirection = "Varış"
            )
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
        
        // Navigasyon aktifse üst bilgi paneli
        if (isNavigationActive) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Navigasyon Aktif",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = navigationSteps[currentStep].instruction,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Mesafe: ${navigationSteps[currentStep].distance}")
                        Text("Süre: ${navigationSteps[currentStep].duration}")
                        Text("Adım: ${currentStep + 1}/${navigationSteps.size}")
                    }
                }
            }
        }
        
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
                    // Navigasyon başlat/durdur butonu
                    FloatingActionButton(
                        onClick = { 
                            if (isNavigationActive) {
                                isNavigationActive = false
                                currentStep = 0
                            } else {
                                isNavigationActive = true
                                showNavigationDialog = true
                            }
                        },
                        modifier = Modifier.size(56.dp),
                        containerColor = if (isNavigationActive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                    ) {
                        Icon(
                            if (isNavigationActive) Icons.Default.Stop else Icons.Default.Navigation,
                            if (isNavigationActive) "Navigasyonu Durdur" else "Navigasyon Başlat"
                        )
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
                
                // Aksiyon butonları
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OutlinedButton(
                        onClick = { 
                            isNavigationActive = true
                            showNavigationDialog = true
                        }
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
    
    // Navigasyon dialog
    if (showNavigationDialog) {
        AlertDialog(
            onDismissRequest = { showNavigationDialog = false },
            title = { Text("Navigasyon Başlatılıyor") },
            text = {
                LazyColumn {
                    items(navigationSteps) { step ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(MaterialTheme.colorScheme.primary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${step.step}",
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text(
                                        text = step.instruction,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "${step.distance} • ${step.duration}",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = step.turnDirection,
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showNavigationDialog = false
                        isNavigationActive = true
                        currentStep = 0
                    }
                ) {
                    Text("Navigasyonu Başlat")
                }
            },
            dismissButton = {
                TextButton(onClick = { showNavigationDialog = false }) {
                    Text("İptal")
                }
            }
        )
    }
}

data class NavigationStep(
    val step: Int,
    val instruction: String,
    val distance: String,
    val duration: String,
    val turnDirection: String
)