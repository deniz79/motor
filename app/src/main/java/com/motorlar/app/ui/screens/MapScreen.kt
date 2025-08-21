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
    
    // Dialog states
    var showLocationSettingsDialog by remember { mutableStateOf(false) }
    var showMapLayersDialog by remember { mutableStateOf(false) }
    var showFiltersDialog by remember { mutableStateOf(false) }
    var showRouteSearchDialog by remember { mutableStateOf(false) }
    var showSavedRoutesDialog by remember { mutableStateOf(false) }
    var showTrafficDialog by remember { mutableStateOf(false) }
    var showRouteDrawingDialog by remember { mutableStateOf(false) }
    var showNearbyRoutesDialog by remember { mutableStateOf(false) }
    
    // İstanbul koordinatları (varsayılan)
    val istanbul = LatLng(41.0082, 28.9784)
    
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
                        onClick = { showRouteDrawingDialog = true },
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(Icons.Default.Timeline, "Rota Çiz")
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
                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(istanbul, 15f))
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
                    TextButton(
                        onClick = { /* Konum izni ver */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Konum İzni Ver")
                    }
                    TextButton(
                        onClick = { /* Konum ayarlarını aç */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Konum Ayarlarını Aç")
                    }
                    TextButton(
                        onClick = { /* GPS ayarları */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("GPS Ayarları")
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showLocationSettingsDialog = false }) {
                    Text("Kapat")
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
                        onClick = { /* Terrain görünümü */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Arazi Görünümü")
                    }
                    TextButton(
                        onClick = { /* Normal görünüm */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Normal Görünüm")
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
            title = { Text("Harita Filtreleri") },
            text = {
                Column {
                    TextButton(
                        onClick = { /* Motor tipi filtresi */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Motor Tipi")
                    }
                    TextButton(
                        onClick = { /* Zorluk filtresi */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Zorluk Seviyesi")
                    }
                    TextButton(
                        onClick = { /* Mesafe filtresi */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Mesafe")
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showFiltersDialog = false }) {
                    Text("Kapat")
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
                            // Arama sonuçlarını simüle et
                            if (it.isNotBlank()) {
                                searchResults = listOf(
                                    "İstanbul - Bursa",
                                    "İstanbul - Ankara",
                                    "İstanbul - İzmir",
                                    "Bursa - İzmir",
                                    "Ankara - İzmir"
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
                                    // Rota çizme işlemi
                                    searchQuery = result
                                    showRouteSearchDialog = false
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
                            // Rota çizme işlemi
                            showRouteSearchDialog = false
                        }
                    }
                ) {
                    Text("Rota Çiz")
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
                Column {
                    Text("İstanbul - Sapanca Gölü")
                    Text("Bolu - Abant Gölü")
                    Text("İzmir - Çeşme Sahil")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Toplam: 3 kayıtlı rota")
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
                    Text("Mevcut konum: Yoğun trafik")
                    Text("İstanbul merkez: Orta yoğunluk")
                    Text("Sapanca yolu: Açık")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Son güncelleme: 5 dk önce")
                }
            },
            confirmButton = {
                TextButton(onClick = { showTrafficDialog = false }) {
                    Text("Tamam")
                }
            }
        )
    }
    
    // Rota çizme dialog
    if (showRouteDrawingDialog) {
        AlertDialog(
            onDismissRequest = { showRouteDrawingDialog = false },
            title = { Text("Rota Çizme") },
            text = {
                Column {
                    Text("Haritada tıklayarak rota çizebilirsiniz")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("• Başlangıç noktasına tıklayın")
                    Text("• Ara noktalara tıklayın")
                    Text("• Bitiş noktasına çift tıklayın")
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { 
                        // Rota çizme modunu başlat
                        showRouteDrawingDialog = false 
                    }
                ) {
                    Text("Başlat")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRouteDrawingDialog = false }) {
                    Text("İptal")
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
                Column {
                    Text("5 km içinde:")
                    Text("• İstanbul - Sapanca (2.3 km)")
                    Text("• Bolu - Abant (4.1 km)")
                    Text("• İzmir - Çeşme (4.8 km)")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Toplam: 3 rota bulundu")
                }
            },
            confirmButton = {
                TextButton(onClick = { showNearbyRoutesDialog = false }) {
                    Text("Göster")
                }
            },
            dismissButton = {
                TextButton(onClick = { showNearbyRoutesDialog = false }) {
                    Text("İptal")
                }
            }
        )
    }
}