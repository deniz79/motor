package com.motorlar.app.ui.screens

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
import com.motorlar.app.viewmodel.MainViewModel
import com.motorlar.app.data.model.Route
import com.motorlar.app.data.model.MotorcycleType
import com.motorlar.app.data.model.RouteDifficulty

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val currentUser = viewModel.uiState.collectAsState().value.currentUser
    
    var mapView by remember { mutableStateOf<MapView?>(null) }
    var isMapReady by remember { mutableStateOf(false) }
    var currentLocation by remember { mutableStateOf(LatLng(41.0082, 28.9784)) } // İstanbul
    var isDrawingRoute by remember { mutableStateOf(false) }
    var routePoints by remember { mutableStateOf<List<LatLng>>(emptyList()) }
    var currentRoute by remember { mutableStateOf<Route?>(null) }
    
    // Dialog states
    var showLocationSettingsDialog by remember { mutableStateOf(false) }
    var showMapLayersDialog by remember { mutableStateOf(false) }
    var showFiltersDialog by remember { mutableStateOf(false) }
    var showRouteSearchDialog by remember { mutableStateOf(false) }
    var showSavedRoutesDialog by remember { mutableStateOf(false) }
    var showTrafficDialog by remember { mutableStateOf(false) }
    var showRouteDrawingDialog by remember { mutableStateOf(false) }
    var showNearbyRoutesDialog by remember { mutableStateOf(false) }
    var showRouteSaveDialog by remember { mutableStateOf(false) }
    var showDirectionsDialog by remember { mutableStateOf<Route?>(null) }
    
    // Kullanıcı rotaları (örnek veri)
    val userRoutes = remember {
        listOf(
            Route(
                name = "İstanbul - Sapanca Gölü",
                description = "Güzel manzaralı rota",
                creatorId = 1,
                creatorName = "Ahmet",
                motorcycleType = MotorcycleType.SPORT,
                startLocation = "İstanbul",
                endLocation = "Sapanca",
                distance = 120.0,
                duration = 7200000L, // 2 saat
                difficulty = RouteDifficulty.MEDIUM
            ),
            Route(
                name = "İstanbul - Bursa",
                description = "Tarihi rota",
                creatorId = 2,
                creatorName = "Mehmet",
                motorcycleType = MotorcycleType.TOURING,
                startLocation = "İstanbul",
                endLocation = "Bursa",
                distance = 150.0,
                duration = 9000000L, // 2.5 saat
                difficulty = RouteDifficulty.EASY
            ),
            Route(
                name = "İstanbul - İzmit",
                description = "Sahil rotası",
                creatorId = 3,
                creatorName = "Ayşe",
                motorcycleType = MotorcycleType.CRUISER,
                startLocation = "İstanbul",
                endLocation = "İzmit",
                distance = 80.0,
                duration = 5400000L, // 1.5 saat
                difficulty = RouteDifficulty.EASY
            )
        )
    }
    
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Üst bar
        TopAppBar(
            title = { Text("Harita", fontWeight = FontWeight.Bold) },
            actions = {
                IconButton(onClick = { showLocationSettingsDialog = true }) {
                    Icon(Icons.Default.LocationOn, "Konum")
                }
                IconButton(onClick = { showMapLayersDialog = true }) {
                    Icon(Icons.Default.Layers, "Katmanlar")
                }
                IconButton(onClick = { showFiltersDialog = true }) {
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
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 10f))
                        
                        // Kullanıcı rotalarını haritaya ekle
                        userRoutes.forEach { route ->
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
                            
                            // Başlangıç marker'ı
                            googleMap.addMarker(
                                MarkerOptions()
                                    .position(startLatLng)
                                    .title(route.name)
                                    .snippet("Başlangıç: ${route.startLocation}")
                            )
                            
                            // Bitiş marker'ı
                            googleMap.addMarker(
                                MarkerOptions()
                                    .position(endLatLng)
                                    .title(route.name)
                                    .snippet("Bitiş: ${route.endLocation}")
                            )
                            
                            // Rota çizgisi
                            googleMap.addPolyline(
                                PolylineOptions()
                                    .add(startLatLng, endLatLng)
                                    .width(8f)
                                    .color(0xFF2196F3.toInt())
                            )
                        }
                        
                        // Harita tıklama olayı
                        googleMap.setOnMapClickListener { latLng ->
                            if (isDrawingRoute) {
                                routePoints = routePoints + latLng
                                currentRoute = currentRoute?.copy(
                                    distance = calculateDistance(routePoints),
                                    duration = calculateDuration(routePoints)
                                )
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
                    // Rota çizme butonu
                    FloatingActionButton(
                        onClick = { 
                            if (isDrawingRoute) {
                                // Çizimi bitir
                                isDrawingRoute = false
                                showRouteSaveDialog = true
                            } else {
                                // Çizimi başlat
                                showRouteDrawingDialog = true
                            }
                        },
                        modifier = Modifier.size(56.dp),
                        containerColor = if (isDrawingRoute) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                    ) {
                        Icon(
                            if (isDrawingRoute) Icons.Default.Stop else Icons.Default.Timeline,
                            if (isDrawingRoute) "Çizimi Bitir" else "Rota Çiz"
                        )
                    }
                    
                    // Yakın rotalar butonu
                    FloatingActionButton(
                        onClick = { showNearbyRoutesDialog = true },
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(Icons.Default.NearMe, "Yakın Rotalar")
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
                        onClick = { showRouteSearchDialog = true }
                    ) {
                        Icon(Icons.Default.Search, "Ara")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Rota Ara")
                    }
                    
                    // Kayıtlı rotalar
                    OutlinedButton(
                        onClick = { showSavedRoutesDialog = true }
                    ) {
                        Icon(Icons.Default.Bookmark, "Kayıtlı")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Kayıtlılar")
                    }
                    
                    // Trafik
                    OutlinedButton(
                        onClick = { showTrafficDialog = true }
                    ) {
                        Icon(Icons.Default.Traffic, "Trafik")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Trafik")
                    }
                }
            }
        }
    }
    
    // Konum ayarları dialog
    if (showLocationSettingsDialog) {
        AlertDialog(
            onDismissRequest = { showLocationSettingsDialog = false },
            title = { Text("Konum Ayarları") },
            text = {
                Column {
                    Text("Konum servisleri aktif")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("GPS: Açık")
                    Text("Ağ konumu: Açık")
                    Text("Doğruluk: Yüksek")
                }
            },
            confirmButton = {
                TextButton(onClick = { showLocationSettingsDialog = false }) {
                    Text("Tamam")
                }
            }
        )
    }
    
    // Harita katmanları dialog
    if (showMapLayersDialog) {
        AlertDialog(
            onDismissRequest = { showMapLayersDialog = false },
            title = { Text("Harita Katmanları") },
            text = {
                Column {
                    TextButton(
                        onClick = { /* Uydu görünümü */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Uydu Görünümü")
                    }
                    TextButton(
                        onClick = { /* Arazi görünümü */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Arazi Görünümü")
                    }
                    TextButton(
                        onClick = { /* Trafik görünümü */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Trafik Görünümü")
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showMapLayersDialog = false }) {
                    Text("Kapat")
                }
            }
        )
    }
    
    // Filtreler dialog
    if (showFiltersDialog) {
        AlertDialog(
            onDismissRequest = { showFiltersDialog = false },
            title = { Text("Filtreler") },
            text = {
                Column {
                    Text("Motor Tipi:")
                    MotorcycleType.values().forEach { type ->
                        TextButton(
                            onClick = { /* Filtre uygula */ },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(type.name)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Zorluk:")
                    RouteDifficulty.values().forEach { difficulty ->
                        TextButton(
                            onClick = { /* Filtre uygula */ },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(difficulty.name)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showFiltersDialog = false }) {
                    Text("Uygula")
                }
            }
        )
    }
    
    // Rota arama dialog
    if (showRouteSearchDialog) {
        var searchQuery by remember { mutableStateOf("") }
        var searchResults by remember { mutableStateOf<List<String>>(emptyList()) }
        
        AlertDialog(
            onDismissRequest = { showRouteSearchDialog = false },
            title = { Text("Rota Ara") },
            text = {
                Column {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = {
                            searchQuery = it
                            if (it.isNotBlank()) {
                                searchResults = listOf(
                                    "İstanbul - Bursa", "İstanbul - Ankara", "İstanbul - İzmir",
                                    "Bursa - İzmir", "Ankara - İzmir", "Sapanca - İstanbul"
                                ).filter { location -> location.contains(it, ignoreCase = true) }
                            } else {
                                searchResults = emptyList()
                            }
                        },
                        label = { Text("Nereye gitmek istiyorsunuz?") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    if (searchResults.isNotEmpty()) {
                        Text("Öneriler:", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        searchResults.forEach { result ->
                            TextButton(
                                onClick = { 
                                    searchQuery = result
                                    showRouteSearchDialog = false
                                    // Yol tarifi göster
                                    val route = userRoutes.find { it.name.contains(result) }
                                    if (route != null) {
                                        showDirectionsDialog = route
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(result)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { 
                        if (searchQuery.isNotBlank()) {
                            showRouteSearchDialog = false
                            // Yol tarifi göster
                        }
                    }
                ) {
                    Text("Yol Tarifi Al")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRouteSearchDialog = false }) {
                    Text("İptal")
                }
            }
        )
    }
    
    // Kayıtlı rotalar dialog
    if (showSavedRoutesDialog) {
        AlertDialog(
            onDismissRequest = { showSavedRoutesDialog = false },
            title = { Text("Kayıtlı Rotalar") },
            text = {
                LazyColumn {
                    items(userRoutes) { route ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = route.name,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = route.description,
                                    fontSize = 14.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("${route.distance} km")
                                    Text("${route.motorcycleType.name}")
                                    Text("${route.difficulty.name}")
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    OutlinedButton(
                                        onClick = { 
                                            showSavedRoutesDialog = false
                                            showDirectionsDialog = route
                                        }
                                    ) {
                                        Text("Git")
                                    }
                                    OutlinedButton(
                                        onClick = { /* Paylaş */ }
                                    ) {
                                        Text("Paylaş")
                                    }
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showSavedRoutesDialog = false }) {
                    Text("Kapat")
                }
            }
        )
    }
    
    // Trafik dialog
    if (showTrafficDialog) {
        AlertDialog(
            onDismissRequest = { showTrafficDialog = false },
            title = { Text("Trafik Durumu") },
            text = {
                Column {
                    Text("İstanbul: Yoğun")
                    Text("Bursa: Normal")
                    Text("İzmit: Hafif")
                    Text("Sapanca: Normal")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Genel durum: Orta yoğunlukta")
                }
            },
            confirmButton = {
                TextButton(onClick = { showTrafficDialog = false }) {
                    Text("Tamam")
                }
            }
        )
    }
    
    // Yakın rotalar dialog
    if (showNearbyRoutesDialog) {
        AlertDialog(
            onDismissRequest = { showNearbyRoutesDialog = false },
            title = { Text("Yakın Rotalar") },
            text = {
                LazyColumn {
                    items(userRoutes.take(3)) { route ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = route.name,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${route.distance} km uzaklıkta",
                                    fontSize = 14.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedButton(
                                    onClick = { 
                                        showNearbyRoutesDialog = false
                                        showDirectionsDialog = route
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Git")
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showNearbyRoutesDialog = false }) {
                    Text("Kapat")
                }
            }
        )
    }
    
    // Rota çizme başlatma dialog
    if (showRouteDrawingDialog) {
        var routeName by remember { mutableStateOf("") }
        var routeDescription by remember { mutableStateOf("") }
        
        AlertDialog(
            onDismissRequest = { showRouteDrawingDialog = false },
            title = { Text("Rota Çizme") },
            text = {
                Column {
                    OutlinedTextField(
                        value = routeName,
                        onValueChange = { routeName = it },
                        label = { Text("Rota Adı") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = routeDescription,
                        onValueChange = { routeDescription = it },
                        label = { Text("Açıklama") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Haritada tıklayarak rota çizebilirsiniz:")
                    Text("• Başlangıç noktasına tıklayın")
                    Text("• Ara noktalara tıklayın")
                    Text("• Bitiş noktasına tıklayın")
                    Text("• Çizimi bitirmek için butona tekrar basın")
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (routeName.isNotBlank()) {
                            currentRoute = Route(
                                name = routeName,
                                description = routeDescription,
                                creatorId = 1,
                                creatorName = currentUser ?: "Kullanıcı",
                                motorcycleType = MotorcycleType.SPORT,
                                startLocation = "Başlangıç",
                                endLocation = "Bitiş",
                                distance = 0.0,
                                duration = 0L,
                                difficulty = RouteDifficulty.EASY
                            )
                            routePoints = emptyList()
                            isDrawingRoute = true
                            showRouteDrawingDialog = false
                        }
                    },
                    enabled = routeName.isNotBlank()
                ) {
                    Text("Çizmeye Başla")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRouteDrawingDialog = false }) {
                    Text("İptal")
                }
            }
        )
    }
    
    // Rota kaydetme dialog
    if (showRouteSaveDialog) {
        var selectedMotorcycleType by remember { mutableStateOf<MotorcycleType?>(null) }
        var selectedDifficulty by remember { mutableStateOf<RouteDifficulty?>(null) }
        
        AlertDialog(
            onDismissRequest = { showRouteSaveDialog = false },
            title = { Text("Rotayı Kaydet") },
            text = {
                Column {
                    Text("Rota adı: ${currentRoute?.name}")
                    Text("Mesafe: ${currentRoute?.distance?.let { "%.1f km".format(it) }}")
                    Text("Süre: ${currentRoute?.duration?.let { "${it / 60000} dakika" }}")
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Motor Tipi:")
                    MotorcycleType.values().forEach { type ->
                        Row {
                            RadioButton(
                                selected = selectedMotorcycleType == type,
                                onClick = { selectedMotorcycleType = type }
                            )
                            Text(type.name)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Zorluk:")
                    RouteDifficulty.values().forEach { difficulty ->
                        Row {
                            RadioButton(
                                selected = selectedDifficulty == difficulty,
                                onClick = { selectedDifficulty = difficulty }
                            )
                            Text(difficulty.name)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (selectedMotorcycleType != null && selectedDifficulty != null) {
                            // Rotayı kaydet ve ana sayfaya ekle
                            showRouteSaveDialog = false
                            isDrawingRoute = false
                            routePoints = emptyList()
                            currentRoute = null
                        }
                    },
                    enabled = selectedMotorcycleType != null && selectedDifficulty != null
                ) {
                    Text("Kaydet")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRouteSaveDialog = false }) {
                    Text("İptal")
                }
            }
        )
    }
    
    // Yol tarifi dialog
    showDirectionsDialog?.let { route ->
        AlertDialog(
            onDismissRequest = { showDirectionsDialog = null },
            title = { Text("Yol Tarifi: ${route.name}") },
            text = {
                Column {
                    Text("Başlangıç: ${route.startLocation}")
                    Text("Bitiş: ${route.endLocation}")
                    Text("Mesafe: ${route.distance} km")
                    Text("Tahmini süre: ${route.duration / 60000} dakika")
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Yol tarifi:")
                    Text("1. ${route.startLocation} merkezinden çıkın")
                    Text("2. D100 karayoluna girin")
                    Text("3. ${route.endLocation} yönünde devam edin")
                    Text("4. ${route.endLocation} merkezine ulaşın")
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDirectionsDialog = null
                        // Haritada rotayı göster
                        mapView?.getMapAsync { map ->
                            val startLatLng = when (route.startLocation) {
                                "İstanbul" -> LatLng(41.0082, 28.9784)
                                "Sapanca" -> LatLng(40.9862, 29.1244)
                                "Bursa" -> LatLng(40.1885, 29.0610)
                                "İzmit" -> LatLng(40.7392, 29.6111)
                                else -> LatLng(41.0082, 28.9784)
                            }
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(startLatLng, 12f))
                        }
                    }
                ) {
                    Text("Haritada Göster")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDirectionsDialog = null }) {
                    Text("Kapat")
                }
            }
        )
    }
}

// Yardımcı fonksiyonlar
private fun calculateDistance(points: List<LatLng>): Double {
    if (points.size < 2) return 0.0
    var totalDistance = 0.0
    for (i in 0 until points.size - 1) {
        totalDistance += calculateDistanceBetweenPoints(points[i], points[i + 1])
    }
    return totalDistance
}

private fun calculateDistanceBetweenPoints(point1: LatLng, point2: LatLng): Double {
    val R = 6371.0 // Dünya'nın yarıçapı (km)
    val lat1 = Math.toRadians(point1.latitude)
    val lat2 = Math.toRadians(point2.latitude)
    val deltaLat = Math.toRadians(point2.latitude - point1.latitude)
    val deltaLng = Math.toRadians(point2.longitude - point1.longitude)
    
    val a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
            Math.cos(lat1) * Math.cos(lat2) *
            Math.sin(deltaLng / 2) * Math.sin(deltaLng / 2)
    val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
    
    return R * c
}

private fun calculateDuration(points: List<LatLng>): Long {
    val distance = calculateDistance(points)
    val averageSpeed = 60.0 // km/h
    return ((distance / averageSpeed) * 3600000).toLong() // milisaniye
}