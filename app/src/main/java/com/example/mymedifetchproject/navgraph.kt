package com.example.mymedifetchproject

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

import com.example.mymedifetchproject.data.AuthViewModel
import com.example.mymedifetchproject.medifetch.ChooseAccountTypeScreen
import com.example.mymedifetchproject.medifetch.LandingScreen
import com.example.mymedifetchproject.medifetch.LoginScreen
import com.example.mymedifetchproject.medifetch.SplashScreen
import com.example.mymedifetchproject.patient.*
import com.example.mymedifetchproject.provider.*
import com.example.mymedifetchproject.shared.*

@Composable
fun NavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    isDarkMode: Boolean,
    currentRoute: String?,
    onThemeToggle: (Boolean) -> Unit,
    onNavigate: (String) -> Unit
) {
    val context = LocalContext.current

    // --- SHARED LOGIC: LOGOUT HANDLER ---
    val handleLogout: () -> Unit = {
        authViewModel.logout {
            Toast.makeText(context, "Logout successful!", Toast.LENGTH_SHORT).show()
            navController.navigate(Screen.Landing.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {

        // --- 0. SPLASH & SESSION ---
        composable(route = Screen.Splash.route) {
            SplashScreen(onAnimationFinished = {
                authViewModel.checkUserSession(
                    onSessionFound = { verifiedRole ->
                        val target = when (verifiedRole) {
                            "MEDICAL_PROVIDER", "provider" -> Screen.ProviderDashboard.route
                            "LAB_TECHNICIAN", "labtech" -> Screen.LabTechDashboard.route
                            else -> Screen.PatientDashboard.route
                        }
                        navController.navigate(target) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    },
                    onNoSession = {
                        navController.navigate(Screen.Landing.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }
                )
            })
        }

        // --- 1. AUTHENTICATION FLOW ---
        composable(route = Screen.Landing.route) {
            LandingScreen(onNavigate = { navController.navigate(Screen.AccountSelect.route) })
        }

        composable(route = Screen.AccountSelect.route) {
            ChooseAccountTypeScreen(
                isDarkMode = isDarkMode,
                onBack = { navController.popBackStack() },
                onRoleSelected = { role ->
                    navController.navigate(Screen.Login.createRoute(role))
                }
            )
        }

        composable(
            route = Screen.Login.route,
            arguments = listOf(navArgument("role") { type = NavType.StringType })
        ) { entry ->
            val selectedRole = entry.arguments?.getString("role") ?: "patient"
            LoginScreen(
                role = selectedRole,
                authViewModel = authViewModel,
                isDarkMode = isDarkMode,
                onLoginSuccess = { verifiedRole ->
                    val target = when (verifiedRole) {
                        "MEDICAL_PROVIDER", "provider" -> Screen.ProviderDashboard.route
                        "LAB_TECHNICIAN", "labtech" -> Screen.LabTechDashboard.route
                        else -> Screen.PatientDashboard.route
                    }
                    navController.navigate(target) {
                        popUpTo(Screen.Landing.route) { inclusive = true }
                    }
                },
                onRegister = { navController.navigate(Screen.CreateAccount.createRoute(selectedRole)) },
                onForgotPassword = { navController.navigate(Screen.ForgotPassword.route) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.CreateAccount.route,
            arguments = listOf(navArgument("role") { type = NavType.StringType })
        ) { entry ->
            val role = entry.arguments?.getString("role") ?: "patient"
            if (role == "provider" || role == "labtech" || role == "MEDICAL_PROVIDER" || role == "LAB_TECHNICIAN") {
                ProviderRegisterScreen(
                    role = role, authViewModel = authViewModel, isDarkMode = isDarkMode,
                    onAccountCreated = { navController.navigate(Screen.Login.createRoute(role)) },
                    onNavigateToLogin = { navController.navigate(Screen.Login.createRoute(role)) },
                    onBack = { navController.popBackStack() }
                )
            } else {
                PatientRegisterScreen(
                    authViewModel = authViewModel, isDarkMode = isDarkMode,
                    onAccountCreated = { navController.navigate(Screen.Login.createRoute(role)) },
                    onNavigateToLogin = { navController.navigate(Screen.Login.createRoute("patient")) },
                    onBack = { navController.popBackStack() }
                )
            }
        }

        // --- 2. PATIENT LOOP (UPDATED TO FIX ERRORS) ---
        composable(route = Screen.PatientDashboard.route) {
            PatientMainScreen(
                isDarkMode = isDarkMode,
                onThemeToggle = { onThemeToggle(!isDarkMode) },
                onLogout = handleLogout
            )
        }

        // --- 3. SERVICE PROVIDER (DOCTOR) LOOP ---
        composable(route = Screen.ProviderDashboard.route) {
            DashboardServiceProviderScreen(
                authViewModel = authViewModel,
                isDarkMode = isDarkMode,
                onThemeToggle = onThemeToggle,
                onNavigate = { targetRoute -> navController.navigate(targetRoute) },
                onLogout = handleLogout
            )
        }

        // --- 4. LAB TECHNICIAN LOOP ---
        composable(route = Screen.LabTechDashboard.route) {
            LabTechDashboardScreen(
                isDarkMode = isDarkMode,
                onThemeToggle = onThemeToggle,
                onLogout = handleLogout,
                onEditProfile = { navController.navigate(Screen.EditProfile.route) }
            )
        }

        // --- 5. SHARED ROUTES ---
        composable(route = Screen.EditProfile.route) {
            EditProfileScreen(
                authViewModel = authViewModel,
                isDarkMode = isDarkMode,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.PatientDetail.route,
            arguments = listOf(navArgument("patientId") { type = NavType.StringType })
        ) { entry ->
            val pId = entry.arguments?.getString("patientId") ?: "Unknown"
            ProviderPatientDetailScreen(
                patientId = pId,
                isDarkMode = isDarkMode,
                onBack = { navController.popBackStack() },
                onOrderLab = {
                    navController.popBackStack(Screen.ProviderDashboard.route, inclusive = false)
                }
            )
        }

        composable(route = Screen.ProviderLabInbox.route) {
            ProviderLabReportsScreen(
                isDarkMode = isDarkMode,
                onBack = { navController.popBackStack() },
                onAction = { navController.popBackStack() }
            )
        }

        composable(route = Screen.ForgotPassword.route) {
            ForgotPasswordScreen(onBack = { navController.popBackStack() }, isDarkMode = isDarkMode)
        }
    }
}