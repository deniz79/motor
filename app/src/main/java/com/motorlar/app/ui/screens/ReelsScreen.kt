package com.motorlar.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReelsScreen(
    viewModel: MainViewModel,
    onNavigateToLocation: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var currentReelIndex by remember { mutableStateOf(0) }
    var showCommentsDialog by remember { mutableStateOf<ReelPost?>(null) }
    var showShareDialog by remember { mutableStateOf<ReelPost?>(null) }
    
    // Örnek reel verileri
    val reels = remember {
        listOf(
            ReelPost(
                id = 1,
                username = "Motorcu_Ahmet",
                userAvatar = "A",
                location = "Sapanca Gölü, Sakarya",
                description = "Harika bir gün! Sapanca'da muhteşem manzara 🏍️ #motor #sapanca #manzara",
                likes = 245,
                comments = 18,
                shares = 12,
                isLiked = false,
                isVideo = true,
                videoUrl = "sample_video_1"
            ),
            ReelPost(
                id = 2,
                username = "Rider_Merve",
                userAvatar = "M",
                location = "Abant Gölü, Bolu",
                description = "Abant'ta virajların keyfini çıkardık ✨ #abant #viraj #motorcu",
                likes = 189,
                comments = 23,
                shares = 8,
                isLiked = true,
                isVideo = true,
                videoUrl = "sample_video_2"
            ),
            ReelPost(
                id = 3,
                username = "Biker_Can",
                userAvatar = "C",
                location = "Çeşme Sahili, İzmir",
                description = "Çeşme'de güneş batımı 🌅 #çeşme #güneşbatımı #sahil",
                likes = 312,
                comments = 31,
                shares = 15,
                isLiked = false,
                isVideo = true,
                videoUrl = "sample_video_3"
            ),
            ReelPost(
                id = 4,
                username = "Road_Rider",
                userAvatar = "R",
                location = "Kapadokya, Nevşehir",
                description = "Kapadokya'nın büyülü atmosferi 🏺 #kapadokya #balon #motor",
                likes = 456,
                comments = 42,
                shares = 28,
                isLiked = false,
                isVideo = true,
                videoUrl = "sample_video_4"
            )
        )
    }
    
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Üst bar
        TopAppBar(
            title = { Text("Reels", fontWeight = FontWeight.Bold) },
            actions = {
                IconButton(onClick = { /* Kamera */ }) {
                    Icon(Icons.Default.CameraAlt, "Kamera")
                }
                IconButton(onClick = { /* Arama */ }) {
                    Icon(Icons.Default.Search, "Arama")
                }
            }
        )
        
        // Reels içeriği
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                items(reels) { reel ->
                    ReelCard(
                        reel = reel,
                        onLocationClick = { location ->
                            onNavigateToLocation(location)
                        },
                        onLikeClick = { 
                            // Beğeni işlemi
                        },
                        onCommentClick = { 
                            showCommentsDialog = reel
                        },
                        onShareClick = { 
                            showShareDialog = reel
                        },
                        onUserClick = { 
                            // Kullanıcı profiline git
                        }
                    )
                }
            }
        }
    }
    
    // Yorumlar dialog
    showCommentsDialog?.let { reel ->
        AlertDialog(
            onDismissRequest = { showCommentsDialog = null },
            title = { Text("Yorumlar") },
            text = {
                Column {
                    // Örnek yorumlar
                    listOf(
                        "Harika bir paylaşım! 👏",
                        "Bu rotayı ben de gitmek istiyorum 🏍️",
                        "Manzara muhteşem görünüyor",
                        "Hangi motosiklet kullanıyorsun?",
                        "Bir dahaki sefere birlikte gidelim"
                    ).forEach { comment ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "K",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "Kullanıcı",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = comment,
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = "2 saat önce",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showCommentsDialog = null }) {
                    Text("Kapat")
                }
            }
        )
    }
    
    // Paylaş dialog
    showShareDialog?.let { reel ->
        AlertDialog(
            onDismissRequest = { showShareDialog = null },
            title = { Text("Paylaş") },
            text = {
                Column {
                    Text("Bu reel'i paylaşmak istiyor musunuz?")
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Paylaşım seçenekleri:")
                    Text("• Instagram")
                    Text("• Facebook")
                    Text("• Twitter")
                    Text("• WhatsApp")
                    Text("• Kopyala")
                }
            },
            confirmButton = {
                TextButton(onClick = { showShareDialog = null }) {
                    Text("Paylaş")
                }
            },
            dismissButton = {
                TextButton(onClick = { showShareDialog = null }) {
                    Text("İptal")
                }
            }
        )
    }
}

@Composable
fun ReelCard(
    reel: ReelPost,
    onLocationClick: (String) -> Unit,
    onLikeClick: () -> Unit,
    onCommentClick: () -> Unit,
    onShareClick: () -> Unit,
    onUserClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(500.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Video/Fotoğraf alanı
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clip(RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                // Video placeholder
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.PlayCircleOutline,
                        contentDescription = "Video",
                        modifier = Modifier.size(64.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Video Oynatılıyor",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            // Üst bilgiler
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // Kullanıcı bilgisi
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onUserClick() }
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = reel.userAvatar,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = reel.username,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = reel.location,
                            fontSize = 12.sp,
                            color = Color.White,
                            modifier = Modifier.clickable { onLocationClick(reel.location) }
                        )
                    }
                }
                
                // Daha fazla butonu
                IconButton(
                    onClick = { /* Daha fazla seçenek */ }
                ) {
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = "Daha fazla",
                        tint = Color.White
                    )
                }
            }
            
            // Alt bilgiler
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                // Açıklama
                Text(
                    text = reel.description,
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                // Etkileşim butonları
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Beğeni
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        IconButton(
                            onClick = onLikeClick,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                if (reel.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Beğen",
                                tint = if (reel.isLiked) Color.Red else Color.White
                            )
                        }
                        Text(
                            text = "${reel.likes}",
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }
                    
                    // Yorum
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        IconButton(
                            onClick = onCommentClick,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                Icons.Default.Comment,
                                contentDescription = "Yorum",
                                tint = Color.White
                            )
                        }
                        Text(
                            text = "${reel.comments}",
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }
                    
                    // Paylaş
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        IconButton(
                            onClick = onShareClick,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                Icons.Default.Share,
                                contentDescription = "Paylaş",
                                tint = Color.White
                            )
                        }
                        Text(
                            text = "${reel.shares}",
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

data class ReelPost(
    val id: Int,
    val username: String,
    val userAvatar: String,
    val location: String,
    val description: String,
    val likes: Int,
    val comments: Int,
    val shares: Int,
    val isLiked: Boolean,
    val isVideo: Boolean,
    val videoUrl: String
)
