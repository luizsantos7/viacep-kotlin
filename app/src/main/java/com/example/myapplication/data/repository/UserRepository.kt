package com.example.myapplication.data

import com.example.myapplication.data.local.UserDao
import com.example.myapplication.data.local.UserEntity

class UserRepository(private val userDao: UserDao) {
    suspend fun insert(user: UserEntity) = userDao.insert(user)
    suspend fun getAll() = userDao.getAll()
    suspend fun findByUsername(username: String) = userDao.findByUsername(username)
    suspend fun delete(user: UserEntity) = userDao.delete(user)
}
