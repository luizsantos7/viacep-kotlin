package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.data.navigation.NavGraph
import com.example.myapplication.presentation.cep.CepScreen
import com.example.myapplication.presentation.cep.CepViewModel
import com.example.myapplication.presentation.login.LoginViewModel
import com.example.myapplication.presentation.user.UserScreen
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    val navController = rememberNavController()
                    val loginViewModel: LoginViewModel = viewModel()
                    val cepViewModel: CepViewModel = viewModel()


                    NavGraph(navController, loginViewModel, cepViewModel, Modifier.padding(innerPadding))
                }
            }
        }
    }
}

