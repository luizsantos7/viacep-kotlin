
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myapplication.presentation.cep.CepScreen
import com.example.myapplication.presentation.login.LoginScreen
import com.example.myapplication.presentation.login.LoginViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    cepViewModel: CepViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(navController = navController, startDestination = "login", modifier = modifier) {
        composable("login") {
            LoginScreen(viewModel = loginViewModel, navController = navController, modifier = modifier)
        }
        composable("cep") {
            CepScreen(viewModel = cepViewModel, navController = navController, modifier = modifier)
        }
    }
}
