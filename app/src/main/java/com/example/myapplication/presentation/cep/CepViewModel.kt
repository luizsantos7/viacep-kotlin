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

    private val _listaFavoritos = MutableStateFlow<List<CepResult>>(emptyList())
    val listaFavoritos: StateFlow<List<CepResult>> = _listaFavoritos

    fun buscarCep(cep: String) {
        viewModelScope.launch {
            buscarCepInterno(cep)
        }
    }

    private suspend fun buscarCepInterno(cep: String): Result<CepResult> {
        _cepState.value = CepState.Loading
        val resultado = repository.buscarCep(cep)
        _cepState.value = resultado.fold(
            onSuccess = { CepState.Success(cepResult = it) },
            onFailure = { CepState.Error(message = it.message ?: "Erro desconhecido") }
        )
        return resultado
    }

    fun favoritarCep(cep: String) {
        viewModelScope.launch {
            val resultado = buscarCepInterno(cep)
            resultado.onSuccess { cepResult ->
                if (!cepResult.isFav) {
                    val favorito = cepResult.copy(isFav = true)

                    val jaFavoritado = _listaFavoritos.value.any { it.cep == cepResult.cep }
                    if (!jaFavoritado) {
                        _listaFavoritos.value = _listaFavoritos.value + favorito
                    }
                }
            }
        }
    }

    fun desfavoritarCep(cep: String) {
        viewModelScope.launch {
            val resultado = buscarCepInterno(cep)
            resultado.onSuccess { cepResult ->
                val favorito = _listaFavoritos.value.firstOrNull { it.cep == cep }
                if (favorito != null) {
                    _listaFavoritos.value = _listaFavoritos.value.filterNot { it.cep == cep }
                }
            }
        }
    }

    sealed class CepState {
        object Initial : CepState()
        object Loading : CepState()
        data class Success(val cepResult: CepResult) : CepState()
        data class Error(val message: String) : CepState()
    }
}