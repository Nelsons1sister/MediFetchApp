package com.example.mymedifetchproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mymedifetchproject.data.AuthViewModel
import com.example.mymedifetchproject.ui.theme.MyMedifetchProjectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            // 1. INITIALIZE SHARED AUTH VIEWMODEL
            val authViewModel: AuthViewModel = viewModel()

            // 2. THEME STATE (Persistent across screens)
            val systemInDark = isSystemInDarkTheme()
            var isDarkMode by remember { mutableStateOf(systemInDark) }

            // 3. NAVIGATION CONTROLLER
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            // 4. AUTO-LOGIN / SESSION CHECK LOGIC
            // Runs once on App Launch
            LaunchedEffect(Unit) {
                authViewModel.checkUserSession(
                    onSessionFound = { role ->
                        // Navigate to the specific Dashboard based on Supabase 'user_type'
                        val destination = if (role == "provider") {
                            Screen.ProviderDashboard.route
                        } else {
                            Screen.PatientDashboard.route
                        }

                        navController.navigate(destination) {
                            // Pop up to the very start to prevent user from going back to Landing
                            popUpTo(Screen.Landing.route) { inclusive = true }
                        }
                    },
                    onNoSession = {
                        // If no session, user stays on the Landing Screen defined in NavGraph
                    }
                )
            }

            MyMedifetchProjectTheme(darkTheme = isDarkMode) {
                val isLoading by authViewModel.isLoading
                val bgColor = if (isDarkMode) Color.Black else Color(0xFFF8FBFB)
                val accentTeal = if (isDarkMode) Color(0xFF4DB6AC) else Color(0xFF2C7B76)

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = bgColor
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        // THE NAVIGATION HOST
                        NavGraph(
                            navController = navController,
                            authViewModel = authViewModel,
                            isDarkMode = isDarkMode,
                            currentRoute = currentRoute,
                            onThemeToggle = { newState -> isDarkMode = newState },
                            onNavigate = { route ->
                                if (route != currentRoute) {
                                    val isLoggingOut = route == Screen.Landing.route ||
                                            route == Screen.Login.route

                                    navController.navigate(route) {
                                        if (isLoggingOut) {
                                            // Secure clear of all previous screens on Logout
                                            popUpTo(0) { inclusive = true }
                                        } else {
                                            navController.graph.startDestinationRoute?.let { startRoute ->
                                                popUpTo(startRoute) { saveState = true }
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                }
                            }
                        )

                        // 5. GLOBAL LOADING OVERLAY (Splash Screen)
                        // Shows while checking session or performing Auth actions
                        if (isLoading && currentRoute == null) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(bgColor),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    color = accentTeal,
                                    modifier = Modifier.size(50.dp),
                                    strokeWidth = 4.dp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}