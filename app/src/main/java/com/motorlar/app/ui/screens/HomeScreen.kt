package com.motorlar.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.motorlar.app.viewmodel.MainViewModel
import com.motorlar.app.data.model.MotorcycleType
import com.motorlar.app.data.model.RouteDifficulty

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val currentUser = viewModel.uiState.collectAsState().value.currentUser
    
    var searchQuery by remember { mutableStateOf("") }
    var selectedMotorcycleType by remember { mutableStateOf<MotorcycleType?>(null) }
    var selectedDifficulty by remember { mutableStateOf<RouteDifficulty?>(null) }
    
    // Dialog states
    var showMotorcycleTypeDialog by remember { mutableStateOf(false) }
    var showDifficultyDialog by remember { mutableStateOf(false) }
    var showNewRouteDialog by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }
    var showNotificationsDialog by remember { mutableStateOf(false) }
    var showRouteDetailDialog by remember { mutableStateOf<Route?>(null) }
    var showDownloadDialog by remember { mutableStateOf(false) }
    var showCommentDialog by remember { mutableStateOf(false) }
    var showShareDialog by remember { mutableStateOf(false) }
    var selectedRouteForAction by remember { mutableStateOf<Route?>(null) }
    
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
                IconButton(onClick = { showNotificationsDialog = true }) {
                    Icon(Icons.Default.Notifications, "Bildirimler")
                }
                IconButton(onClick = { showSettingsDialog = true }) {
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
                        onClick = { showMotorcycleTypeDialog = true },
                        label = { Text(selectedMotorcycleType?.name ?: "Motor Tipi") },
                        leadingIcon = {
                            Icon(Icons.Default.Motorcycle, "Motor")
                        }
                    )
                    
                    // Zorluk filtresi
                    FilterChip(
                        selected = selectedDifficulty != null,
                        onClick = { showDifficultyDialog = true },
                        label = { Text(selectedDifficulty?.name ?: "Zorluk") },
                        leadingIcon = {
                            Icon(Icons.Default.Star, "Zorluk")
                        }
                    )
                }
            }
            
            // Yeni rota ekleme butonu
            item {
                Button(
                    onClick = { showNewRouteDialog = true },
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
                RouteCard(
                    route = route,
                    onRouteClick = { showRouteDetailDialog = route },
                    onDownload = { 
                        // İndirme işlemi
                        showDownloadDialog = true
                        selectedRouteForAction = route
                    },
                    onComment = { 
                        // Yorum işlemi
                        showCommentDialog = true
                        selectedRouteForAction = route
                    },
                    onShare = { 
                        // Paylaşım işlemi
                        showShareDialog = true
                        selectedRouteForAction = route
                    }
                )
            }
        }
    }
    
    // Motor tipi seçim dialog
    if (showMotorcycleTypeDialog) {
        AlertDialog(
            onDismissRequest = { showMotorcycleTypeDialog = false },
            title = { Text("Motor Tipi Seçin") },
            text = {
                Column {
                    MotorcycleType.values().forEach { type ->
                        TextButton(
                            onClick = {
                                selectedMotorcycleType = type
                                showMotorcycleTypeDialog = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(type.name)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showMotorcycleTypeDialog = false }) {
                    Text("İptal")
                }
            }
        )
    }
    
    // Zorluk seviyesi seçim dialog
    if (showDifficultyDialog) {
        AlertDialog(
            onDismissRequest = { showDifficultyDialog = false },
            title = { Text("Zorluk Seviyesi Seçin") },
            text = {
                Column {
                    RouteDifficulty.values().forEach { difficulty ->
                        TextButton(
                            onClick = {
                                selectedDifficulty = difficulty
                                showDifficultyDialog = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(difficulty.name)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showDifficultyDialog = false }) {
                    Text("İptal")
                }
            }
        )
    }
    
    // Yeni rota ekleme dialog
    if (showNewRouteDialog) {
        var routeName by remember { mutableStateOf("") }
        var routeDescription by remember { mutableStateOf("") }
        var selectedMotorcycleType by remember { mutableStateOf<MotorcycleType?>(null) }
        var selectedDifficulty by remember { mutableStateOf<RouteDifficulty?>(null) }
        var startLocation by remember { mutableStateOf("") }
        var endLocation by remember { mutableStateOf("") }
        
        AlertDialog(
            onDismissRequest = { showNewRouteDialog = false },
            title = { Text("Yeni Rota Ekle") },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    OutlinedTextField(
                        value = routeName,
                        onValueChange = { routeName = it },
                        label = { Text("Rota Adı") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    OutlinedTextField(
                        value = routeDescription,
                        onValueChange = { routeDescription = it },
                        label = { Text("Açıklama") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    OutlinedTextField(
                        value = startLocation,
                        onValueChange = { startLocation = it },
                        label = { Text("Başlangıç Noktası") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    OutlinedTextField(
                        value = endLocation,
                        onValueChange = { endLocation = it },
                        label = { Text("Bitiş Noktası") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text("Motor Tipi:", fontWeight = FontWeight.Bold)
                    MotorcycleType.values().forEach { type ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedMotorcycleType == type,
                                onClick = { selectedMotorcycleType = type }
                            )
                            Text(type.name)
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text("Zorluk:", fontWeight = FontWeight.Bold)
                    RouteDifficulty.values().forEach { difficulty ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
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
                        if (routeName.isNotBlank() && selectedMotorcycleType != null && selectedDifficulty != null) {
                            // Yeni rota ekleme işlemi
                            val newRoute = Route(
                                name = routeName,
                                description = routeDescription,
                                creatorId = 1,
                                creatorName = currentUser ?: "Kullanıcı",
                                motorcycleType = selectedMotorcycleType!!,
                                startLocation = startLocation,
                                endLocation = endLocation,
                                distance = 0.0,
                                duration = 0L,
                                difficulty = selectedDifficulty!!
                            )
                            // TODO: Rotayı listeye ekle
                            showNewRouteDialog = false
                        }
                    },
                    enabled = routeName.isNotBlank() && selectedMotorcycleType != null && selectedDifficulty != null
                ) {
                    Text("Ekle")
                }
            },
            dismissButton = {
                TextButton(onClick = { showNewRouteDialog = false }) {
                    Text("İptal")
                }
            }
        )
    }
    
    // Ayarlar dialog
    if (showSettingsDialog) {
        AlertDialog(
            onDismissRequest = { showSettingsDialog = false },
            title = { Text("Ayarlar") },
            text = {
                Column {
                    TextButton(
                        onClick = { /* Profil ayarları */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Profil Ayarları")
                    }
                    TextButton(
                        onClick = { /* Bildirim ayarları */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Bildirim Ayarları")
                    }
                    TextButton(
                        onClick = { /* Gizlilik ayarları */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Gizlilik Ayarları")
                    }
                    TextButton(
                        onClick = { /* Uygulama ayarları */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Uygulama Ayarları")
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showSettingsDialog = false }) {
                    Text("Kapat")
                }
            }
        )
    }
    
    // Bildirimler dialog
    if (showNotificationsDialog) {
        AlertDialog(
            onDismissRequest = { showNotificationsDialog = false },
            title = { Text("Bildirimler") },
            text = {
                Column {
                    Text("Yeni bildirim yok")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Tüm bildirimleriniz burada görünecek")
                }
            },
            confirmButton = {
                TextButton(onClick = { showNotificationsDialog = false }) {
                    Text("Tamam")
                }
            }
        )
    }
    
    // Rota detay dialog
    showRouteDetailDialog?.let { route ->
        AlertDialog(
            onDismissRequest = { showRouteDetailDialog = null },
            title = { Text(route.name) },
            text = {
                Column {
                    Text(route.description)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Mesafe: ${route.distance} km")
                    Text("Süre: ${route.duration} dk")
                    Text("Zorluk: ${route.difficulty.name}")
                    Text("Motor Tipi: ${route.motorcycleType.name}")
                    Text("Puan: ${route.rating} (${route.reviewCount} yorum)")
                }
            },
            confirmButton = {
                TextButton(onClick = { showRouteDetailDialog = null }) {
                    Text("Kapat")
                }
            }
        )
    }
    
    // İndirme dialog
    if (showDownloadDialog) {
        AlertDialog(
            onDismissRequest = { showDownloadDialog = false },
            title = { Text("Rota İndir") },
            text = {
                Column {
                    selectedRouteForAction?.let { route ->
                        Text("${route.name} rotasını indirmek istiyor musunuz?")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Mesafe: ${route.distance} km")
                        Text("Süre: ${route.duration} dk")
                        Text("Zorluk: ${route.difficulty.name}")
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        // İndirme işlemi
                        showDownloadDialog = false
                        selectedRouteForAction = null
                    }
                ) {
                    Text("İndir")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDownloadDialog = false
                        selectedRouteForAction = null
                    }
                ) {
                    Text("İptal")
                }
            }
        )
    }
    
    // Yorum dialog
    if (showCommentDialog) {
        var commentText by remember { mutableStateOf("") }
        
        AlertDialog(
            onDismissRequest = { showCommentDialog = false },
            title = { Text("Yorum Yap") },
            text = {
                Column {
                    selectedRouteForAction?.let { route ->
                        Text("${route.name} rotası için yorumunuz:")
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = commentText,
                            onValueChange = { commentText = it },
                            label = { Text("Yorumunuz") },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 3
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        // Yorum gönderme işlemi
                        showCommentDialog = false
                        selectedRouteForAction = null
                        commentText = ""
                    }
                ) {
                    Text("Gönder")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showCommentDialog = false
                        selectedRouteForAction = null
                        commentText = ""
                    }
                ) {
                    Text("İptal")
                }
            }
        )
    }
    
    // Paylaş dialog
    if (showShareDialog) {
        AlertDialog(
            onDismissRequest = { showShareDialog = false },
            title = { Text("Rota Paylaş") },
            text = {
                Column {
                    selectedRouteForAction?.let { route ->
                        Text("${route.name} rotasını paylaşmak istiyor musunuz?")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Mesafe: ${route.distance} km")
                        Text("Süre: ${route.duration} dk")
                        Text("Zorluk: ${route.difficulty.name}")
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        // Paylaşım işlemi
                        showShareDialog = false
                        selectedRouteForAction = null
                    }
                ) {
                    Text("Paylaş")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showShareDialog = false
                        selectedRouteForAction = null
                    }
                ) {
                    Text("İptal")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteCard(
    route: Route,
    onRouteClick: () -> Unit,
    onDownload: () -> Unit,
    onComment: () -> Unit,
    onShare: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = onRouteClick
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
                    onClick = onDownload,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Download, "İndir")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("İndir")
                }
                
                Spacer(modifier = Modifier.width(8.dp))
                
                OutlinedButton(
                    onClick = onComment,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Comment, "Yorum")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Yorum")
                }
                
                Spacer(modifier = Modifier.width(8.dp))
                
                OutlinedButton(
                    onClick = onShare,
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