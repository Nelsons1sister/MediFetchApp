package com.example.mymedifetchproject.provider

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mymedifetchproject.data.AuthViewModel
import com.example.mymedifetchproject.shared.Screen
import com.example.mymedifetchproject.ui.theme.MyMedifetchProjectTheme

// --- 0. ENUMS ---
enum class ProviderTab(val label: String, val icon: ImageVector) {
    Home("Home", Icons.Filled.Dashboard),
    Patients("Patients", Icons.Filled.People),
    Prescription("Prescribe", Icons.Filled.Assignment),
    Profile("Profile", Icons.Filled.Person)
}

// --- 1. MAIN DASHBOARD SCREEN ---
@Composable
fun DashboardServiceProviderScreen(
    authViewModel: AuthViewModel = viewModel(),
    isDarkMode: Boolean,
    onThemeToggle: (Boolean) -> Unit,
    onNavigate: (String) -> Unit = {},
    onLogout: () -> Unit = {}
) {
    // ✅ PERMANENT FIX: Fetch profile on launch
    LaunchedEffect(Unit) {
        authViewModel.fetchUserProfile()
    }

    // ✅ PERMANENT FIX: Observe real-time profile state
    val profile by authViewModel.userProfile
    val currentUser = authViewModel.currentUser

    // Navigation and UI States
    var selectedTab by remember { mutableStateOf(ProviderTab.Home) }
    var showLabInbox by remember { mutableStateOf(false) }

    // Theme Colors
    val bgColor = if (isDarkMode) Color.Black else Color(0xFFF5F9FF)
    val navBarColor = if (isDarkMode) Color(0xFF1A1A1A) else Color.White
    val accentBlue = if (isDarkMode) Color(0xFF64B5F6) else Color(0xFF0D47A1)

    Scaffold(
        bottomBar = {
            if (!showLabInbox) {
                NavigationBar(containerColor = navBarColor, tonalElevation = 8.dp) {
                    ProviderTab.entries.forEach { tab ->
                        NavigationBarItem(
                            selected = selectedTab == tab,
                            onClick = { selectedTab = tab },
                            label = { Text(tab.label, fontSize = 10.sp) },
                            icon = { Icon(imageVector = tab.icon, contentDescription = tab.label) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = accentBlue,
                                indicatorColor = accentBlue.copy(alpha = 0.1f)
                            )
                        )
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(bgColor)
        ) {
            // ✅ PERMANENT FIX: Shared Header with dynamic Database Data
            if (!showLabInbox && selectedTab != ProviderTab.Profile) {
                SharedProviderHeader(
                    name = profile?.full_name ?: "Medical Staff",
                    role = profile?.role ?: "Verifying...",
                    id = currentUser?.uid?.take(8)?.uppercase() ?: "---",
                    isDarkMode = isDarkMode,
                    accentBlue = accentBlue
                )
            }

            Box(modifier = Modifier.fillMaxSize()) {
                when {
                    showLabInbox -> {
                        ProviderLabReportsScreen(
                            isDarkMode = isDarkMode,
                            onBack = { showLabInbox = false },
                            onAction = { _ ->
                                selectedTab = ProviderTab.Prescription
                                showLabInbox = false
                            }
                        )
                    }

                    selectedTab == ProviderTab.Home -> {
                        ProviderHomeContent(
                            isDarkMode = isDarkMode,
                            accentBlue = accentBlue,
                            onViewReportsClick = { showLabInbox = true },
                            onQuickPrescribe = { selectedTab = ProviderTab.Prescription }
                        )
                    }

                    selectedTab == ProviderTab.Patients -> {
                        ProviderPatientListContent(accentBlue) { id ->
                            onNavigate(Screen.PatientDetail.createRoute(id))
                        }
                    }

                    selectedTab == ProviderTab.Prescription -> {
                        ProviderPrescriptionForm(accentBlue) {
                            selectedTab = ProviderTab.Home
                        }
                    }

                    selectedTab == ProviderTab.Profile -> {
                        ProviderProfileScreen(
                            authViewModel = authViewModel,
                            isDarkMode = isDarkMode,
                            onThemeToggle = onThemeToggle,
                            onEditProfile = { onNavigate(Screen.EditProfile.route) },
                            onLogout = onLogout
                        )
                    }
                }
            }
        }
    }
}

// --- 2. SUPPORTING COMPONENTS ---

@Composable
fun SharedProviderHeader(name: String, role: String, id: String, isDarkMode: Boolean, accentBlue: Color) {
    val cardBg = if (isDarkMode) Color(0xFF1A1A1A) else Color.White
    Card(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(cardBg),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(modifier = Modifier.size(48.dp), shape = CircleShape, color = accentBlue.copy(alpha = 0.1f)) {
                val icon = if (role.contains("LAB", ignoreCase = true)) Icons.Default.Science else Icons.Default.MedicalServices
                Icon(icon, null, tint = accentBlue, modifier = Modifier.padding(8.dp))
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text(name, fontWeight = FontWeight.Bold, color = if(isDarkMode) Color.White else Color.Black)
                Text("${role.replace("_", " ").uppercase()} • STAFF ID: $id", fontSize = 11.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
private fun ProviderHomeContent(
    isDarkMode: Boolean,
    accentBlue: Color,
    onViewReportsClick: () -> Unit,
    onQuickPrescribe: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)) {
        Text("Facility Overview", fontSize = 24.sp, fontWeight = FontWeight.Black, color = accentBlue)
        Spacer(Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { ProviderStatCard("Total Patients", "1,240", Icons.Default.Groups, accentBlue, isDarkMode) }
            item { ProviderStatCard("Pending Reports", "8", Icons.Default.Science, Color(0xFFFFA000), isDarkMode) }
        }

        Spacer(Modifier.weight(1f))

        Button(
            onClick = onQuickPrescribe,
            modifier = Modifier.fillMaxWidth().height(60.dp),
            colors = ButtonDefaults.buttonColors(accentBlue)
        ) {
            Text("NEW PRESCRIPTION", fontWeight = FontWeight.Black)
        }

        Spacer(Modifier.height(12.dp))

        OutlinedButton(
            onClick = onViewReportsClick,
            modifier = Modifier.fillMaxWidth().height(60.dp),
            border = BorderStroke(2.dp, accentBlue)
        ) {
            Text("REVIEW LAB RESULTS", color = accentBlue, fontWeight = FontWeight.Black)
        }
        Spacer(Modifier.height(20.dp))
    }
}

@Composable
fun ProviderStatCard(title: String, count: String, icon: ImageVector, accentColor: Color, isDarkMode: Boolean) {
    Card(
        modifier = Modifier.height(100.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(if (isDarkMode) Color(0xFF1E1E1E) else Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.Center) {
            Icon(icon, null, tint = accentColor, modifier = Modifier.size(20.dp))
            Text(title, fontSize = 12.sp, color = Color.Gray)
            Text(count, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = if (isDarkMode) Color.White else Color.Black)
        }
    }
}

@Composable
private fun ProviderPatientListContent(accentBlue: Color, onPatientClick: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Text("Patient Directory", fontSize = 24.sp, fontWeight = FontWeight.Black, color = accentBlue)
        Card(modifier = Modifier.fillMaxWidth().padding(top = 12.dp).clickable { onPatientClick("1") }) {
            Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AccountCircle, null, tint = accentBlue)
                Spacer(Modifier.width(12.dp))
                Text("John Doe (ID: MF-001)", fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Composable
private fun ProviderPrescriptionForm(accentBlue: Color, onSent: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Text("New Prescription", fontSize = 24.sp, fontWeight = FontWeight.Black, color = accentBlue)
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(value = "", onValueChange = {}, label = { Text("Medication") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = "", onValueChange = {}, label = { Text("Dosage") }, modifier = Modifier.fillMaxWidth())
        Button(onClick = onSent, modifier = Modifier.fillMaxWidth().padding(top = 20.dp), colors = ButtonDefaults.buttonColors(accentBlue)) {
            Text("SEND TO PATIENT")
        }
    }
}

// --- 3. DUAL THEME PREVIEWS ---

@Preview(name = "Light Mode Dashboard", showBackground = true, showSystemUi = true)
@Composable
fun ProviderDashboardLightPreview() {
    MyMedifetchProjectTheme(darkTheme = false) {
        DashboardServiceProviderScreen(isDarkMode = false, onThemeToggle = {})
    }
}

@Preview(name = "Dark Mode Dashboard", showBackground = true, showSystemUi = true)
@Composable
fun ProviderDashboardDarkPreview() {
    MyMedifetchProjectTheme(darkTheme = true) {
        DashboardServiceProviderScreen(isDarkMode = true, onThemeToggle = {})
    }
}