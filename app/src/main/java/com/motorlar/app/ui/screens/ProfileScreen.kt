package com.motorlar.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    var showEditProfileDialog by remember { mutableStateOf(false) }
    
    // Dialog states
    var showPersonalInfoDialog by remember { mutableStateOf(false) }
    var showMotorcycleInfoDialog by remember { mutableStateOf(false) }
    var showRideStatisticsDialog by remember { mutableStateOf(false) }
    var showSavedRoutesDialog by remember { mutableStateOf(false) }
    var showTeamSettingsDialog by remember { mutableStateOf(false) }
    var showNotificationSettingsDialog by remember { mutableStateOf(false) }
    var showPrivacySettingsDialog by remember { mutableStateOf(false) }
    var showHelpDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    
    val currentUser = viewModel.uiState.collectAsState().value.currentUser
    
    // Örnek kullanıcı bilgileri
    val userInfo = remember {
        UserProfile(
            name = currentUser ?: "Kullanıcı",
            email = "${currentUser ?: "user"}@example.com",
            motorcycleType = "Sport",
            experienceLevel = "Orta",
            totalDistance = 1250.5f,
            totalTime = 45.5f,
            routesCreated = 12,
            routesCompleted = 28
        )
    }
    
    // Menü öğeleri
    val menuItems = remember {
        listOf(
            MenuItem("Kişisel Bilgiler", Icons.Default.Person, "Profil bilgilerini düzenle") { showPersonalInfoDialog = true },
            MenuItem("Motor Bilgileri", Icons.Default.Motorcycle, "Motor tipi ve özellikleri") { showMotorcycleInfoDialog = true },
            MenuItem("Sürüş İstatistikleri", Icons.Default.Analytics, "Detaylı sürüş verileri") { showRideStatisticsDialog = true },
            MenuItem("Kayıtlı Rotalar", Icons.Default.Bookmark, "Favori rotalarınız") { showSavedRoutesDialog = true },
            MenuItem("Takım Ayarları", Icons.Default.Group, "Takım tercihleri") { showTeamSettingsDialog = true },
            MenuItem("Bildirimler", Icons.Default.Notifications, "Bildirim ayarları") { showNotificationSettingsDialog = true },
            MenuItem("Gizlilik", Icons.Default.Security, "Gizlilik ayarları") { showPrivacySettingsDialog = true },
            MenuItem("Yardım", Icons.Default.Help, "Yardım ve destek") { showHelpDialog = true },
            MenuItem("Hakkında", Icons.Default.Info, "Uygulama bilgileri") { showAboutDialog = true },
            MenuItem("Çıkış Yap", Icons.Default.Logout, "Hesaptan çıkış") { 
                viewModel.logoutUser()
                showLogoutDialog = true 
            }
        )
    }
    
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Üst bar
        TopAppBar(
            title = { Text("Profil", fontWeight = FontWeight.Bold) },
            actions = {
                IconButton(onClick = { showEditProfileDialog = true }) {
                    Icon(Icons.Default.Edit, "Düzenle")
                }
                IconButton(onClick = { showNotificationSettingsDialog = true }) {
                    Icon(Icons.Default.Settings, "Ayarlar")
                }
            }
        )
        
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Profil kartı
            item {
                ProfileCard(userInfo = userInfo)
            }
            
            // İstatistikler kartı
            item {
                StatisticsCard(userInfo = userInfo)
            }
            
            // Menü öğeleri
            items(menuItems) { menuItem ->
                MenuItemCard(menuItem = menuItem)
            }
        }
    }
    
    // Profil düzenleme dialog
    if (showEditProfileDialog) {
        EditProfileDialog(
            userInfo = userInfo,
            onDismiss = { showEditProfileDialog = false },
            onConfirm = { updatedUser ->
                // Profil güncelleme işlemi
                showEditProfileDialog = false
            }
        )
    }
    
    // Kişisel bilgiler dialog
    if (showPersonalInfoDialog) {
        AlertDialog(
            onDismissRequest = { showPersonalInfoDialog = false },
            title = { Text("Kişisel Bilgiler") },
            text = {
                Column {
                    Text("Ad Soyad: ${userInfo.name}")
                    Text("E-posta: ${userInfo.email}")
                    Text("Telefon: +90 555 123 4567")
                    Text("Doğum Tarihi: 15 Mart 1990")
                    Text("Şehir: İstanbul")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Bu bilgileri düzenlemek için profil düzenle butonunu kullanın.")
                }
            },
            confirmButton = {
                TextButton(onClick = { showPersonalInfoDialog = false }) {
                    Text("Tamam")
                }
            }
        )
    }
    
    // Motor bilgileri dialog
    if (showMotorcycleInfoDialog) {
        AlertDialog(
            onDismissRequest = { showMotorcycleInfoDialog = false },
            title = { Text("Motor Bilgileri") },
            text = {
                Column {
                    Text("Motor Tipi: ${userInfo.motorcycleType}")
                    Text("Marka: Honda")
                    Text("Model: CBR600RR")
                    Text("Yıl: 2020")
                    Text("Motor Hacmi: 600cc")
                    Text("Renk: Kırmızı")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Deneyim Seviyesi: ${userInfo.experienceLevel}")
                }
            },
            confirmButton = {
                TextButton(onClick = { showMotorcycleInfoDialog = false }) {
                    Text("Tamam")
                }
            }
        )
    }
    
    // Sürüş istatistikleri dialog
    if (showRideStatisticsDialog) {
        AlertDialog(
            onDismissRequest = { showRideStatisticsDialog = false },
            title = { Text("Sürüş İstatistikleri") },
            text = {
                Column {
                    Text("Toplam Mesafe: ${userInfo.totalDistance.toInt()} km")
                    Text("Toplam Süre: ${userInfo.totalTime.toInt()} saat")
                    Text("Ortalama Hız: 85 km/h")
                    Text("Maksimum Hız: 180 km/h")
                    Text("Toplam Sürüş: 45 gün")
                    Text("En Uzun Sürüş: 320 km")
                    Text("Favori Rota: İstanbul - Sapanca")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Sürüş Puanı: 8.5/10")
                }
            },
            confirmButton = {
                TextButton(onClick = { showRideStatisticsDialog = false }) {
                    Text("Tamam")
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
                    Text("Favori Rotalar:")
                    Text("• İstanbul - Sapanca Gölü")
                    Text("• Bolu - Abant Gölü")
                    Text("• İzmir - Çeşme Sahil")
                    Text("• Kapadokya Turu")
                    Text("• Ege Sahil Yolu")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Toplam: 5 kayıtlı rota")
                }
            },
            confirmButton = {
                TextButton(onClick = { showSavedRoutesDialog = false }) {
                    Text("Tamam")
                }
            }
        )
    }
    
    // Takım ayarları dialog
    if (showTeamSettingsDialog) {
        AlertDialog(
            onDismissRequest = { showTeamSettingsDialog = false },
            title = { Text("Takım Ayarları") },
            text = {
                Column {
                    TextButton(
                        onClick = { /* Konum paylaşımı */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Konum Paylaşımı: Açık")
                    }
                    TextButton(
                        onClick = { /* Takım davetleri */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Takım Davetleri: Açık")
                    }
                    TextButton(
                        onClick = { /* Otomatik takım katılımı */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Otomatik Katılım: Kapalı")
                    }
                    TextButton(
                        onClick = { /* Takım bildirimleri */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Takım Bildirimleri: Açık")
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showTeamSettingsDialog = false }) {
                    Text("Kaydet")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTeamSettingsDialog = false }) {
                    Text("İptal")
                }
            }
        )
    }
    
    // Bildirim ayarları dialog
    if (showNotificationSettingsDialog) {
        AlertDialog(
            onDismissRequest = { showNotificationSettingsDialog = false },
            title = { Text("Bildirim Ayarları") },
            text = {
                Column {
                    TextButton(
                        onClick = { /* Push bildirimleri */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Push Bildirimleri: Açık")
                    }
                    TextButton(
                        onClick = { /* E-posta bildirimleri */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("E-posta Bildirimleri: Kapalı")
                    }
                    TextButton(
                        onClick = { /* Rota önerileri */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Rota Önerileri: Açık")
                    }
                    TextButton(
                        onClick = { /* Takım bildirimleri */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Takım Bildirimleri: Açık")
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showNotificationSettingsDialog = false }) {
                    Text("Kaydet")
                }
            },
            dismissButton = {
                TextButton(onClick = { showNotificationSettingsDialog = false }) {
                    Text("İptal")
                }
            }
        )
    }
    
    // Gizlilik ayarları dialog
    if (showPrivacySettingsDialog) {
        AlertDialog(
            onDismissRequest = { showPrivacySettingsDialog = false },
            title = { Text("Gizlilik Ayarları") },
            text = {
                Column {
                    TextButton(
                        onClick = { /* Profil görünürlüğü */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Profil Görünürlüğü: Herkese Açık")
                    }
                    TextButton(
                        onClick = { /* Konum paylaşımı */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Konum Paylaşımı: Sadece Takım")
                    }
                    TextButton(
                        onClick = { /* Rota paylaşımı */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Rota Paylaşımı: Herkese Açık")
                    }
                    TextButton(
                        onClick = { /* İstatistik paylaşımı */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("İstatistik Paylaşımı: Gizli")
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showPrivacySettingsDialog = false }) {
                    Text("Kaydet")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPrivacySettingsDialog = false }) {
                    Text("İptal")
                }
            }
        )
    }
    
    // Yardım dialog
    if (showHelpDialog) {
        AlertDialog(
            onDismissRequest = { showHelpDialog = false },
            title = { Text("Yardım ve Destek") },
            text = {
                Column {
                    Text("Sık Sorulan Sorular:")
                    Text("• Rota nasıl oluşturulur?")
                    Text("• Takıma nasıl katılırım?")
                    Text("• Konum paylaşımı nasıl çalışır?")
                    Text("• Rota kaydetme nasıl yapılır?")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Destek: support@motorlar.com")
                    Text("Telefon: +90 212 555 0123")
                }
            },
            confirmButton = {
                TextButton(onClick = { showHelpDialog = false }) {
                    Text("Tamam")
                }
            }
        )
    }
    
    // Hakkında dialog
    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            title = { Text("Hakkında") },
            text = {
                Column {
                    Text("Motorlar v1.0.0")
                    Text("Motorcu rota uygulaması")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Geliştirici: Motorlar Team")
                    Text("Lisans: MIT")
                    Text("© 2024 Motorlar")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Bu uygulama motorcuların rotalarını paylaşması ve takip etmesi için geliştirilmiştir.")
                }
            },
            confirmButton = {
                TextButton(onClick = { showAboutDialog = false }) {
                    Text("Tamam")
                }
            }
        )
    }
    
    // Çıkış yap dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Çıkış Yap") },
            text = {
                Text("Hesabınızdan çıkış yapmak istediğinizden emin misiniz?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        // Çıkış yapma işlemi
                        showLogoutDialog = false
                    }
                ) {
                    Text("Çıkış Yap")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("İptal")
                }
            }
        )
    }
}

@Composable
fun ProfileCard(userInfo: UserProfile) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = userInfo.name.take(1),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Kullanıcı adı
            Text(
                text = userInfo.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                text = userInfo.email,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Motor bilgileri
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                InfoChip(
                    icon = Icons.Default.Motorcycle,
                    label = "Motor",
                    value = userInfo.motorcycleType
                )
                
                InfoChip(
                    icon = Icons.Default.Star,
                    label = "Seviye",
                    value = userInfo.experienceLevel
                )
            }
        }
    }
}

@Composable
fun StatisticsCard(userInfo: UserProfile) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Sürüş İstatistikleri",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatItem(
                    icon = Icons.Default.Place,
                    label = "Toplam Mesafe",
                    value = "${userInfo.totalDistance.toInt()} km"
                )
                
                StatItem(
                    icon = Icons.Default.Schedule,
                    label = "Toplam Süre",
                    value = "${userInfo.totalTime.toInt()} saat"
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatItem(
                    icon = Icons.Default.Create,
                    label = "Oluşturulan",
                    value = "${userInfo.routesCreated} rota"
                )
                
                StatItem(
                    icon = Icons.Default.CheckCircle,
                    label = "Tamamlanan",
                    value = "${userInfo.routesCompleted} rota"
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuItemCard(menuItem: MenuItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = menuItem.onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = menuItem.icon,
                contentDescription = menuItem.title,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = menuItem.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                
                Text(
                    text = menuItem.description,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Git",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun InfoChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        
        Text(
            text = label,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun StatItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        
        Text(
            text = label,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileDialog(
    userInfo: UserProfile,
    onDismiss: () -> Unit,
    onConfirm: (UserProfile) -> Unit
) {
    var name by remember { mutableStateOf(userInfo.name) }
    var email by remember { mutableStateOf(userInfo.email) }
    var motorcycleType by remember { mutableStateOf(userInfo.motorcycleType) }
    var experienceLevel by remember { mutableStateOf(userInfo.experienceLevel) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Profil Düzenle") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Ad Soyad") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("E-posta") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                OutlinedTextField(
                    value = motorcycleType,
                    onValueChange = { motorcycleType = it },
                    label = { Text("Motor Tipi") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                OutlinedTextField(
                    value = experienceLevel,
                    onValueChange = { experienceLevel = it },
                    label = { Text("Deneyim Seviyesi") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(
                        userInfo.copy(
                            name = name,
                            email = email,
                            motorcycleType = motorcycleType,
                            experienceLevel = experienceLevel
                        )
                    )
                },
                enabled = name.isNotEmpty() && email.isNotEmpty()
            ) {
                Text("Kaydet")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("İptal")
            }
        }
    )
}

// Data classes
data class UserProfile(
    val name: String,
    val email: String,
    val motorcycleType: String,
    val experienceLevel: String,
    val totalDistance: Float,
    val totalTime: Float,
    val routesCreated: Int,
    val routesCompleted: Int
)

data class MenuItem(
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val description: String,
    val onClick: () -> Unit
)