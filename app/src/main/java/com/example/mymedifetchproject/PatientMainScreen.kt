package com.example.mymedifetchproject.patient

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mymedifetchproject.shared.EditProfileScreen
import com.example.mymedifetchproject.shared.Screen

@Composable
fun PatientMainScreen(
    isDarkMode: Boolean,
    onLogout: () -> Unit,
    onThemeToggle: () -> Unit // Added to match your error logs
) {
    // --- 1. NAVIGATION STATE ---
    var currentRoute by remember { mutableStateOf(Screen.PatientDashboard.route) }

    // --- 2. SHARED DATA STATE ---
    var selectedLabName by remember { mutableStateOf("") }
    var selectedLabAddress by remember { mutableStateOf("") }
    var selectedReportId by remember { mutableStateOf("") }

    // --- 3. UI LOGIC: HIDE BOTTOM BAR ON SUB-SCREENS ---
    val showBottomBar = currentRoute in listOf(
        Screen.PatientDashboard.route,
        Screen.PatientReports.route,
        Screen.PatientProfile.route
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                PatientBottomNavBar(
                    currentRoute = currentRoute,
                    onTabSelected = { newRoute -> currentRoute = newRoute }
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (currentRoute) {

                Screen.PatientDashboard.route -> {
                    DashboardPatientScreen(
                        isDarkMode = isDarkMode,
                        onNavigate = { route -> currentRoute = route }
                    )
                }

                Screen.ReportSickness.route -> {
                    ReportSicknessScreen(
                        isDarkMode = isDarkMode,
                        onBack = { currentRoute = Screen.PatientDashboard.route },
                        onSubmitted = { currentRoute = Screen.PatientDashboard.route }
                    )
                }

                Screen.FindLabs.route -> {
                    PatientFindLabScreen(
                        isDarkMode = isDarkMode,
                        onBack = { currentRoute = Screen.PatientDashboard.route },
                        onNavigateToCheckIn = { name, address ->
                            selectedLabName = name
                            selectedLabAddress = address
                            currentRoute = "lab_checkin_detail"
                        }
                    )
                }

                "lab_checkin_detail" -> {
                    LabCheckInScreen(
                        labName = selectedLabName,
                        labAddress = selectedLabAddress,
                        requestedTests = "Malaria (RDT), Typhoid (Widal)",
                        isDarkMode = isDarkMode,
                        onConfirm = { currentRoute = Screen.PatientDashboard.route },
                        onCancel = { currentRoute = Screen.FindLabs.route }
                    )
                }

                Screen.PatientReports.route -> {
                    ReportsScreen(
                        isDarkMode = isDarkMode,
                        onBack = { currentRoute = Screen.PatientDashboard.route },
                        onReportClick = { reportId ->
                            selectedReportId = reportId
                            currentRoute = "report_detail_view"
                        }
                    )
                }

                "report_detail_view" -> {
                    PatientReportDetailScreen(
                        reportId = selectedReportId,
                        isDarkMode = isDarkMode,
                        onBack = { currentRoute = Screen.PatientReports.route }
                    )
                }

                Screen.PatientProfile.route -> {
                    // Update your PatientProfileScreen to accept these 4 params
                    PatientProfileScreen(
                        isDarkMode = isDarkMode,
                        onEditProfile = { currentRoute = Screen.EditProfile.route },
                        onLogout = onLogout,
                        onThemeToggle = onThemeToggle
                    )
                }

                Screen.EditProfile.route -> {
                    // Make sure EditProfileScreen does NOT have an 'onSave' param
                    // if it just uses 'onBack' to return.
                    EditProfileScreen(
                        isDarkMode = isDarkMode,
                        onBack = { currentRoute = Screen.PatientProfile.route }
                    )
                }
            }
        }
    }
}

@Composable
fun PatientBottomNavBar(
    currentRoute: String,
    onTabSelected: (String) -> Unit
) {
    NavigationBar(
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = currentRoute == Screen.PatientDashboard.route,
            onClick = { onTabSelected(Screen.PatientDashboard.route) },
            label = { Text("Home") },
            icon = { Icon(Icons.Default.Home, contentDescription = null) }
        )
        NavigationBarItem(
            selected = currentRoute == Screen.PatientReports.route,
            onClick = { onTabSelected(Screen.PatientReports.route) },
            label = { Text("Reports") },
            icon = { Icon(Icons.Default.Assignment, contentDescription = null) }
        )
        NavigationBarItem(
            selected = currentRoute == Screen.PatientProfile.route,
            onClick = { onTabSelected(Screen.PatientProfile.route) },
            label = { Text("Profile") },
            icon = { Icon(Icons.Default.Person, contentDescription = null) }
        )
    }
}