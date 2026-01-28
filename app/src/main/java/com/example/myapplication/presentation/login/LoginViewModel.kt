package com.example.myapplication.presentation.login

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()


    fun onUserTextChange(value: String){
        _uiState.value = uiState.value.copy(usuario = value)
    }

    fun onSenhaTextChange(value: String){
        _uiState.value = uiState.value.copy(senha = value)
    }

    fun isEnabled () : Boolean{
        if (_uiState.value.usuario.isEmpty() and _uiState.value.senha.isEmpty())
            return false

        return true
    }
}