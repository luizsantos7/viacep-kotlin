package com.example.myapplication.presentation.cep

import com.example.myapplication.data.model.CepResult

data class CepUiState(
    val foiFavoritado : Boolean = false,
    val listaFavoritos: MutableList<CepResult> = mutableListOf()
)