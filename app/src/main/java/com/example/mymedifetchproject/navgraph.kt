package com.example.mymedifetchproject

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

// --- Splash & Shared ---
import com.example.mymedifetchproject.medifetch.SplashScreen
import com.example.mymedifetchproject.LandingScreen
import com.example.mymedifetchproject.ChooseAccountTypeScreen
import com.example.mymedifetchproject.LoginScreen

// --- Patient Screens ---
import com.example.mymedifetchproject.patient.*

// --- Provider Screens ---
import com.example.mymedifetchproject.provider.*

@Composable
fun NavGraph(
    isDarkMode: Boolean,      // ✅ New parameter
    onThemeToggle: (Boolean) -> Unit // ✅ New parameter
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {

        // --- 0. SPLASH SCREEN ---
        composable(route = Screen.Splash.route) {
            SplashScreen(onAnimationFinished = {
                navController.navigate(Screen.Landing.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            })
        }

        // --- 1. LANDING & ACCOUNT SELECTION ---
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

        // --- 2. REGISTRATION GATEWAY ---
        composable(
            route = Screen.CreateAccount.route,
            arguments = listOf(navArgument("role") { type = NavType.StringType })
        ) { entry: NavBackStackEntry ->
            val role = entry.arguments?.getString("role") ?: "patient"
            if (role == "provider") {
                ProviderRegisterScreen(
                    role = role,
                    onAccountCreated = { navController.navigate(Screen.Login.createRoute(role)) },
                    onBack = { navController.popBackStack() }
                )
            } else {
                PatientRegisterScreen(
                    onAccountCreated = { navController.navigate(Screen.Login.createRoute(role)) },
                    onBack = { navController.popBackStack() }
                )
            }
        }

        // --- 3. LOGIN ---
        composable(
            route = Screen.Login.route,
            arguments = listOf(navArgument("role") { type = NavType.StringType })
        ) { entry: NavBackStackEntry ->
            val selectedRole = entry.arguments?.getString("role") ?: "patient"
            LoginScreen(
                role = selectedRole,
                onLoginSuccess = {
                    val target = if (selectedRole == "provider") Screen.ProviderDashboard.route
                    else Screen.PatientDashboard.route

                    navController.navigate(target) {
                        popUpTo(Screen.Landing.route) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() },
                onForgotPassword = {
                    val route = if(selectedRole == "provider") Screen.ForgotPasswordProvider.route
                    else Screen.ForgotPasswordPatient.route
                    navController.navigate(route)
                }
            )
        }

        // --- 4. DASHBOARDS (Passing theme logic down) ---
        composable(route = Screen.PatientDashboard.route) {
            PatientMainScreen(
                isDarkMode = isDarkMode,             // ✅ Pass down
                onThemeToggle = onThemeToggle,       // ✅ Pass down
                onExternalNavigate = { route: String ->
                    if (route == "logout") {
                        navController.navigate(Screen.Landing.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    } else {
                        navController.navigate(route)
                    }
                }
            )
        }

        composable(route = Screen.ProviderDashboard.route) {
            DashboardServiceProviderScreen(
                isDarkMode = isDarkMode,             // ✅ Pass down
                onThemeToggle = onThemeToggle,       // ✅ Pass down
                onNavigate = { route: String ->
                    if (route == "logout") {
                        navController.navigate(Screen.Landing.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    } else {
                        navController.navigate(route)
                    }
                }
            )
        }

        // --- 5. PROVIDER LOOP SCREENS ---
        composable(
            route = Screen.PatientDetail.route,
            arguments = listOf(navArgument("patientId") { type = NavType.StringType })
        ) { entry ->
            val pId = entry.arguments?.getString("patientId") ?: "Unknown"
            ProviderPatientDetailScreen(
                patientId = pId,
                onBack = { navController.popBackStack() },
                onOrderLab = {
                    navController.popBackStack(Screen.ProviderDashboard.route, inclusive = false)
                }
            )
        }

        composable(route = Screen.LabWaitingRoom.route) {
            LabWaitingRoomScreen(
                onBack = { navController.popBackStack() },
                onStartTest = { cId: String ->
                    navController.navigate(Screen.LabUpload.createRoute(cId))
                }
            )
        }

        composable(route = Screen.ProviderLabInbox.route) {
            ProviderLabReportsScreen(
                onBack = { navController.popBackStack() },
                onNavigateToPrescribe = { pId: String ->
                    navController.navigate(Screen.ProviderPrescription.createRoute(pId))
                }
            )
        }

        composable(
            route = Screen.ProviderPrescription.route,
            arguments = listOf(navArgument("patientId") { type = NavType.StringType })
        ) { entry: NavBackStackEntry ->
            val pId = entry.arguments?.getString("patientId") ?: ""
            ProviderPrescriptionScreen(
                initialPatientId = pId,
                onBack = { navController.popBackStack() },
                onPrescriptionSent = {
                    navController.navigate(Screen.ProviderDashboard.route) {
                        popUpTo(Screen.ProviderDashboard.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Screen.LabUpload.route,
            arguments = listOf(navArgument("caseId") { type = NavType.StringType })
        ) { entry: NavBackStackEntry ->
            val cId = entry.arguments?.getString("caseId") ?: "Unknown"
            UploadResultScreen(
                caseId = cId,
                onBack = { navController.popBackStack() },
                onUploadComplete = {
                    navController.popBackStack(Screen.ProviderDashboard.route, inclusive = false)
                }
            )
        }

        // --- 6. PATIENT LOOP SCREENS ---
        composable(
            route = Screen.LabCheckIn.route,
            arguments = listOf(
                navArgument("name") { type = NavType.StringType },
                navArgument("address") { type = NavType.StringType }
            )
        ) { entry ->
            val name = entry.arguments?.getString("name") ?: "Unknown Lab"
            val addr = entry.arguments?.getString("address") ?: "No Address Provided"
            val mockTests = "Malaria (RDT), Typhoid (Widal)"

            LabCheckInScreen(
                labName = name,
                labAddress = addr,
                requestedTests = mockTests,
                onConfirm = {
                    navController.navigate(Screen.PatientDashboard.route) {
                        popUpTo(Screen.PatientDashboard.route) { inclusive = true }
                    }
                },
                onCancel = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.PatientReportDetail.route,
            arguments = listOf(navArgument("reportId") { type = NavType.StringType })
        ) { entry ->
            val rId = entry.arguments?.getString("reportId") ?: ""
            PatientReportDetailScreen(
                reportId = rId,
                onBack = { navController.popBackStack() }
            )
        }

        composable(route = Screen.FindLabs.route) {
            PatientFindLabScreen(
                onBack = { navController.popBackStack() },
                onNavigateToCheckIn = { name: String, addr: String ->
                    navController.navigate(Screen.LabCheckIn.createRoute(name, addr))
                }
            )
        }

        composable(route = Screen.ReportSickness.route) {
            ReportSicknessScreen(
                onBack = { navController.popBackStack() },
                onSubmitted = { navController.popBackStack() },
                onNavigate = { route: String -> navController.navigate(route) }
            )
        }
    }
}