package com.example.myapplication.presentation.cep

import CepViewModel
import CepViewModel.CepState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.myapplication.data.model.CepResult
import com.example.myapplication.data.navigation.Routes
import androidx.compose.ui.platform.LocalContext
import com.example.myapplication.data.local.LoginPreferences


@Composable
fun CepScreen(
    viewModel: CepViewModel = viewModel(),
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    var cepInput by remember { mutableStateOf("") }
    val cepState by viewModel.cepState.collectAsStateWithLifecycle()
    val listaCep by viewModel.listaFavoritos.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Consulta CEP",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Button(
                onClick = {
                    // Fazer logout: limpar SharedPreferences e voltar para tela de login
                    LoginPreferences.clear(context)
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.CEP) { inclusive = true }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Logout", color = MaterialTheme.colorScheme.onError)
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)   // ocupa todo o espaço disponível acima
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            OutlinedTextField(
                value = cepInput,
                onValueChange = {
                    if (it.length <= 8) cepInput = it
                },
                label = { Text("CEP") },
                placeholder = { Text("00000-000") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Button(
                onClick = { viewModel.buscarCep(cepInput) },
                modifier = Modifier.fillMaxWidth(),
                enabled = cepInput.isNotBlank()
            ) {
                Text("Buscar")
            }

            when (val state = cepState) {
                is CepState.Initial -> {}
                is CepState.Loading -> CircularProgressIndicator()
                is CepState.Success -> {
                    CepResultCard(
                        cepResult = state.cepResult,
                        onClick = { viewModel.favoritarCep(state.cepResult.cep) }
                    )
                }

                is CepState.Error -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Text(
                            text = state.message,
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Favoritos",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(items = listaCep,) { card ->
                    CepResultCard(
                        cepResult = card,
                        onClick = { viewModel.desfavoritarCep(card.cep) }
                    )
                }
            }
        }
    }
}


@Composable
fun CepResultCard(cepResult: CepResult, onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Endereço Encontrado",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Divider()

            InfoRow(label = "CEP", value = cepResult.cep)
            InfoRow(label = "Logradouro", value = cepResult.logradouro)
            InfoRow(label = "Bairro", value = cepResult.bairro)
            InfoRow(label = "Estado", value = "${cepResult.estado} (${cepResult.uf})")

            if (!cepResult.complemento.isNullOrBlank()) {
                InfoRow(label = "Complemento", value = cepResult.complemento)
            }


        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(
                onClick = onClick,
                shape = RoundedCornerShape(5.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Remover")
            }

        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$label:",
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview
@Composable
private fun sdsdsdsdsdsdsdsdsdsdsd() {
    val cep: CepResult = CepResult(
        "sddsd",
        "sddsd",
        "sddsd",
        "sddsd",
        "sddsd",
        "sddsd",
    )
    CepResultCard(cep)
}