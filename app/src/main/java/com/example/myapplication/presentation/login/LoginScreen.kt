package com.example.myapplication.presentation.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun LoginScreen(modifier: Modifier = Modifier) {
    val viewModel: LoginViewModel = viewModel()
    val uiState = viewModel.uiState.collectAsState().value
    Column(
        modifier.fillMaxSize()
    ) {
        OutlinedTextField(
            value = uiState.usuario,
            onValueChange = { viewModel.onUserTextChange(it) },

        )
        Text("oi")
    }
}


@Preview(showBackground = true)
@Composable
private fun LoginPreview() {
    LoginScreen()
}