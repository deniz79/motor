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
import com.motorlar.app.data.model.Route
import com.motorlar.app.data.model.MotorcycleType
import com.motorlar.app.data.model.RouteDifficulty

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedMotorcycleType by remember { mutableStateOf<MotorcycleType?>(null) }
    var selectedDifficulty by remember { mutableStateOf<RouteDifficulty?>(null) }
    
    // Örnek rotalar
    val sampleRoutes = remember {
        listOf(
            Route(
                id = 1,
                name = "İstanbul - Sapanca Gölü",
                description = "Şehir dışına çıkıp doğayla buluşun",
                distance = 120.5,
                duration = 180,
                difficulty = RouteDifficulty.MEDIUM,
                motorcycleType = MotorcycleType.TOURING,
                rating = 4.5f,
                reviewCount = 23,
                isPublic = true,
                creatorId = 1,
                createdAt = System.currentTimeMillis(),
                waypoints = emptyList()
            ),
            Route(
                id = 2,
                name = "Bolu - Abant Gölü",
                description = "Muhteşem manzaralı dağ yolu",
                distance = 85.2,
                duration = 120,
                difficulty = RouteDifficulty.EASY,
                motorcycleType = MotorcycleType.ADVENTURE,
                rating = 4.8f,
                reviewCount = 45,
                isPublic = true,
                creatorId = 2,
                createdAt = System.currentTimeMillis(),
                waypoints = emptyList()
            ),
            Route(
                id = 3,
                name = "İzmir - Çeşme Sahil",
                description = "Ege'nin maviliğinde keyifli sürüş",
                distance = 95.0,
                duration = 90,
                difficulty = RouteDifficulty.EASY,
                motorcycleType = MotorcycleType.CRUISER,
                rating = 4.2f,
                reviewCount = 18,
                isPublic = true,
                creatorId = 3,
                createdAt = System.currentTimeMillis(),
                waypoints = emptyList()
            )
        )
    }
    
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Üst bar
        TopAppBar(
            title = { Text("Motorlar", fontWeight = FontWeight.Bold) },
            actions = {
                IconButton(onClick = { /* Bildirimler */ }) {
                    Icon(Icons.Default.Notifications, "Bildirimler")
                }
                IconButton(onClick = { /* Ayarlar */ }) {
                    Icon(Icons.Default.Settings, "Ayarlar")
                }
            }
        )
        
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Arama çubuğu
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Rota ara...") },
                    leadingIcon = { Icon(Icons.Default.Search, "Ara") },
                    trailingIcon = { 
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, "Temizle")
                            }
                        }
                    }
                )
            }
            
            // Filtreler
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Motor tipi filtresi
                    FilterChip(
                        selected = selectedMotorcycleType != null,
                        onClick = { /* Motor tipi seçimi */ },
                        label = { Text("Motor Tipi") },
                        leadingIcon = {
                            Icon(Icons.Default.Motorcycle, "Motor")
                        }
                    )
                    
                    // Zorluk filtresi
                    FilterChip(
                        selected = selectedDifficulty != null,
                        onClick = { /* Zorluk seçimi */ },
                        label = { Text("Zorluk") },
                        leadingIcon = {
                            Icon(Icons.Default.Star, "Zorluk")
                        }
                    )
                }
            }
            
            // Yeni rota ekleme butonu
            item {
                Button(
                    onClick = { /* Yeni rota ekleme */ },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(Icons.Default.Add, "Ekle")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Yeni Rota Ekle")
                }
            }
            
            // Popüler rotalar başlığı
            item {
                Text(
                    text = "Popüler Rotalar",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            // Rota listesi
            items(sampleRoutes) { route ->
                RouteCard(route = route)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteCard(route: Route) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = { /* Rota detayına git */ }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Rota başlığı ve rating
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = route.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = "Yıldız",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${route.rating} (${route.reviewCount})",
                        fontSize = 14.sp
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Açıklama
            Text(
                text = route.description,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Detaylar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Mesafe
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Place,
                        contentDescription = "Mesafe",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("${route.distance} km")
                }
                
                // Süre
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Schedule,
                        contentDescription = "Süre",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("${route.duration} dk")
                }
                
                // Zorluk
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.TrendingUp,
                        contentDescription = "Zorluk",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(route.difficulty.name)
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Aksiyon butonları
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedButton(
                    onClick = { /* İndir */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Download, "İndir")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("İndir")
                }
                
                Spacer(modifier = Modifier.width(8.dp))
                
                OutlinedButton(
                    onClick = { /* Yorum yap */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Comment, "Yorum")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Yorum")
                }
                
                Spacer(modifier = Modifier.width(8.dp))
                
                OutlinedButton(
                    onClick = { /* Paylaş */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Share, "Paylaş")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Paylaş")
                }
            }
        }
    }
}