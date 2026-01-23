package com.example.myapplication.presentation.user

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.presentation.user.UserViewModel.UserState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen(
    viewModel: UserViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val userState by viewModel.userState.collectAsState()
    var showCreateDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.buscarUsers()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Usuários") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreateDialog = true }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar Usuário")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (userState) {
                is UserState.Initial -> {
                    Text(
                        text = "Aguardando...",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is UserState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is UserState.Success -> {
                    val users = (userState as UserState.Success).userResult

                    if (users.isEmpty()) {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Nenhum usuário encontrado")
                            Spacer(modifier = Modifier.height(8.dp))
                            TextButton(onClick = { showCreateDialog = true }) {
                                Text("Adicionar primeiro usuário")
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(users) { user ->
                                UserCard(user = user)
                            }
                        }
                    }
                }

                is UserState.Error -> {
                    val message = (userState as UserState.Error).message
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Erro: $message",
                            color = Color.Red
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.buscarUsers() }) {
                            Text("Tentar novamente")
                        }
                    }
                }
            }
        }

        if (showCreateDialog) {
            CreateUserDialog(
                viewModel = viewModel,
                onDismiss = { showCreateDialog = false },
                onUserCreated = {
                    showCreateDialog = false
                    viewModel.buscarUsers()
                }
            )
        }
    }
}

@Composable
fun UserCard(user: com.example.myapplication.data.model.UserResult) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = user.name,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = user.email,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
            Text(
                text = "ID: ${user.id}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )

            user.telefone?.let {
                Text(
                    text = "Telefone: $it",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateUserDialog(
    viewModel: UserViewModel,
    onDismiss: () -> Unit,
    onUserCreated: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    val createUserState by viewModel.createUserState.collectAsState()

    LaunchedEffect(createUserState) {
        if (createUserState is UserViewModel.CreateUserState.Success) {
            onUserCreated()
            viewModel.resetCreateState()
        }
    }

    AlertDialog(
        onDismissRequest = {
            if (createUserState !is UserViewModel.CreateUserState.Loading) {
                onDismiss()
                viewModel.resetCreateState()
            }
        },
        title = { Text("Criar Novo Usuário") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nome Completo") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    enabled = createUserState !is UserViewModel.CreateUserState.Loading
                )

                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    enabled = createUserState !is UserViewModel.CreateUserState.Loading
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true,
                    enabled = createUserState !is UserViewModel.CreateUserState.Loading
                )

                if (createUserState is UserViewModel.CreateUserState.Error) {
                    Text(
                        text = (createUserState as UserViewModel.CreateUserState.Error).message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    viewModel.criarUsuario(
                        name = name.trim(),
                        username = username.trim(),
                        email = email.trim()
                    )
                },
                enabled = name.isNotBlank() &&
                        username.isNotBlank() &&
                        email.isNotBlank() &&
                        createUserState !is UserViewModel.CreateUserState.Loading
            ) {
                if (createUserState is UserViewModel.CreateUserState.Loading) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                        Text("Criando...")
                    }
                } else {
                    Text("Criar")
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                    viewModel.resetCreateState()
                },
                enabled = createUserState !is UserViewModel.CreateUserState.Loading
            ) {
                Text("Cancelar")
            }
        }
    )
}