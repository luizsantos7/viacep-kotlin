// src/main/java/com/example/myapplication/data/local/CepEntity.kt
package com.example.myapplication.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ceps")
data class CepEntity(
    @PrimaryKey val cep: String,
    val logradouro: String = "",
    val complemento: String = "",
    val bairro: String = "",
    val estado: String = "",
    val uf: String = "",
    val isFav: Boolean = true
)
