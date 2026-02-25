package com.example.mymedifetchproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mymedifetchproject.ui.theme.MyMedifetchProjectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enables edge-to-edge for modern look (status bar transparency)
        enableEdgeToEdge()

        setContent {
            // 1. THEME STATE MANAGEMENT
            val systemInDark = isSystemInDarkTheme()
            var isDarkMode by remember { mutableStateOf(systemInDark) }

            // 2. NAVIGATION STATE MANAGEMENT
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()

            // Extracts current route to sync the Bottom Navigation UI
            val currentRoute = navBackStackEntry?.destination?.route

            MyMedifetchProjectTheme(darkTheme = isDarkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavGraph(
                        navController = navController,
                        isDarkMode = isDarkMode,
                        currentRoute = currentRoute,
                        // Passed to Profile screen via NavGraph
                        onThemeToggle = { newState -> isDarkMode = newState },
                        onNavigate = { route ->
                            // Prevent redundant navigation to the same screen
                            if (route != currentRoute) {

                                // 🔒 LOGOUT LOGIC CHECK
                                // If navigating back to Landing/Login, we clear the stack
                                val isLoggingOut = route == Screen.Landing.route ||
                                        route == Screen.Login.route

                                navController.navigate(route) {
                                    if (isLoggingOut) {
                                        // Pop everything up to the very root (0)
                                        // This prevents the user from clicking 'Back' into a profile
                                        popUpTo(0) { inclusive = true }
                                    } else {
                                        // STANDARD NAVIGATION (Tab Switching)
                                        navController.graph.startDestinationRoute?.let { startRoute ->
                                            popUpTo(startRoute) {
                                                saveState = true
                                            }
                                        }
                                        // Avoid multiple copies of the same screen on top
                                        launchSingleTop = true
                                        // Re-use state when clicking back to a previous tab
                                        restoreState = true
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}