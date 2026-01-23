package com.example.myapplication.data.api

import com.example.myapplication.data.model.CepResult
import retrofit2.http.GET
import retrofit2.http.Path

interface ViaCepApi {
    @GET("ws/{cep}/json/")
    suspend fun buscarCep(@Path("cep") cep: String): CepResult
}