package com.example.myapplication.data.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myapplication.presentation.cep.CepScreen
import com.example.myapplication.presentation.cep.CepViewModel
import com.example.myapplication.presentation.login.LoginScreen
import com.example.myapplication.presentation.login.LoginViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    cepViewModel: CepViewModel,
    modifier: Modifier
){
    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ){
        composable(Routes.LOGIN) {
            LoginScreen(loginViewModel, navController, modifier)
        }
        composable(Routes.CEP) {
            CepScreen(cepViewModel, navController, modifier)
        }
    }
}