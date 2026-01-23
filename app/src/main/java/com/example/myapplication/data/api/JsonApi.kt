package com.example.myapplication.data.api

import com.example.myapplication.data.dto.UserInputDto
import com.example.myapplication.data.dto.UserInputDtoResponse
import com.example.myapplication.data.model.UserResult
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface JsonApi {

    @GET("users")
    suspend fun buscarUsers(): List<UserResult>

    @POST("users")
    suspend fun postUser(@Body input: UserInputDto): Response<UserResult>
}