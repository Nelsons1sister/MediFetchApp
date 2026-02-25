package com.example.mymedifetchproject.patient

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mymedifetchproject.Screen

@Composable
fun PatientMainScreen(
    isDarkMode: Boolean,
    onThemeToggle: (Boolean) -> Unit,
    onExternalNavigate: (String) -> Unit,
    currentRoute: String?
) {
    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = if (isDarkMode) androidx.compose.ui.graphics.Color.Black else MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                val navItems = listOf(
                    Triple("Home", Icons.Default.Home, Screen.PatientHome.route),
                    Triple("Labs", Icons.Default.Science, Screen.FindLabs.route),
                    Triple("Reports", Icons.Default.Assessment, Screen.PatientReports.route),
                    Triple("Profile", Icons.Default.Person, Screen.PatientProfile.route)
                )

                navItems.forEach { (label, icon, route) ->
                    val isSelected = currentRoute == route

                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            if (!isSelected) {
                                onExternalNavigate(route)
                            }
                        },
                        label = { Text(label) },
                        icon = { Icon(icon, contentDescription = label) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = if (isDarkMode) androidx.compose.ui.graphics.Color(0xFF4DB6AC) else MaterialTheme.colorScheme.primary,
                            indicatorColor = if (isDarkMode) androidx.compose.ui.graphics.Color(0xFF4DB6AC).copy(alpha = 0.1f) else MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                            unselectedIconColor = if (isDarkMode) androidx.compose.ui.graphics.Color.Gray else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(if (isDarkMode) androidx.compose.ui.graphics.Color.Black else MaterialTheme.colorScheme.background)
        ) {
            when (currentRoute) {
                Screen.PatientHome.route -> {
                    DashboardPatientScreen(
                        onNavigate = { route -> onExternalNavigate(route) },
                        isDarkMode = isDarkMode // ✅ Added
                    )
                }
                Screen.FindLabs.route -> {
                    PatientFindLabScreen(
                        onBack = { onExternalNavigate(Screen.PatientHome.route) },
                        onNavigateToCheckIn = { name, addr ->
                            onExternalNavigate(Screen.LabCheckIn.createRoute(name, addr))
                        },
                        isDarkMode = isDarkMode // ✅ Added
                    )
                }
                Screen.PatientReports.route -> {
                    ReportsScreen(
                        onBack = { onExternalNavigate(Screen.PatientHome.route) },
                        onReportClick = { reportId ->
                            onExternalNavigate(Screen.PatientReportDetail.createRoute(reportId))
                        },
                        isDarkMode = isDarkMode // ✅ Added
                    )
                }
                Screen.PatientProfile.route -> {
                    PatientProfileScreen(
                        isDarkMode = isDarkMode,
                        onThemeToggle = onThemeToggle,
                        onLogout = { onExternalNavigate(Screen.Landing.route) }
                    )
                }
                else -> {
                    DashboardPatientScreen(
                        onNavigate = { route -> onExternalNavigate(route) },
                        isDarkMode = isDarkMode // ✅ Added
                    )
                }
            }
        }
    }
}