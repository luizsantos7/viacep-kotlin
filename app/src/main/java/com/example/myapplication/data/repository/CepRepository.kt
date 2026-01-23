package com.example.myapplication.data.repository

import com.example.myapplication.data.model.CepResult

// Em uma ViewModel ou Repository
class CepRepository {
    private val api = RetrofitClient.viaCepApi

    suspend fun buscarCep(cep: String): Result<CepResult> {
        return try {
            val resultado = api.buscarCep(cep.replace("-", ""))

            if (resultado.erro == true) {
                Result.failure(Exception("CEP não encontrado"))
            } else {
                Result.success(resultado)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}