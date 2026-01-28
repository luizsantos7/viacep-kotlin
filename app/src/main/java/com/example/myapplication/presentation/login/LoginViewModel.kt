package com.example.myapplication.presentation.login

import android.content.Context
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.myapplication.data.local.LoginPreferences

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
        return _uiState.value.usuario.isNotBlank() && _uiState.value.senha.isNotBlank()
    }

    fun saveCredentials(context: Context) {
        val user = _uiState.value.usuario
        val pass = _uiState.value.senha
        LoginPreferences.saveLogin(context, user, pass)
    }

    fun loadSavedCredentials(context: Context) {
        val user = LoginPreferences.getUser(context)
        val pass = LoginPreferences.getPass(context)
        if (user != null && pass != null) {
            _uiState.value = _uiState.value.copy(usuario = user, senha = pass)
        }
    }

    fun clearSavedCredentials(context: Context) {
        LoginPreferences.clear(context)
        _uiState.value = LoginUiState()
    }
}