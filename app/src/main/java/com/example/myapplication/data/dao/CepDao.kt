// src/main/java/com/example/myapplication/data/local/CepDao.kt
package com.example.myapplication.data.local

import androidx.room.*

@Dao
interface CepDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cep: CepEntity)

    @Query("SELECT * FROM ceps")
    suspend fun getAll(): List<CepEntity>

    @Query("SELECT * FROM ceps WHERE cep = :cep LIMIT 1")
    suspend fun findByCep(cep: String): CepEntity?

    @Query("DELETE FROM ceps WHERE cep = :cep")
    suspend fun deleteByCep(cep: String)
}
