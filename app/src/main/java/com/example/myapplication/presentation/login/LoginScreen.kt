package com.example.myapplication.presentation.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.data.navigation.Routes
import androidx.compose.ui.platform.LocalContext
import com.example.myapplication.data.local.LoginPreferences


@Composable
fun LoginScreen(viewModel: LoginViewModel, navController: NavHostController, modifier: Modifier) {
    val uiState = viewModel.uiState.collectAsState().value
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loadSavedCredentials(context)
        if (LoginPreferences.isLogged(context)) {
            navController.navigate(Routes.CEP) {
                popUpTo(Routes.LOGIN) { inclusive = true }
            }
        }
    }

    Column(
        modifier
            .fillMaxSize()
            .padding(50.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = uiState.usuario,
            onValueChange = { viewModel.onUserTextChange(it) },
            modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            label = {Text("Email")}
        )

        OutlinedTextField(
            value = uiState.senha,
            onValueChange = { viewModel.onSenhaTextChange(it) },
            modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            label = {Text("Senha")},
            visualTransformation = PasswordVisualTransformation()
        )
        Button(
            onClick = {
                // salva credenciais antes de navegar
                viewModel.saveCredentials(context)
                navController.navigate(Routes.CEP) {
                    popUpTo(Routes.LOGIN) { inclusive = true }
                }
            },
            shape = RoundedCornerShape(12.dp),
            modifier = modifier.height(50.dp),
            enabled = viewModel.isEnabled()
        ) {
            Text(
                "Entrar",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun LoginPreview() {
    val viewModel: LoginViewModel = viewModel()
    LoginScreen(viewModel = viewModel, navController = rememberNavController(), Modifier)
}