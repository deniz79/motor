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
import com.motorlar.app.viewmodel.MainViewModel
import com.motorlar.app.data.model.Route
import com.motorlar.app.utils.RouteUtils.calculateDistance
import com.motorlar.app.utils.RouteUtils.calculateDuration

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteDrawingScreen(
    routeName: String,
    routeDescription: String,
    viewModel: MainViewModel,
    onBack: () -> Unit,
    onRouteSaved: () -> Unit,
    modifier: Modifier = Modifier
) {
    var mapView by remember { mutableStateOf<MapView?>(null) }
    var isMapReady by remember { mutableStateOf(false) }
    var isDrawingRoute by remember { mutableStateOf(false) }
    var routePoints by remember { mutableStateOf<List<LatLng>>(emptyList()) }
    var currentLocation by remember { mutableStateOf(LatLng(41.0082, 28.9784)) } // İstanbul
    
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Üst bar
        TopAppBar(
            title = { Text("Rota Çizme", fontWeight = FontWeight.Bold) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, "Geri")
                }
            },
            actions = {
                if (isDrawingRoute) {
                                            TextButton(
                            onClick = {
                                isDrawingRoute = false
                                // Rotayı kaydet ve harita ekranına ekle
                                val newRoute = Route(
                                    id = System.currentTimeMillis(),
                                    name = routeName,
                                    description = routeDescription,
                                    creatorId = 1L,
                                    creatorName = "Siz",
                                    motorcycleType = com.motorlar.app.data.model.MotorcycleType.SPORT,
                                    startLocation = "Başlangıç",
                                    endLocation = "Bitiş",
                                    distance = calculateDistance(routePoints),
                                    duration = calculateDuration(routePoints),
                                    difficulty = com.motorlar.app.data.model.RouteDifficulty.EASY
                                )
                                // Burada rotayı harita ekranına eklenebilir
                                onRouteSaved()
                            }
                        ) {
                            Text("Kaydet", fontWeight = FontWeight.Bold)
                        }
                }
            }
        )
        
        // Bilgi paneli
        if (isDrawingRoute) {
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
                        text = "Rota Çiziliyor",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text("Rota Adı: $routeName")
                    Text("Nokta Sayısı: ${routePoints.size}")
                    if (routePoints.isNotEmpty()) {
                        Text("Mesafe: ${calculateDistance(routePoints).toInt()} km")
                    }
                    Text("Haritada tıklayarak rota çizin")
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
                        
                        // Varsayılan konuma git
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 10f))
                        
                        // Harita tıklama olayı
                        googleMap.setOnMapClickListener { latLng ->
                            if (isDrawingRoute) {
                                routePoints = routePoints + latLng
                                
                                // Yeni durak noktası marker'ı ekle
                                googleMap.addMarker(
                                    MarkerOptions()
                                        .position(latLng)
                                        .title("Durak ${routePoints.size}")
                                        .snippet("${latLng.latitude}, ${latLng.longitude}")
                                )
                                
                                // Rota çizgisini güncelle (Google Maps benzeri)
                                if (routePoints.size > 1) {
                                    // Sadece son iki nokta arasına çizgi ekle
                                    val previousPoint = routePoints[routePoints.size - 2]
                                    googleMap.addPolyline(
                                        PolylineOptions()
                                            .add(previousPoint, latLng)
                                            .width(8f)
                                            .color(0xFF2196F3.toInt())
                                    )
                                }
                            }
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
                    // Rota çizme başlat/durdur butonu
                    FloatingActionButton(
                        onClick = { 
                            if (isDrawingRoute) {
                                // Çizimi bitir
                                isDrawingRoute = false
                            } else {
                                // Çizimi başlat
                                isDrawingRoute = true
                                routePoints = emptyList()
                            }
                        },
                        modifier = Modifier.size(56.dp),
                        containerColor = if (isDrawingRoute) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                    ) {
                        Icon(
                            if (isDrawingRoute) Icons.Default.Stop else Icons.Default.Timeline,
                            if (isDrawingRoute) "Çizimi Bitir" else "Çizmeye Başla"
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
                    
                    // Temizle butonu
                    if (routePoints.isNotEmpty()) {
                        FloatingActionButton(
                            onClick = { 
                                routePoints = emptyList()
                                // Haritayı temizle ve yeniden yükle
                                mapView?.getMapAsync { map ->
                                    map.clear()
                                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 10f))
                                }
                            },
                            modifier = Modifier.size(56.dp),
                            containerColor = MaterialTheme.colorScheme.secondary
                        ) {
                            Icon(Icons.Default.Clear, "Temizle")
                        }
                    }
                }
            }
        }
        
        // Alt bilgi paneli
        if (routePoints.isNotEmpty()) {
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
                            Text("Nokta Sayısı", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("${routePoints.size}", fontWeight = FontWeight.Bold)
                        }
                        Column {
                            Text("Mesafe", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("${calculateDistance(routePoints).toInt()} km", fontWeight = FontWeight.Bold)
                        }
                        Column {
                            Text("Tahmini Süre", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("${(calculateDistance(routePoints) / 60).toInt()} dk", fontWeight = FontWeight.Bold)
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
                                isDrawingRoute = false
                                // Rotayı kaydet
                                onRouteSaved()
                            }
                        ) {
                            Icon(Icons.Default.Save, "Kaydet")
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Rotayı Kaydet")
                        }
                        
                        OutlinedButton(
                            onClick = { 
                                routePoints = emptyList()
                                // Haritayı temizle
                                mapView?.getMapAsync { map ->
                                    map.clear()
                                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 10f))
                                }
                            }
                        ) {
                            Icon(Icons.Default.Clear, "Temizle")
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Temizle")
                        }
                    }
                }
            }
        }
    }
}


