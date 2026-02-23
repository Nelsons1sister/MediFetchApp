package com.example.mymedifetchproject.provider

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
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
import com.example.mymedifetchproject.ui.theme.MyMedifetchProjectTheme

// --- 1. TAB DEFINITION ---
enum class ProviderTab(val label: String, val icon: ImageVector) {
    Home("Home", Icons.Filled.Dashboard),
    Patients("Patients", Icons.Filled.People),
    Prescription("Prescribe", Icons.Filled.Assignment),
    Profile("Profile", Icons.Filled.Person)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardServiceProviderScreen(
    isDarkMode: Boolean,              // ✅ Accepts state from NavGraph
    onThemeToggle: (Boolean) -> Unit, // ✅ Accepts function from NavGraph
    onNavigate: (String) -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(ProviderTab.Home) }
    var showLabInbox by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                ProviderTab.entries.forEach { tab ->
                    NavigationBarItem(
                        selected = !showLabInbox && selectedTab == tab,
                        onClick = {
                            selectedTab = tab
                            showLabInbox = false
                        },
                        label = { Text(tab.label, fontSize = 10.sp) },
                        icon = { Icon(tab.icon, contentDescription = tab.label) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                        )
                    )
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            if (showLabInbox) {
                ProviderLabReportsScreen(
                    onBack = { showLabInbox = false },
                    onNavigateToPrescribe = { patientId ->
                        onNavigate(Screen.ProviderPrescription.createRoute(patientId))
                    }
                )
            } else {
                when (selectedTab) {
                    ProviderTab.Home -> ProviderHomeContent(
                        onViewReportsClick = { showLabInbox = true },
                        onWaitingRoomClick = { onNavigate(Screen.LabWaitingRoom.route) }
                    )

                    ProviderTab.Patients -> {
                        ProviderPatientListScreen(onPatientClick = { patientId ->
                            onNavigate(Screen.PatientDetail.createRoute(patientId))
                        })
                    }

                    ProviderTab.Prescription -> {
                        ProviderPrescriptionScreen(
                            onBack = { selectedTab = ProviderTab.Home },
                            onPrescriptionSent = { selectedTab = ProviderTab.Home }
                        )
                    }

                    ProviderTab.Profile -> {
                        // ✅ Pass theme logic to the Provider's profile screen
                        ProviderProfileScreen(
                            isDarkMode = isDarkMode,
                            onThemeToggle = onThemeToggle,
                            onLogout = { onNavigate("logout") }
                        )
                    }
                }
            }
        }
    }
}

// --- 2. HOME CONTENT ---
@Composable
fun ProviderHomeContent(
    onViewReportsClick: () -> Unit,
    onWaitingRoomClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(Color(0xFF2C7B76), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("DR", color = Color.White, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Dr. Smith",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Riverside Diagnostics",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 14.sp
                )
            }
            IconButton(onClick = {}) {
                Icon(
                    Icons.Filled.Notifications,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Facility Overview",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(24.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ProviderStatCard(
                    "Patients", "150", Icons.Filled.Groups,
                    Color(0xFF2C7B76).copy(alpha = 0.15f), Color(0xFF2C7B76)
                )
            }
            item {
                ProviderStatCard(
                    "Pending Labs", "8", Icons.Filled.HourglassBottom,
                    Color(0xFF1E88E5).copy(alpha = 0.15f), Color(0xFF1E88E5)
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            "Active Workflows",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onWaitingRoomClick,
            modifier = Modifier.fillMaxWidth().height(60.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF2C7B76)),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2C7B76))
        ) {
            Icon(Icons.Default.Badge, contentDescription = null)
            Spacer(modifier = Modifier.width(12.dp))
            Text("Lab Waiting Room", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onViewReportsClick,
            modifier = Modifier.fillMaxWidth().height(60.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C7B76))
        ) {
            Icon(Icons.Default.Science, contentDescription = null)
            Spacer(modifier = Modifier.width(12.dp))
            Text("Medical Lab Reports", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun ProviderStatCard(title: String, count: String, icon: ImageVector, bgColor: Color, iconColor: Color) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(bgColor, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = iconColor)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = title, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
            Text(
                text = count,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

// --- 3. PREVIEWS ---

@Preview(name = "Light Mode", showBackground = true, showSystemUi = true)
@Composable
fun DashboardServiceProviderLightPreview() {
    MyMedifetchProjectTheme(darkTheme = false) {
        DashboardServiceProviderScreen(isDarkMode = false, onThemeToggle = {})
    }
}

@Preview(name = "Dark Mode", showBackground = true, showSystemUi = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DashboardServiceProviderDarkPreview() {
    MyMedifetchProjectTheme(darkTheme = true) {
        DashboardServiceProviderScreen(isDarkMode = true, onThemeToggle = {})
    }
}