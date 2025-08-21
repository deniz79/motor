package com.motorlar.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.motorlar.app.viewmodel.MainViewModel
import com.motorlar.app.data.model.Route
import com.motorlar.app.data.model.MotorcycleType
import com.motorlar.app.data.model.RouteDifficulty

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onNavigateToRouteDetail: (String) -> Unit = {},
    onNavigateToPostCreate: () -> Unit = {},
    onNavigateToRouteDrawing: (String, String) -> Unit = { _, _ -> },
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
    var showRouteDrawingDialog by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }
    var showNotificationsDialog by remember { mutableStateOf(false) }
    var showRouteDetailDialog by remember { mutableStateOf<Route?>(null) }
    var showDownloadDialog by remember { mutableStateOf(false) }
    var showCommentDialog by remember { mutableStateOf(false) }
    var showShareDialog by remember { mutableStateOf(false) }
    var showReelsDialog by remember { mutableStateOf(false) }
    var showCreatePostDialog by remember { mutableStateOf(false) }
    var selectedRouteForAction by remember { mutableStateOf<Route?>(null) }
    
               // Örnek rotalar (gerçek verilerle değiştirilecek)
           val sampleRoutes = remember {
               listOf(
                   Route(
                       id = 1,
                       name = "İstanbul - Sapanca Gölü",
                       description = "Güzel manzaralı rota, virajlı yollar",
                       creatorId = 1,
                       creatorName = "Ahmet",
                       motorcycleType = MotorcycleType.SPORT,
                       startLocation = "İstanbul",
                       endLocation = "Sapanca",
                       distance = 120.0,
                       duration = 7200000L,
                       difficulty = RouteDifficulty.MEDIUM,
                       rating = 4.5f,
                       reviewCount = 28
                   ),
                   Route(
                       id = 2,
                       name = "İstanbul - Bursa",
                       description = "Tarihi rota, düz yollar",
                       creatorId = 2,
                       creatorName = "Mehmet",
                       motorcycleType = MotorcycleType.TOURING,
                       startLocation = "İstanbul",
                       endLocation = "Bursa",
                       distance = 150.0,
                       duration = 9000000L,
                       difficulty = RouteDifficulty.EASY,
                       rating = 4.2f,
                       reviewCount = 15
                   ),
                   Route(
                       id = 3,
                       name = "İstanbul - İzmit",
                       description = "Sahil rotası, manzaralı",
                       creatorId = 3,
                       creatorName = "Ayşe",
                       motorcycleType = MotorcycleType.CRUISER,
                       startLocation = "İstanbul",
                       endLocation = "İzmit",
                       distance = 80.0,
                       duration = 5400000L,
                       difficulty = RouteDifficulty.EASY,
                       rating = 4.8f,
                       reviewCount = 32
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
                    label = { Text("Rota ara...") },
                    leadingIcon = { Icon(Icons.Default.Search, "Ara") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
            
            // Filtreler
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = selectedMotorcycleType != null,
                        onClick = { showMotorcycleTypeDialog = true },
                        label = { Text(selectedMotorcycleType?.displayName ?: "Motor Tipi") },
                        leadingIcon = { Icon(Icons.Default.Motorcycle, "Motor") }
                    )
                    
                    FilterChip(
                        selected = selectedDifficulty != null,
                        onClick = { showDifficultyDialog = true },
                        label = { Text(selectedDifficulty?.name ?: "Zorluk") },
                        leadingIcon = { Icon(Icons.Default.Star, "Zorluk") }
                    )
                }
            }
            
            // Yeni rota ekle butonu
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
            
            // Reels/Post paylaş butonu
            item {
                Button(
                    onClick = { onNavigateToPostCreate() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Icon(Icons.Default.CameraAlt, "Kamera")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Fotoğraf/Video Paylaş")
                }
            }
            
            // Popüler Rotalar başlığı
            item {
                Text(
                    text = "Popüler Rotalar",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            
            // Rota kartları
            items(sampleRoutes) { route ->
                RouteCard(
                    route = route,
                    onRouteClick = { showRouteDetailDialog = route },
                    onDownload = {
                        showDownloadDialog = true
                        selectedRouteForAction = route
                    },
                    onComment = {
                        showCommentDialog = true
                        selectedRouteForAction = route
                    },
                    onShare = {
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
            title = { Text("Motor Tipi Seç") },
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
                            Text(type.displayName)
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
    
    // Zorluk seçim dialog
    if (showDifficultyDialog) {
        AlertDialog(
            onDismissRequest = { showDifficultyDialog = false },
            title = { Text("Zorluk Seviyesi Seç") },
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
    
    // Yeni rota ekleme dialog (harita çizme ile)
    if (showNewRouteDialog) {
        AlertDialog(
            onDismissRequest = { showNewRouteDialog = false },
            title = { Text("Yeni Rota Oluştur") },
            text = {
                Column {
                    Text("Rota oluşturma seçenekleri:")
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Button(
                        onClick = {
                            showNewRouteDialog = false
                            onNavigateToRouteDrawing("Yeni Rota", "Haritada çizilen rota")
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Timeline, "Çiz")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Haritada Rota Çiz")
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Button(
                        onClick = {
                            showNewRouteDialog = false
                            // GPS ile rota kaydetme
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.GpsFixed, "GPS")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("GPS ile Rota Kaydet")
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Button(
                        onClick = {
                            showNewRouteDialog = false
                            // Manuel rota oluşturma
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Edit, "Manuel")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Manuel Rota Oluştur")
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showNewRouteDialog = false }) {
                    Text("İptal")
                }
            }
        )
    }
    
    // Rota çizme dialog
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
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = routeDescription,
                        onValueChange = { routeDescription = it },
                        label = { Text("Açıklama") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Haritada rota çizmek için:")
                    Text("• Harita ekranına gidin")
                    Text("• 'Rota Çiz' butonuna basın")
                    Text("• Haritada tıklayarak rota oluşturun")
                    Text("• Çizimi bitirmek için tekrar butona basın")
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (routeName.isNotBlank()) {
                            showRouteDrawingDialog = false
                            onNavigateToRouteDrawing(routeName, routeDescription)
                        }
                    },
                    enabled = routeName.isNotBlank()
                ) {
                    Text("Haritaya Git")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRouteDrawingDialog = false }) {
                    Text("İptal")
                }
            }
        )
    }
    
    // Fotoğraf/Video paylaş dialog
    if (showCreatePostDialog) {
        var postDescription by remember { mutableStateOf("") }
        var postLocation by remember { mutableStateOf("") }
        var showLocationRequired by remember { mutableStateOf(false) }
        
        AlertDialog(
            onDismissRequest = { showCreatePostDialog = false },
            title = { Text("Fotoğraf/Video Paylaş") },
            text = {
                Column {
                    Text("Fotoğraf veya video seçin:")
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Button(
                        onClick = { /* Kamera/galeri aç */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.CameraAlt, "Kamera")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Fotoğraf/Video Seç")
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    OutlinedTextField(
                        value = postDescription,
                        onValueChange = { postDescription = it },
                        label = { Text("Açıklama") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    OutlinedTextField(
                        value = postLocation,
                        onValueChange = { postLocation = it },
                        label = { Text("Konum (Zorunlu)") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = showLocationRequired && postLocation.isBlank()
                    )
                    
                    if (showLocationRequired && postLocation.isBlank()) {
                        Text(
                            text = "Konum bilgisi zorunludur!",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (postLocation.isNotBlank()) {
                            // Post paylaş
                            showCreatePostDialog = false
                        } else {
                            showLocationRequired = true
                        }
                    }
                ) {
                    Text("Paylaş")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCreatePostDialog = false }) {
                    Text("İptal")
                }
            }
        )
    }
    
    // Yol tarifi dialog
    if (showDownloadDialog) {
        AlertDialog(
            onDismissRequest = { showDownloadDialog = false },
            title = { Text("Yol Tarifi Al") },
            text = {
                Column {
                    selectedRouteForAction?.let { route ->
                        Text("${route.name} rotasına gitmek istiyor musunuz?")
                        Spacer(modifier = Modifier.height(8.dp))
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
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDownloadDialog = false
                        selectedRouteForAction?.let { route ->
                            onNavigateToRouteDetail(route.id.toString())
                        }
                        selectedRouteForAction = null
                    }
                ) {
                    Text("Haritada Göster")
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
                        Text("Bu rota sosyal medyada paylaşılacak.")
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
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
    
    // Rota detay dialog
    showRouteDetailDialog?.let { route ->
        AlertDialog(
            onDismissRequest = { showRouteDetailDialog = null },
            title = { Text(route.name) },
            text = {
                Column {
                    Text(route.description)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Oluşturan: ${route.creatorName}")
                    Text("Mesafe: ${route.distance} km")
                    Text("Süre: ${route.duration / 60000} dakika")
                    Text("Zorluk: ${route.difficulty.name}")
                    Text("Motor Tipi: ${route.motorcycleType.displayName}")
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Yorumlar bölümü
                    Text("Yorumlar:", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Örnek yorumlar
                    listOf(
                        "Harika bir rota! Virajlar çok keyifli.",
                        "Manzara muhteşem, kesinlikle tavsiye ederim.",
                        "Yol durumu iyi, rahat sürüş."
                    ).forEach { comment ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp)
                            ) {
                                Text(
                                    text = comment,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "Kullanıcı • 2 saat önce",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showRouteDetailDialog = null
                        onNavigateToRouteDetail(route.id.toString())
                    }
                ) {
                    Text("Haritada Göster")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRouteDetailDialog = null }) {
                    Text("Kapat")
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
}

@Composable
fun RouteCard(
    route: Route,
    onRouteClick: () -> Unit,
    onDownload: () -> Unit,
    onComment: () -> Unit,
    onShare: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onRouteClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Başlık ve yıldız
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
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(20.dp)
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
                    Text("${route.duration / 60000} dk")
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
                    Icon(Icons.Default.Directions, "Git")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Git")
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

