package com.motorlar.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordScreen(
    modifier: Modifier = Modifier
) {
    var isRecording by remember { mutableStateOf(false) }
    var recordingTime by remember { mutableStateOf(0L) }
    var currentSpeed by remember { mutableStateOf(0f) }
    var maxSpeed by remember { mutableStateOf(0f) }
    var averageSpeed by remember { mutableStateOf(0f) }
    var distance by remember { mutableStateOf(0f) }
    var leanAngle by remember { mutableStateOf(0f) }
    
    // Zamanlayıcı efekti
    LaunchedEffect(isRecording) {
        while (isRecording) {
            delay(1000)
            recordingTime += 1000
        }
    }
    
    // Hız simülasyonu
    LaunchedEffect(isRecording) {
        while (isRecording) {
            delay(2000)
            currentSpeed = (60..120).random().toFloat()
            if (currentSpeed > maxSpeed) maxSpeed = currentSpeed
            averageSpeed = (currentSpeed + averageSpeed) / 2
            distance += currentSpeed * 0.000556f // km/h'den km'ye çevirme
            leanAngle = (-15..15).random().toFloat()
        }
    }
    
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Üst bar
        TopAppBar(
            title = { Text("Rota Kaydı", fontWeight = FontWeight.Bold) },
            actions = {
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
            // Kayıt durumu kartı
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isRecording) 
                            MaterialTheme.colorScheme.primaryContainer 
                        else 
                            MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = if (isRecording) Icons.Default.RadioButtonChecked else Icons.Default.RadioButtonUnchecked,
                            contentDescription = "Kayıt Durumu",
                            modifier = Modifier.size(48.dp),
                            tint = if (isRecording) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = if (isRecording) "Kayıt Devam Ediyor" else "Kayıt Bekliyor",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        
                        if (isRecording) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = formatTime(recordingTime),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
            
            // Hız bilgileri kartı
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Hız Bilgileri",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            SpeedInfoCard(
                                title = "Mevcut Hız",
                                value = "${currentSpeed.toInt()}",
                                unit = "km/h",
                                icon = Icons.Default.Speed
                            )
                            
                            SpeedInfoCard(
                                title = "Maksimum",
                                value = "${maxSpeed.toInt()}",
                                unit = "km/h",
                                icon = Icons.Default.TrendingUp
                            )
                            
                            SpeedInfoCard(
                                title = "Ortalama",
                                value = "${averageSpeed.toInt()}",
                                unit = "km/h",
                                icon = Icons.Default.Analytics
                            )
                        }
                    }
                }
            }
            
            // Mesafe ve açı kartı
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Sürüş Bilgileri",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            SpeedInfoCard(
                                title = "Mesafe",
                                value = String.format("%.1f", distance),
                                unit = "km",
                                icon = Icons.Default.Place
                            )
                            
                            SpeedInfoCard(
                                title = "Yatma Açısı",
                                value = "${leanAngle.toInt()}",
                                unit = "°",
                                icon = Icons.Default.RotateRight
                            )
                        }
                    }
                }
            }
            
            // Kayıt kontrolleri
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Başlat/Durdur butonu
                    Button(
                        onClick = { isRecording = !isRecording },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isRecording) 
                                MaterialTheme.colorScheme.error 
                            else 
                                MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            if (isRecording) Icons.Default.Stop else Icons.Default.PlayArrow,
                            contentDescription = if (isRecording) "Durdur" else "Başlat"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(if (isRecording) "Durdur" else "Başlat")
                    }
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    // Duraklat/Devam et butonu
                    OutlinedButton(
                        onClick = { /* Duraklat/Devam et */ },
                        modifier = Modifier.weight(1f),
                        enabled = isRecording
                    ) {
                        Icon(Icons.Default.Pause, "Duraklat")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Duraklat")
                    }
                }
            }
            
            // Ek kontroller
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OutlinedButton(
                        onClick = { /* Rota kaydet */ },
                        enabled = !isRecording && recordingTime > 0
                    ) {
                        Icon(Icons.Default.Save, "Kaydet")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Kaydet")
                    }
                    
                    OutlinedButton(
                        onClick = { /* İstatistikler */ },
                        enabled = recordingTime > 0
                    ) {
                        Icon(Icons.Default.Analytics, "İstatistikler")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("İstatistikler")
                    }
                    
                    OutlinedButton(
                        onClick = { /* Paylaş */ },
                        enabled = recordingTime > 0
                    ) {
                        Icon(Icons.Default.Share, "Paylaş")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Paylaş")
                    }
                }
            }
            
            // Kayıt geçmişi
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Son Kayıtlar",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        // Örnek kayıtlar
                        listOf(
                            "İstanbul - Sapanca" to "2 saat 15 dk",
                            "Bolu - Abant" to "1 saat 45 dk",
                            "İzmir - Çeşme" to "1 saat 30 dk"
                        ).forEach { (route, duration) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(route, fontSize = 14.sp)
                                Text(duration, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SpeedInfoCard(
    title: String,
    value: String,
    unit: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        
        Text(
            text = unit,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Text(
            text = title,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

fun formatTime(millis: Long): String {
    val seconds = (millis / 1000).toInt()
    val minutes = seconds / 60
    val hours = minutes / 60
    val remainingMinutes = minutes % 60
    val remainingSeconds = seconds % 60
    
    return if (hours > 0) {
        String.format("%02d:%02d:%02d", hours, remainingMinutes, remainingSeconds)
    } else {
        String.format("%02d:%02d", remainingMinutes, remainingSeconds)
    }
}