package com.example.myapplication.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserInputDto(

    @SerialName("name")
    val name: String = "",

    @SerialName("username")
    val username: String = "",

    @SerialName("email")
    val email: String = "",
)
