package com.example.mymedifetchproject

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

// --- Shared & Entry ---
import com.example.mymedifetchproject.medifetch.SplashScreen
import com.example.mymedifetchproject.LandingScreen
import com.example.mymedifetchproject.ChooseAccountTypeScreen
import com.example.mymedifetchproject.LoginScreen

// --- Patient & Provider ---
import com.example.mymedifetchproject.patient.*
import com.example.mymedifetchproject.provider.*

@Composable
fun NavGraph(
    navController: NavHostController,
    isDarkMode: Boolean,
    currentRoute: String?,
    onThemeToggle: (Boolean) -> Unit,
    onNavigate: (String) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {

        // --- 0. SPLASH & ENTRY ---
        composable(route = Screen.Splash.route) {
            SplashScreen(onAnimationFinished = {
                navController.navigate(Screen.Landing.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            })
        }

        composable(route = Screen.Landing.route) {
            LandingScreen(onNavigate = { navController.navigate(Screen.AccountSelect.route) })
        }

        composable(route = Screen.AccountSelect.route) {
            ChooseAccountTypeScreen(
                onBack = { navController.popBackStack() },
                onRoleSelected = { role: String ->
                    navController.navigate(Screen.CreateAccount.createRoute(role))
                }
            )
        }

        // ✅ FIXED: Passing isDarkMode to Registration Screens
        composable(
            route = Screen.CreateAccount.route,
            arguments = listOf(navArgument("role") { type = NavType.StringType })
        ) { entry ->
            val role = entry.arguments?.getString("role") ?: "patient"
            if (role == "provider") {
                ProviderRegisterScreen(
                    role = role,
                    isDarkMode = isDarkMode, // Pass theme here
                    onAccountCreated = { navController.navigate(Screen.Login.createRoute(role)) },
                    onBack = { navController.popBackStack() }
                )
            } else {
                PatientRegisterScreen(
                    isDarkMode = isDarkMode, // Pass theme here
                    onAccountCreated = { navController.navigate(Screen.Login.createRoute(role)) },
                    onBack = { navController.popBackStack() }
                )
            }
        }

        // ✅ FIXED: Passing isDarkMode to Login Screen
        composable(
            route = Screen.Login.route,
            arguments = listOf(navArgument("role") { type = NavType.StringType })
        ) { entry ->
            val selectedRole = entry.arguments?.getString("role") ?: "patient"
            LoginScreen(
                role = selectedRole,
                isDarkMode = isDarkMode, // Pass theme here
                onLoginSuccess = {
                    val target = if (selectedRole == "provider") Screen.ProviderDashboard.route
                    else Screen.PatientDashboard.route

                    navController.navigate(target) {
                        popUpTo(Screen.Landing.route) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        // --- 2. AUTHENTICATED ZONE (PATIENT) ---
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
                    onExternalNavigate = onNavigate
                )
            }
        }

        // ✅ Patient Check-In Route
        composable(
            route = Screen.LabCheckIn.route,
            arguments = listOf(
                navArgument("name") { type = NavType.StringType },
                navArgument("address") { type = NavType.StringType }
            )
        ) { entry ->
            val pName = entry.arguments?.getString("name") ?: "Patient"
            val pAddress = entry.arguments?.getString("address") ?: ""

            LabWaitingRoomScreen(
                patientName = pName,
                labUnit = pAddress,
                isDarkMode = isDarkMode,
                onBack = { navController.popBackStack() },
                onConfirmCheckIn = {
                    navController.navigate(Screen.PatientDashboard.route) {
                        popUpTo(Screen.PatientDashboard.route) { inclusive = true }
                    }
                }
            )
        }

        // --- 3. PROVIDER ZONE ---
        composable(route = Screen.ProviderDashboard.route) {
            DashboardServiceProviderScreen(
                isDarkMode = isDarkMode,
                onThemeToggle = onThemeToggle,
                onNavigate = { route: String -> navController.navigate(route) }
            )
        }

        composable(route = Screen.LabWaitingRoom.route) {
            LabWaitingRoomScreen(
                isDarkMode = isDarkMode,
                onBack = { navController.popBackStack() },
                onConfirmCheckIn = {
                    navController.navigate(Screen.ProviderLabInbox.route) {
                        popUpTo(Screen.LabWaitingRoom.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = Screen.ProviderLabInbox.route) {
            ProviderLabReportsScreen(
                isDarkMode = isDarkMode,
                onBack = { navController.popBackStack() },
                onNavigateToPrescribe = { patientId: String ->
                    navController.navigate(Screen.ProviderPrescription.createRoute(patientId))
                }
            )
        }

        composable(
            route = Screen.ProviderPrescription.route,
            arguments = listOf(navArgument("patientId") { type = NavType.StringType })
        ) { entry ->
            val pId = entry.arguments?.getString("patientId") ?: ""
            ProviderPrescriptionScreen(
                isDarkMode = isDarkMode,
                initialPatientId = pId,
                onBack = { navController.popBackStack() },
                onPrescriptionSent = {
                    navController.navigate(Screen.ProviderDashboard.route) {
                        popUpTo(Screen.ProviderDashboard.route) { inclusive = true }
                    }
                }
            )
        }

        // --- 4. SHARED & DETAILS ---
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