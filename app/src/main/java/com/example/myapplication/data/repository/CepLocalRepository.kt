// kotlin
// Arquivo: app/src/main/java/com/example/myapplication/data/repository/CepLocalRepository.kt
package com.example.myapplication.data.repository

import com.example.myapplication.data.local.CepDao
import com.example.myapplication.data.local.CepEntity
import com.example.myapplication.MyApplication

class CepLocalRepository(private val cepDao: CepDao? = null) {
    private val dao: CepDao
        get() = cepDao ?: MyApplication.database.cepDao()

    suspend fun addFav(cep: CepEntity) = dao.insert(cep)
    suspend fun removeFavByCep(cep: String) = dao.deleteByCep(cep)
    suspend fun getAll() = dao.getAll()
    suspend fun findByCep(cep: String) = dao.findByCep(cep)
}
