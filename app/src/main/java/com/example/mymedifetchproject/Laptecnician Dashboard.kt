package com.example.mymedifetchproject

import com.example.mymedifetchproject.provider.SharedProviderHeader
import com.example.mymedifetchproject.provider.ProviderProfileScreen
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mymedifetchproject.data.AuthViewModel
import com.example.mymedifetchproject.ui.theme.MyMedifetchProjectTheme

// --- 1. DATA MODEL FOR LAB ---
data class LabQueuePatient(
    val id: String,
    val name: String,
    val testType: String,
    val status: String,
    val timeCheckedIn: String
)

@Composable
fun LabTechDashboardScreen(
    authViewModel: AuthViewModel = viewModel(),
    isDarkMode: Boolean,
    onThemeToggle: (Boolean) -> Unit,
    onEditProfile: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    // ✅ EMBEDDED LOGIC: Fetch and observe database profile
    LaunchedEffect(Unit) {
        authViewModel.fetchUserProfile()
    }

    val profile by authViewModel.userProfile
    val currentUser = authViewModel.currentUser

    var selectedTab by remember { mutableIntStateOf(0) }
    val accentBlue = if (isDarkMode) Color(0xFF64B5F6) else Color(0xFF0D47A1)
    val bgColor = if (isDarkMode) Color.Black else Color(0xFFF5F9FF)
    val cardBg = if (isDarkMode) Color(0xFF1A1A1A) else Color.White

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = cardBg, tonalElevation = 8.dp) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    label = { Text("Queue", fontSize = 11.sp) },
                    icon = { Icon(Icons.Default.PendingActions, null) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = accentBlue,
                        indicatorColor = accentBlue.copy(alpha = 0.1f)
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    label = { Text("Workspace", fontSize = 11.sp) },
                    icon = { Icon(Icons.Default.Science, null) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = accentBlue,
                        indicatorColor = accentBlue.copy(alpha = 0.1f)
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    label = { Text("Profile", fontSize = 11.sp) },
                    icon = { Icon(Icons.Default.Person, null) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = accentBlue,
                        indicatorColor = accentBlue.copy(alpha = 0.1f)
                    )
                )
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().background(bgColor)) {

            // ✅ EMBEDDED LOGIC: Shared Header now uses dynamic Database Data
            if (selectedTab != 2) {
                SharedProviderHeader(
                    name = profile?.full_name ?: "Lab Staff",
                    role = profile?.role ?: "Lab Technician",
                    id = currentUser?.uid?.take(8)?.uppercase() ?: "---",
                    isDarkMode = isDarkMode,
                    accentBlue = accentBlue
                )
            }

            when (selectedTab) {
                0 -> LabQueueSection(isDarkMode, accentBlue) {
                    selectedTab = 1
                }
                1 -> LabWorkspaceSection(isDarkMode, accentBlue) {
                    selectedTab = 0
                }
                2 -> ProviderProfileScreen(
                    authViewModel = authViewModel,
                    isDarkMode = isDarkMode,
                    onThemeToggle = onThemeToggle,
                    onEditProfile = onEditProfile,
                    onLogout = onLogout
                )
            }
        }
    }
}

@Composable
fun LabQueueSection(isDarkMode: Boolean, accentBlue: Color, onStartTest: (LabQueuePatient) -> Unit) {
    val patients = remember {
        mutableStateListOf(
            LabQueuePatient("1", "David John", "Malaria RDT", "Waiting", "10:05 AM"),
            LabQueuePatient("2", "Sarah Smith", "Blood Sugar", "Waiting", "10:15 AM"),
            LabQueuePatient("3", "Michael Obi", "Cholesterol", "Waiting", "10:30 AM")
        )
    }

    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Patient Queue", fontSize = 22.sp, fontWeight = FontWeight.Black, color = accentBlue)
            Spacer(Modifier.weight(1f))
            Badge(containerColor = accentBlue) { Text("${patients.size}", color = Color.White) }
        }
        Text("Patients awaiting diagnostics", fontSize = 12.sp, color = Color.Gray)

        Spacer(Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 20.dp)
        ) {
            items(patients) { patient ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(if(isDarkMode) Color(0xFF1A1A1A) else Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(Modifier.size(8.dp).background(Color.Yellow, CircleShape))
                                Spacer(Modifier.width(8.dp))
                                Text(patient.name, fontWeight = FontWeight.Bold, color = if(isDarkMode) Color.White else Color.Black)
                            }
                            Text(patient.testType, color = accentBlue, fontSize = 13.sp)
                            Text("Arrived: ${patient.timeCheckedIn}", fontSize = 11.sp, color = Color.Gray)
                        }

                        Button(
                            onClick = { onStartTest(patient) },
                            contentPadding = PaddingValues(horizontal = 12.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(accentBlue)
                        ) {
                            Text("START TEST", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LabWorkspaceSection(isDarkMode: Boolean, accentBlue: Color, onResultsSent: () -> Unit) {
    var testFindings by remember { mutableStateOf("") }
    var showConfirmation by remember { mutableStateOf(false) }

    if (showConfirmation) {
        AlertDialog(
            onDismissRequest = { showConfirmation = false },
            title = { Text("Confirm Transmission") },
            text = { Text("Are you sure you want to send these results to the requesting Doctor?") },
            confirmButton = {
                Button(
                    onClick = {
                        showConfirmation = false
                        onResultsSent()
                    },
                    colors = ButtonDefaults.buttonColors(accentBlue)
                ) {
                    Text("Yes, Send")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmation = false }) {
                    Text("Cancel", color = if(isDarkMode) Color.White else Color.Black)
                }
            },
            shape = RoundedCornerShape(20.dp)
        )
    }

    Column(modifier = Modifier.padding(20.dp)) {
        Text("Results Workspace", fontSize = 22.sp, fontWeight = FontWeight.Black, color = accentBlue)
        Text("Active diagnostic session", fontSize = 12.sp, color = Color.Gray)

        Spacer(Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(if(isDarkMode) Color(0xFF1E1E1E) else Color.White)
        ) {
            Column(Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Science, null, tint = accentBlue)
                    Spacer(Modifier.width(8.dp))
                    Text("ACTIVE TEST", color = accentBlue, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }

                Spacer(Modifier.height(8.dp))
                Text("Patient: David John", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text("Investigation: Malaria RDT", fontSize = 14.sp, color = Color.Gray)

                Spacer(Modifier.height(20.dp))

                OutlinedTextField(
                    value = testFindings,
                    onValueChange = { testFindings = it },
                    placeholder = { Text("Enter detailed findings (e.g. Parasites seen ++)") },
                    modifier = Modifier.fillMaxWidth().height(120.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = accentBlue,
                        unfocusedBorderColor = Color.Gray
                    )
                )

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (testFindings.isNotBlank()) {
                            showConfirmation = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(accentBlue),
                    shape = RoundedCornerShape(12.dp),
                    enabled = testFindings.isNotBlank()
                ) {
                    Icon(Icons.Default.CloudUpload, null)
                    Spacer(Modifier.width(8.dp))
                    Text("SEND TO DOCTOR", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// --- 4. PREVIEWS ---
@Preview(name = "LabTech - Light", showBackground = true, showSystemUi = true)
@Composable
fun PreviewLabLight() {
    MyMedifetchProjectTheme(darkTheme = false) {
        // Mocked ViewModel logic for preview purposes
        LabTechDashboardScreen(isDarkMode = false, onThemeToggle = {})
    }
}

@Preview(name = "LabTech - Dark", showBackground = true, showSystemUi = true)
@Composable
fun PreviewLabDark() {
    MyMedifetchProjectTheme(darkTheme = true) {
        LabTechDashboardScreen(isDarkMode = true, onThemeToggle = {})
    }
}