package com.example.mymedifetch

// Import your real screens
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mymedifetch.navigation.Screen
import com.example.mymedifetch.ui.theme.MymedifetchTheme

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

    NavHost(navController = navController, startDestination = Screen.Landing.route) {
        composable(Screen.Landing.route) {
            LandingScreen(onNavigate = { navController.navigate(Screen.ChooseAccount.route) })
        }
        composable(Screen.ChooseAccount.route) {
            ChooseAccountTypeScreen(
                onNavigate = { navController.navigate(Screen.Login.route) },
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Login.route) {
            LoginScreen(
                onLogin = { navController.navigate(Screen.Dashboard.route) },
                onCreateAccount = { navController.navigate(Screen.CreateAccount.route) }
            )
        }
        composable(Screen.CreateAccount.route) {
            CreateAccountScreen(
                onNavigateBack = { navController.popBackStack() },
                onAccountCreated = { navController.navigate(Screen.Login.route) }
            )
        }
        composable(Screen.Dashboard.route) {
            DashboardScreen()
        }
    }
}