package com.example.myapplication.presentation.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.dto.UserInputDto
import com.example.myapplication.data.model.UserResult
import com.example.myapplication.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository = UserRepository()) : ViewModel() {

    private val _userState = MutableStateFlow<UserState>(UserState.Initial)
    val userState: StateFlow<UserState> = _userState.asStateFlow()

    private val _createUserState = MutableStateFlow<CreateUserState>(CreateUserState.Initial)
    val createUserState: StateFlow<CreateUserState> = _createUserState.asStateFlow()

    fun buscarUsers() {
        viewModelScope.launch {
            _userState.value = UserState.Loading

            val resultado = userRepository.buscarUsuarios()

            _userState.value = resultado.fold(
                onSuccess = { users ->
                    android.util.Log.d("UserViewModel", "Usuários recebidos: ${users.size}")
                    users.forEach { user ->
                        android.util.Log.d("UserViewModel", "User: id=${user.id}, name=${user.name}, email=${user.email}")
                    }
                    UserState.Success(userResult = users)
                },
                onFailure = { error ->
                    android.util.Log.e("UserViewModel", "Erro ao buscar usuários", error)
                    UserState.Error(message = error.message ?: "Erro desconhecido")
                }
            )
        }
    }

    fun criarUsuario(name: String, username: String, email: String) {
        viewModelScope.launch {
            _createUserState.value = CreateUserState.Loading

            android.util.Log.d("UserViewModel", "Criando usuário: name=$name, username=$username, email=$email")

            val input = UserInputDto(
                name = name,
                username = username,
                email = email
            )

            val resultado = userRepository.criarUsuario(input)

            _createUserState.value = resultado.fold(
                onSuccess = { user ->
                    android.util.Log.d("UserViewModel", "Usuário criado com sucesso: $user")
                    CreateUserState.Success(user = user)
                },
                onFailure = { error ->
                    android.util.Log.e("UserViewModel", "Erro ao criar usuário", error)
                    CreateUserState.Error(message = error.message ?: "Erro ao criar usuário")
                }
            )
        }
    }

    fun resetCreateState() {
        _createUserState.value = CreateUserState.Initial
    }

    sealed class UserState {
        object Initial : UserState()
        object Loading : UserState()
        data class Success(val userResult: List<UserResult>) : UserState()
        data class Error(val message: String) : UserState()
    }

    sealed class CreateUserState {
        object Initial : CreateUserState()
        object Loading : CreateUserState()
        data class Success(val user: UserResult) : CreateUserState()
        data class Error(val message: String) : CreateUserState()
    }
}