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
    modifier: Modifier = Modifier
) {
    var showEditProfileDialog by remember { mutableStateOf(false) }
    
    // Örnek kullanıcı bilgileri
    val userInfo = remember {
        UserProfile(
            name = "Ahmet Yılmaz",
            email = "ahmet@example.com",
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
            MenuItem("Kişisel Bilgiler", Icons.Default.Person, "Profil bilgilerini düzenle"),
            MenuItem("Motor Bilgileri", Icons.Default.Motorcycle, "Motor tipi ve özellikleri"),
            MenuItem("Sürüş İstatistikleri", Icons.Default.Analytics, "Detaylı sürüş verileri"),
            MenuItem("Kayıtlı Rotalar", Icons.Default.Bookmark, "Favori rotalarınız"),
            MenuItem("Takım Ayarları", Icons.Default.Group, "Takım tercihleri"),
            MenuItem("Bildirimler", Icons.Default.Notifications, "Bildirim ayarları"),
            MenuItem("Gizlilik", Icons.Default.Security, "Gizlilik ayarları"),
            MenuItem("Yardım", Icons.Default.Help, "Yardım ve destek"),
            MenuItem("Hakkında", Icons.Default.Info, "Uygulama bilgileri"),
            MenuItem("Çıkış Yap", Icons.Default.Logout, "Hesaptan çıkış")
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
        onClick = { /* Menü öğesi tıklama */ }
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
    val description: String
)