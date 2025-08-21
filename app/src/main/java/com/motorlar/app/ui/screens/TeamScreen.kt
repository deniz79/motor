package com.motorlar.app.ui.screens

import androidx.compose.foundation.background
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
import com.motorlar.app.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) }
    var showCreateTeamDialog by remember { mutableStateOf(false) }
    var showJoinTeamDialog by remember { mutableStateOf(false) }
    
    // Örnek takımlar
    val sampleTeams = remember {
        listOf(
            TeamInfo("İstanbul Riders", 8, "Aktif", "2 saat önce"),
            TeamInfo("Bolu Adventure", 5, "Aktif", "1 saat önce"),
            TeamInfo("Ege Cruisers", 12, "Pasif", "1 gün önce")
        )
    }
    
    // Örnek takım üyeleri
    val sampleMembers = remember {
        listOf(
            MemberInfo("Ahmet Yılmaz", "Lider", "2 km uzakta", true),
            MemberInfo("Mehmet Demir", "Üye", "5 km uzakta", true),
            MemberInfo("Ayşe Kaya", "Üye", "Çevrimdışı", false),
            MemberInfo("Ali Özkan", "Üye", "1 km uzakta", true)
        )
    }
    
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Üst bar
        TopAppBar(
            title = { Text("Takım", fontWeight = FontWeight.Bold) },
            actions = {
                IconButton(onClick = { showCreateTeamDialog = true }) {
                    Icon(Icons.Default.Add, "Takım Oluştur")
                }
                IconButton(onClick = { showJoinTeamDialog = true }) {
                    Icon(Icons.Default.GroupAdd, "Takıma Katıl")
                }
            }
        )
        
        // Tab bar
        TabRow(selectedTabIndex = selectedTab) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Takımlarım") },
                icon = { Icon(Icons.Default.Group, "Takımlar") }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Aktif Sürüş") },
                icon = { Icon(Icons.Default.DirectionsBike, "Sürüş") }
            )
        }
        
        when (selectedTab) {
            0 -> TeamsTab(
                teams = sampleTeams,
                onCreateTeam = { showCreateTeamDialog = true },
                onJoinTeam = { showJoinTeamDialog = true }
            )
            1 -> ActiveRideTab(members = sampleMembers)
        }
    }
    
    // Takım oluşturma dialog
    if (showCreateTeamDialog) {
        CreateTeamDialog(
            onDismiss = { showCreateTeamDialog = false },
            onConfirm = { teamName ->
                // Takım oluşturma işlemi
                showCreateTeamDialog = false
            }
        )
    }
    
    // Takıma katılma dialog
    if (showJoinTeamDialog) {
        JoinTeamDialog(
            onDismiss = { showJoinTeamDialog = false },
            onConfirm = { teamCode ->
                // Takıma katılma işlemi
                showJoinTeamDialog = false
            }
        )
    }
}

@Composable
fun TeamsTab(
    teams: List<TeamInfo>,
    onCreateTeam: () -> Unit,
    onJoinTeam: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hızlı aksiyonlar
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onCreateTeam,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Add, "Oluştur")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Takım Oluştur")
                }
                
                OutlinedButton(
                    onClick = onJoinTeam,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.GroupAdd, "Katıl")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Takıma Katıl")
                }
            }
        }
        
        // Takım listesi
        items(teams) { team ->
            TeamCard(team = team)
        }
    }
}

@Composable
fun ActiveRideTab(members: List<MemberInfo>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Aktif sürüş durumu
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.DirectionsBike,
                        contentDescription = "Aktif Sürüş",
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "Aktif Grup Sürüşü",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Text(
                        text = "4 üye aktif",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        
        // Üye listesi
        items(members) { member ->
            MemberCard(member = member)
        }
        
        // Sürüş kontrolleri
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Sürüş Kontrolleri",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        OutlinedButton(
                            onClick = { /* Konum paylaşımını aç/kapat */ }
                        ) {
                            Icon(Icons.Default.LocationOn, "Konum")
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Konum")
                        }
                        
                        OutlinedButton(
                            onClick = { /* Mesaj gönder */ }
                        ) {
                            Icon(Icons.Default.Message, "Mesaj")
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Mesaj")
                        }
                        
                        OutlinedButton(
                            onClick = { /* Acil durum */ }
                        ) {
                            Icon(Icons.Default.Emergency, "Acil")
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Acil")
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamCard(team: TeamInfo) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = { /* Takım detayına git */ }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = team.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                
                FilterChip(
                    selected = false,
                    onClick = { },
                    label = { Text(team.status, fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = if (team.status == "Aktif") 
                            MaterialTheme.colorScheme.primaryContainer 
                        else 
                            MaterialTheme.colorScheme.surfaceVariant
                    )
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${team.memberCount} üye",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Text(
                    text = team.lastActivity,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedButton(
                    onClick = { /* Takıma katıl */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.GroupAdd, "Katıl")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Katıl")
                }
                
                Spacer(modifier = Modifier.width(8.dp))
                
                OutlinedButton(
                    onClick = { /* Takım detayı */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Info, "Detay")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Detay")
                }
            }
        }
    }
}

@Composable
fun MemberCard(member: MemberInfo) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = if (member.isOnline) 
                            MaterialTheme.colorScheme.primary 
                        else 
                            MaterialTheme.colorScheme.surfaceVariant,
                        shape = androidx.compose.foundation.shape.CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = member.name.take(1),
                    color = if (member.isOnline) 
                        MaterialTheme.colorScheme.onPrimary 
                    else 
                        MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Üye bilgileri
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = member.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Text(
                    text = member.role,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Text(
                    text = member.location,
                    fontSize = 12.sp,
                    color = if (member.isOnline) 
                        MaterialTheme.colorScheme.primary 
                    else 
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            // Online durumu
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(
                        color = if (member.isOnline) 
                            MaterialTheme.colorScheme.primary 
                        else 
                            MaterialTheme.colorScheme.surfaceVariant,
                        shape = androidx.compose.foundation.shape.CircleShape
                    )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTeamDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var teamName by remember { mutableStateOf("") }
    var teamDescription by remember { mutableStateOf("") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Yeni Takım Oluştur") },
        text = {
            Column {
                OutlinedTextField(
                    value = teamName,
                    onValueChange = { teamName = it },
                    label = { Text("Takım Adı") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = teamDescription,
                    onValueChange = { teamDescription = it },
                    label = { Text("Açıklama (İsteğe bağlı)") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { 
                    if (teamName.isNotBlank()) {
                        onConfirm(teamName)
                    }
                },
                enabled = teamName.isNotBlank()
            ) {
                Text("Oluştur")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("İptal")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoinTeamDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var teamCode by remember { mutableStateOf("") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Takıma Katıl") },
        text = {
            OutlinedTextField(
                value = teamCode,
                onValueChange = { teamCode = it },
                label = { Text("Takım Kodu") },
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(teamCode) },
                enabled = teamCode.isNotEmpty()
            ) {
                Text("Katıl")
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
data class TeamInfo(
    val name: String,
    val memberCount: Int,
    val status: String,
    val lastActivity: String
)

data class MemberInfo(
    val name: String,
    val role: String,
    val location: String,
    val isOnline: Boolean
)