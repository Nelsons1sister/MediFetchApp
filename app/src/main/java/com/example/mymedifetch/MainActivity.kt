package com.example.mymedifetch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mymedifetch.navigation.Screen
import com.example.mymedifetch.ui.theme.MymedifetchTheme

// Import your real screens
import com.example.mymedifetch.LandingScreen
import com.example.mymedifetch.ChooseAccountTypeScreen
import com.example.mymedifetch.LoginScreen
import com.example.mymedifetch.CreateAccountScreen
import com.example.mymedifetch.DashboardScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MymedifetchTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Landing.route
    ) {

        // Landing Screen
        composable(Screen.Landing.route) {
            LandingScreen(
                onNavigate = { navController.navigate(Screen.ChooseAccount.route) },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Choose Account Screen
        composable(Screen.ChooseAccount.route) {
            ChooseAccountTypeScreen(
                onNavigate = { navController.navigate(Screen.Login.route) },
                onBack = { navController.popBackStack() }
            )
        }

        // Login Screen
        composable(Screen.Login.route) {
            LoginScreen(
                onLogin = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onCreateAccount = { navController.navigate(Screen.CreateAccount.route) },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Create Account Screen
        composable(Screen.CreateAccount.route) {
            CreateAccountScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

        // Dashboard Screen
//        composable(Screen.Dashboard.route) {
//            DashboardScreen(
//                onMyReports = { navController.navigate(Screen.Reports.route) },
//                onFindHospitals = { navController.navigate(Screen.Hospitals.route) },
//                onPrescriptions = { navController.navigate(Screen.Prescriptions.route) },
//                onConsultDoctor = { navController.navigate(Screen.ConsultDoctor.route) }
//            )
//        }

//        // Reports Screen
//        composable(Screen.Reports.route) {
//            PlaceholderScreen("Reports Screen")
//        }
//
//        // Hospitals Screen
//        composable(Screen.Hospitals.route) {
//            PlaceholderScreen("Hospitals Screen")
//        }
//
//        // Prescriptions Screen
//        composable(Screen.Prescriptions.route) {
//            PlaceholderScreen("Prescriptions Screen")
//        }
//
//        // Consult Doctor Screen
//        composable(Screen.ConsultDoctor.route) {
//            PlaceholderScreen("Consult Doctor Screen")
//        }
//    }
//}

// Simple placeholder composable for screens not implemented yet
//@Composable
//fun PlaceholderScreen(title: String) {
//    Text(text = title)
//}

//@Preview(showBackground = true)
//@Composable
//fun PreviewAppNavigation() {
//    MymedifetchTheme {
//        AppNavigation()
//    }
//}
