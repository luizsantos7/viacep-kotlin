package com.example.myapplication.data.model

data class CepResult(
    val cep: String,
    val logradouro: String,
    val complemento: String,
    val bairro: String,
    val estado: String,
    val uf: String,
    val erro: Boolean? = null,
)
