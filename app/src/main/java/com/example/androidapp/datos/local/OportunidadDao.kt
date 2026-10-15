package com.example.androidapp.datos.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface OportunidadDao {
    @Query("SELECT * FROM oportunidades ORDER BY id DESC")
    fun obtenerTodas(): Flow<List<OportunidadEntity>>

    @Query("SELECT * FROM oportunidades WHERE id = :id")
    suspend fun obtenerPorId(id: Int): OportunidadEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(oportunidad: OportunidadEntity)

    @Update
    suspend fun actualizar(oportunidad: OportunidadEntity)

    @Delete
    suspend fun eliminar(oportunidad: OportunidadEntity)
}