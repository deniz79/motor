package com.motorlar.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
fun PostCreateScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var postDescription by remember { mutableStateOf("") }
    var postLocation by remember { mutableStateOf("") }
    var showLocationRequired by remember { mutableStateOf(false) }
    var isImageSelected by remember { mutableStateOf(false) }
    var isVideoSelected by remember { mutableStateOf(false) }
    
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Üst bar
        TopAppBar(
            title = { Text("Fotoğraf/Video Paylaş", fontWeight = FontWeight.Bold) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, "Geri")
                }
            },
            actions = {
                TextButton(
                    onClick = {
                        if (postLocation.isNotBlank()) {
                            // Post paylaş
                            onBack()
                        } else {
                            showLocationRequired = true
                        }
                    },
                    enabled = postLocation.isNotBlank()
                ) {
                    Text("Paylaş", fontWeight = FontWeight.Bold)
                }
            }
        )
        
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Fotoğraf/Video seçim alanı
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .clip(RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (!isImageSelected && !isVideoSelected) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    Icons.Default.CameraAlt,
                                    contentDescription = "Kamera",
                                    modifier = Modifier.size(64.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "Fotoğraf veya Video Seçin",
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    Button(
                                        onClick = { isImageSelected = true }
                                    ) {
                                        Icon(Icons.Default.PhotoCamera, "Kamera")
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Kamera")
                                    }
                                    
                                    Button(
                                        onClick = { isImageSelected = true }
                                    ) {
                                        Icon(Icons.Default.PhotoLibrary, "Galeri")
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Galeri")
                                    }
                                    
                                    Button(
                                        onClick = { isVideoSelected = true }
                                    ) {
                                        Icon(Icons.Default.Videocam, "Video")
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Video")
                                    }
                                }
                            }
                        } else {
                            // Seçilen medya gösterimi
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    if (isVideoSelected) Icons.Default.Videocam else Icons.Default.Image,
                                    contentDescription = "Medya",
                                    modifier = Modifier.size(48.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
            
            // Açıklama alanı
            item {
                OutlinedTextField(
                    value = postDescription,
                    onValueChange = { postDescription = it },
                    label = { Text("Açıklama") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5,
                    placeholder = { Text("Bu anınızı paylaşın...") }
                )
            }
            
            // Konum alanı
            item {
                Column {
                    OutlinedTextField(
                        value = postLocation,
                        onValueChange = { postLocation = it },
                        label = { Text("Konum (Zorunlu)") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = showLocationRequired && postLocation.isBlank(),
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    // Anlık konum alma
                                    postLocation = "İstanbul, Türkiye" // Gerçek uygulamada GPS'ten alınacak
                                }
                            ) {
                                Icon(Icons.Default.LocationOn, "Anlık Konum")
                            }
                        }
                    )
                    
                    if (showLocationRequired && postLocation.isBlank()) {
                        Text(
                            text = "Konum bilgisi zorunludur!",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Button(
                        onClick = {
                            // Anlık konum alma
                            postLocation = "İstanbul, Türkiye" // Gerçek uygulamada GPS'ten alınacak
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Icon(Icons.Default.LocationOn, "Anlık Konum")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Anlık Konumu Al")
                    }
                }
            }
            
            // Motor bilgileri
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Motor Bilgileri",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Motor Tipi", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("Sport", fontWeight = FontWeight.Bold)
                            }
                            Column {
                                Text("Marka/Model", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("Honda CBR", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
            
            // Paylaşım ayarları
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Paylaşım Ayarları",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Herkese açık")
                            Switch(
                                checked = true,
                                onCheckedChange = { /* Ayarları değiştir */ }
                            )
                        }
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Konum göster")
                            Switch(
                                checked = true,
                                onCheckedChange = { /* Ayarları değiştir */ }
                            )
                        }
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Yorumlara izin ver")
                            Switch(
                                checked = true,
                                onCheckedChange = { /* Ayarları değiştir */ }
                            )
                        }
                    }
                }
            }
            
            // Önizleme
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Önizleme",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        // Önizleme kartı
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                // Fotoğraf/Video placeholder
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                                        .clip(RoundedCornerShape(8.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        if (isVideoSelected) Icons.Default.Videocam else Icons.Default.Image,
                                        contentDescription = "Medya",
                                        modifier = Modifier.size(48.dp),
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                
                                // Üst bilgiler
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Column {
                                        Text(
                                            text = "Siz",
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                        Text(
                                            text = postLocation.ifBlank { "Konum seçilmedi" },
                                            fontSize = 12.sp,
                                            color = Color.White
                                        )
                                    }
                                }
                                
                                // Alt bilgiler
                                Column(
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .padding(12.dp)
                                ) {
                                    Text(
                                        text = postDescription.ifBlank { "Açıklama yok" },
                                        color = Color.White,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
