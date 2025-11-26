import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mymedifetch.ChooseAccountTypeScreen
import com.example.mymedifetch.LandingScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "landing"
    ) {
        composable("landing") {
            LandingScreen(
                onNavigate = { navController.navigate("choose_account") }
            )
        }

        composable(route = "choose_account") {
            ChooseAccountTypeScreen()
        }
    }
}
