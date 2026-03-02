package com.example.mymedifetchproject

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

import com.example.mymedifetchproject.data.AuthViewModel
import com.example.mymedifetchproject.medifetch.SplashScreen
import com.example.mymedifetchproject.patient.*
import com.example.mymedifetchproject.provider.*
import com.example.mymedifetchproject.shared.* @Composable
fun NavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    isDarkMode: Boolean,
    currentRoute: String?,
    onThemeToggle: (Boolean) -> Unit,
    onNavigate: (String) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {

        // --- 0. SPLASH & SESSION ---
        composable(route = Screen.Splash.route) {
            SplashScreen(onAnimationFinished = {
                authViewModel.checkUserSession(
                    onSessionFound = { verifiedRole ->
                        // Routes to appropriate dashboard based on Supabase role
                        val target = if (verifiedRole == "provider") Screen.ProviderDashboard.route
                        else Screen.PatientDashboard.route
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

        composable(route = Screen.Landing.route) {
            LandingScreen(onNavigate = { navController.navigate(Screen.AccountSelect.route) })
        }

        composable(route = Screen.AccountSelect.route) {
            ChooseAccountTypeScreen(
                onBack = { navController.popBackStack() },
                onRoleSelected = { role ->
                    navController.navigate(Screen.CreateAccount.createRoute(role))
                }
            )
        }

        // --- 1. AUTHENTICATION ---
        composable(
            route = Screen.CreateAccount.route,
            arguments = listOf(navArgument("role") { type = NavType.StringType })
        ) { entry ->
            val role = entry.arguments?.getString("role") ?: "patient"
            if (role == "provider") {
                ProviderRegisterScreen(
                    role = role,
                    authViewModel = authViewModel,
                    isDarkMode = isDarkMode,
                    onAccountCreated = { navController.navigate(Screen.Login.createRoute(role)) },
                    onNavigateToLogin = { navController.navigate(Screen.Login.createRoute("provider")) },
                    onBack = { navController.popBackStack() }
                )
            } else {
                PatientRegisterScreen(
                    authViewModel = authViewModel,
                    isDarkMode = isDarkMode,
                    onAccountCreated = { navController.navigate(Screen.Login.createRoute(role)) },
                    onNavigateToLogin = { navController.navigate(Screen.Login.createRoute("patient")) },
                    onBack = { navController.popBackStack() }
                )
            }
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
                    val target = if (verifiedRole == "provider") Screen.ProviderDashboard.route
                    else Screen.PatientDashboard.route
                    navController.navigate(target) {
                        popUpTo(Screen.Landing.route) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        // --- 2. PATIENT ZONE ---
        val patientMainTabs = listOf(
            Screen.PatientDashboard.route,
            Screen.FindLabs.route,
            Screen.PatientReports.route,
            Screen.PatientProfile.route
        )

        patientMainTabs.forEach { route ->
            composable(route = route) {
                PatientMainScreen(
                    isDarkMode = isDarkMode,
                    onThemeToggle = onThemeToggle,
                    currentRoute = currentRoute,
                    onExternalNavigate = { target -> navController.navigate(target) }
                )
            }
        }

        // --- 3. PROVIDER ZONE ---
        composable(route = Screen.ProviderDashboard.route) {
            DashboardServiceProviderScreen(
                isDarkMode = isDarkMode,
                onThemeToggle = onThemeToggle,
                onNavigate = { targetRoute -> navController.navigate(targetRoute) },
                // ✅ FIXED: This triggers the navigation to the Edit Profile screen
                onEditProfile = {
                    navController.navigate(Screen.EditProfile.route)
                },
                onLogout = {
                    authViewModel.logout {
                        navController.navigate(Screen.Landing.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }
            )
        }

        composable(route = Screen.LabWaitingRoom.route) {
            LabWaitingRoomScreen(
                isDarkMode = isDarkMode,
                onBack = { navController.popBackStack() },
                onConfirmCheckIn = { navController.popBackStack() }
            )
        }

        // --- 4. SHARED & DETAILS ---
        composable(route = Screen.EditProfile.route) {
            // Uses the shared EditProfilePatientScreen as requested
            EditProfilePatientScreen(
                authViewModel = authViewModel,
                isDarkMode = isDarkMode,
                onBack = { navController.popBackStack() }
            )
        }

        composable(route = Screen.ReportSickness.route) {
            ReportSicknessScreen(
                onBack = { navController.popBackStack() },
                onSubmitted = { navController.popBackStack() },
                isDarkMode = isDarkMode
            )
        }

        composable(
            route = Screen.PatientReportDetail.route,
            arguments = listOf(navArgument("reportId") { type = NavType.StringType })
        ) { entry ->
            val rId = entry.arguments?.getString("reportId") ?: ""
            PatientReportDetailScreen(
                reportId = rId,
                onBack = { navController.popBackStack() },
                isDarkMode = isDarkMode
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
    }
}