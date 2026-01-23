package com.example.myapplication.presentation.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.data.navigation.Routes


@Composable
fun LoginScreen(viewModel: LoginViewModel, navController: NavHostController, modifier: Modifier) {
    val uiState = viewModel.uiState.collectAsState().value
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = uiState.usuario,
            onValueChange = { viewModel.onUserTextChange(it) },
            modifier.padding(10.dp),
            shape = RoundedCornerShape(12.dp)
        )

        OutlinedTextField(
            value = uiState.senha,
            onValueChange = { viewModel.onSenhaTextChange(it) },
            modifier.padding(10.dp),
            shape = RoundedCornerShape(12.dp)
        )
        Button(
            onClick = {
                navController.navigate(Routes.CEP) {
                    popUpTo(Routes.LOGIN) { inclusive = true }
                }
            }
        ) {
            Text("Entrar")
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun LoginPreview() {
    val viewModel: LoginViewModel = viewModel()
    LoginScreen(viewModel = viewModel, navController = rememberNavController(), Modifier)
}