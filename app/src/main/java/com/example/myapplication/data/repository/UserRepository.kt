package com.example.myapplication.data.repository

import RetrofitClient
import com.example.myapplication.data.dto.UserInputDto
import com.example.myapplication.data.dto.UserInputDtoResponse
import com.example.myapplication.data.model.UserResult

class UserRepository {
    private val api = RetrofitClient.jsonApi

    suspend fun buscarUsuarios(): Result<List<UserResult>> {
        return try {
            val users = api.buscarUsers()
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun criarUsuario(input: UserInputDto): Result<UserResult> {
        return try {
            val response = api.postUser(input)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Erro ao criar usuário: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}