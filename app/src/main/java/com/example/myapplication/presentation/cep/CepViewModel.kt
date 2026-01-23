package com.example.myapplication.presentation.cep

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.CepResult
import com.example.myapplication.data.repository.CepRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CepViewModel(private val repository: CepRepository = CepRepository()) : ViewModel() {

    private val _cepState = MutableStateFlow<CepState>(CepState.Initial)
    val cepState: StateFlow<CepState> = _cepState.asStateFlow()

    fun buscarCep(cep: String) {
        viewModelScope.launch {
            _cepState.value = CepState.Loading

            val resultado = repository.buscarCep(cep)

            _cepState.value = resultado.fold(
                onSuccess = { CepState.Success(it) },
                onFailure = { CepState.Error(it.message ?: "Erro desconhecido") }
            )
        }
    }

    fun limparEstado() {
        _cepState.value = CepState.Initial
    }
}

sealed class CepState {
    object Initial : CepState()
    object Loading : CepState()
    data class Success(val cepResult: CepResult) : CepState()
    data class Error(val message: String) : CepState()
}