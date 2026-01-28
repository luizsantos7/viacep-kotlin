package com.example.myapplication

import CepViewModel
import NavGraph
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.presentation.login.LoginViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val loginViewModel: LoginViewModel = viewModel()
            val cepViewModel: CepViewModel = viewModel()

            // Chama a NavGraph definida no seu projeto.
            NavGraph(
                navController = navController,
                loginViewModel = loginViewModel,
                cepViewModel = cepViewModel,
                modifier = Modifier
            )
        }
    }
}
