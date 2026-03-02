package com.example.mymedifetchproject.provider

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.mymedifetchproject.Screen

// --- ENUMS ---
enum class ProviderTab(val label: String, val icon: ImageVector) {
    Home("Home", Icons.Filled.Dashboard),
    Patients("Patients", Icons.Filled.People),
    Prescription("Prescribe", Icons.Filled.Assignment),
    Profile("Profile", Icons.Filled.Person)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardServiceProviderScreen(
    isDarkMode: Boolean,
    onThemeToggle: (Boolean) -> Unit,
    onNavigate: (String) -> Unit = {},
    // ✅ ADDED: Parameters to fix NavGraph errors
    onEditProfile: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(ProviderTab.Home) }
    var showLabInbox by remember { mutableStateOf(false) }

    val bgColor = if (isDarkMode) Color.Black else Color(0xFFF8FBFB)
    val navBarColor = if (isDarkMode) Color.Black else Color.White
    val accentTeal = if (isDarkMode) Color(0xFF4DB6AC) else Color(0xFF2C7B76)
    val primaryText = if (isDarkMode) Color.White else Color.Black

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = navBarColor,
                tonalElevation = 8.dp
            ) {
                ProviderTab.entries.forEach { tab ->
                    val isSelected = !showLabInbox && selectedTab == tab
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            selectedTab = tab
                            showLabInbox = false
                        },
                        label = { Text(tab.label, fontSize = 10.sp) },
                        icon = { Icon(tab.icon, contentDescription = tab.label) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = accentTeal,
                            selectedTextColor = accentTeal,
                            indicatorColor = accentTeal.copy(alpha = 0.1f),
                            unselectedIconColor = if (isDarkMode) Color.Gray else Color.DarkGray
                        )
                    )
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(bgColor)
        ) {
            when {
                showLabInbox -> {
                    ProviderLabResultsContent(
                        isDarkMode = isDarkMode,
                        accentTeal = accentTeal,
                        onBack = { showLabInbox = false },
                        onWritePrescription = {
                            showLabInbox = false
                            selectedTab = ProviderTab.Prescription
                        }
                    )
                }

                selectedTab == ProviderTab.Home -> {
                    ProviderHomeContent(
                        isDarkMode = isDarkMode,
                        accentTeal = accentTeal,
                        onViewReportsClick = { showLabInbox = true },
                        onWaitingRoomClick = { onNavigate(Screen.LabWaitingRoom.route) }
                    )
                }

                selectedTab == ProviderTab.Patients -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Patient List Screen", color = primaryText)
                    }
                }

                selectedTab == ProviderTab.Prescription -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Prescription Screen", color = primaryText)
                    }
                }

                selectedTab == ProviderTab.Profile -> {
                    // ✅ FIXED: Passing callbacks down to ProviderProfileScreen
                    ProviderProfileScreen(
                        isDarkMode = isDarkMode,
                        onThemeToggle = onThemeToggle,
                        onEditProfile = onEditProfile,
                        onLogout = onLogout
                    )
                }
            }
        }
    }
}

// --- SUB-COMPOSABLES ---

@Composable
fun ProviderLabResultsContent(
    isDarkMode: Boolean,
    accentTeal: Color,
    onBack: () -> Unit,
    onWritePrescription: () -> Unit
) {
    val primaryText = if (isDarkMode) Color.White else Color.Black
    val cardBg = if (isDarkMode) Color(0xFF121212) else Color.White

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = accentTeal)
            }
            Text("Lab Results Inbox", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = primaryText)
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 20.dp)
        ) {
            items(5) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = cardBg),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Description, contentDescription = null, tint = accentTeal)
                            Spacer(Modifier.width(12.dp))
                            Column(Modifier.weight(1f)) {
                                Text("David John", fontWeight = FontWeight.Bold, color = primaryText)
                                Text("Malaria RDT: Positive (++)", color = accentTeal, fontSize = 12.sp)
                            }
                            Text("Today", color = Color.Gray, fontSize = 11.sp)
                        }
                        Spacer(Modifier.height(16.dp))
                        Button(
                            onClick = onWritePrescription,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = accentTeal),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Write Prescription", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProviderHomeContent(
    isDarkMode: Boolean,
    accentTeal: Color,
    onViewReportsClick: () -> Unit,
    onWaitingRoomClick: () -> Unit
) {
    val primaryText = if (isDarkMode) Color.White else Color.Black
    val secondaryText = if (isDarkMode) Color(0xFFB0B0B0) else Color.Gray

    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(50.dp).background(accentTeal, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("RD", color = Color.White, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Riverside Diagnostics", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = primaryText)
                Text("Medical Provider ID: MF-PRO-99", color = secondaryText, fontSize = 12.sp)
            }
            IconButton(onClick = {}) {
                Icon(Icons.Default.Notifications, contentDescription = null, tint = accentTeal)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
        Text("Facility Overview", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = primaryText)
        Spacer(modifier = Modifier.height(24.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            item { ProviderStatCard("Total Patients", "1,240", Icons.Default.Groups, accentTeal, isDarkMode) }
            item { ProviderStatCard("Pending Review", "8", Icons.Default.MedicalInformation, Color(0xFFFFA000), isDarkMode) }
            item { ProviderStatCard("Completed Labs", "42", Icons.Default.CheckCircle, Color(0xFF43A047), isDarkMode) }
            item { ProviderStatCard("Active Orders", "14", Icons.Default.Science, Color(0xFF1E88E5), isDarkMode) }
        }

        Spacer(modifier = Modifier.weight(1f))
        Text("Active Operations", fontWeight = FontWeight.Bold, color = secondaryText, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onWaitingRoomClick,
            modifier = Modifier.fillMaxWidth().height(60.dp),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.5.dp, accentTeal)
        ) {
            Icon(Icons.Default.HourglassEmpty, contentDescription = null, tint = accentTeal)
            Spacer(modifier = Modifier.width(12.dp))
            Text("Enter Waiting Room", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = primaryText)
        }

        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = onViewReportsClick,
            modifier = Modifier.fillMaxWidth().height(60.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = accentTeal)
        ) {
            Icon(Icons.Default.History, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(12.dp))
            Text("Review All Lab Results", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = Color.White)
        }
    }
}

@Composable
fun ProviderStatCard(title: String, count: String, icon: ImageVector, accentColor: Color, isDarkMode: Boolean) {
    val cardBg = if (isDarkMode) Color(0xFF121212) else Color.White
    val primaryText = if (isDarkMode) Color.White else Color.Black

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(if (isDarkMode) 0.dp else 2.dp),
        border = if (isDarkMode) BorderStroke(1.dp, Color(0xFF222222)) else null
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier.size(40.dp).background(accentColor.copy(alpha = 0.15f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = accentColor)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = title, color = if (isDarkMode) Color.LightGray else Color.Gray, fontSize = 12.sp)
            Text(text = count, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = primaryText)
        }
    }
}

// --- DUAL PREVIEWS ---

@Preview(name = "Dashboard - Light Mode", showBackground = true, showSystemUi = true)
@Composable
fun ProviderDashboardLightPreview() {
    MaterialTheme {
        DashboardServiceProviderScreen(
            isDarkMode = false,
            onThemeToggle = {},
            onEditProfile = {},
            onLogout = {}
        )
    }
}

@Preview(name = "Dashboard - Dark Mode", showBackground = true, showSystemUi = true)
@Composable
fun ProviderDashboardDarkPreview() {
    MaterialTheme {
        Surface(color = Color.Black) {
            DashboardServiceProviderScreen(
                isDarkMode = true,
                onThemeToggle = {},
                onEditProfile = {},
                onLogout = {}
            )
        }
    }
}