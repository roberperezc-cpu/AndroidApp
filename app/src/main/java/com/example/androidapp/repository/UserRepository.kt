package com.example.androidapp.repository

import com.example.androidapp.datos.local.UserDao
import com.example.androidapp.datos.local.UserEntity

class UserRepository(private val userDao: UserDao) {

    suspend fun registrar(usuario: UserEntity): Result<Unit> {
        return try {
            val existente = userDao.obtenerPorEmail(usuario.email)
            if (existente != null) {
                Result.failure(Exception("El correo ya está registrado"))
            } else {
                userDao.insertar(usuario)
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(email: String, password: String): UserEntity? {
        return userDao.login(email, password)
    }
}
