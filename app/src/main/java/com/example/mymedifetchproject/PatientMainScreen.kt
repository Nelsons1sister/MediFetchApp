package com.example.mymedifetchproject.patient

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mymedifetchproject.Screen

@Composable
fun PatientMainScreen(
    isDarkMode: Boolean,              // ✅ Accepts state from NavGraph
    onThemeToggle: (Boolean) -> Unit, // ✅ Accepts function from NavGraph
    onExternalNavigate: (String) -> Unit
) {
    val internalNavController = rememberNavController()
    val navBackStackEntry by internalNavController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            NavigationBar(
                // Use theme tokens instead of Color.White
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                // --- HOME TAB ---
                NavigationBarItem(
                    selected = currentDestination?.route == Screen.PatientHome.route,
                    onClick = {
                        internalNavController.navigate(Screen.PatientHome.route) {
                            popUpTo(internalNavController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    label = { Text("Home") },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    )
                )

                // --- LABS TAB ---
                NavigationBarItem(
                    selected = currentDestination?.route == Screen.FindLabs.route,
                    onClick = {
                        internalNavController.navigate(Screen.FindLabs.route) {
                            popUpTo(internalNavController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    label = { Text("Labs") },
                    icon = { Icon(Icons.Default.Science, contentDescription = "Labs") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    )
                )

                // --- REPORTS TAB ---
                NavigationBarItem(
                    selected = currentDestination?.route == Screen.PatientReports.route,
                    onClick = {
                        internalNavController.navigate(Screen.PatientReports.route) {
                            popUpTo(internalNavController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    label = { Text("Reports") },
                    icon = { Icon(Icons.Default.Assessment, contentDescription = "Reports") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    )
                )

                // --- PROFILE TAB ---
                NavigationBarItem(
                    selected = currentDestination?.route == Screen.PatientProfile.route,
                    onClick = {
                        internalNavController.navigate(Screen.PatientProfile.route) {
                            popUpTo(internalNavController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    label = { Text("Profile") },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    )
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = internalNavController,
            startDestination = Screen.PatientHome.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.PatientHome.route) {
                DashboardPatientScreen(onNavigate = { route ->
                    if (route == Screen.ReportSickness.route ||
                        route.startsWith("patient_report_detail") ||
                        route == "logout") {
                        onExternalNavigate(route)
                    } else {
                        internalNavController.navigate(route)
                    }
                })
            }

            composable(Screen.FindLabs.route) {
                PatientFindLabScreen(
                    onBack = { internalNavController.popBackStack() },
                    onNavigateToCheckIn = { labName, labAddress ->
                        val checkInRoute = Screen.LabCheckIn.createRoute(labName, labAddress)
                        onExternalNavigate(checkInRoute)
                    }
                )
            }

            composable(Screen.PatientReports.route) {
                ReportsScreen(
                    onBack = { internalNavController.popBackStack() },
                    onReportClick = { reportId ->
                        val detailRoute = Screen.PatientReportDetail.createRoute(reportId)
                        onExternalNavigate(detailRoute)
                    }
                )
            }

            // 👤 Profile Settings - UPDATED TO USE GLOBAL THEME STATE ✅
            composable(Screen.PatientProfile.route) {
                PatientProfileScreen(
                    isDarkMode = isDarkMode,           // Pass global state
                    onThemeToggle = onThemeToggle,     // Pass global function
                    onLogout = { onExternalNavigate("logout") }
                )
            }
        }
    }
}