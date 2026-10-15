package com.example.androidapp.repository

import com.example.androidapp.datos.local.OportunidadDao
import com.example.androidapp.datos.local.OportunidadEntity
import kotlinx.coroutines.flow.Flow

class OportunidadRepository(private val dao: OportunidadDao) {

    val todasLasOportunidades: Flow<List<OportunidadEntity>> = dao.obtenerTodas()

    suspend fun obtenerPorId(id: Int): OportunidadEntity? {
        return dao.obtenerPorId(id)
    }

    suspend fun guardarOportunidad(oportunidad: OportunidadEntity) {
        if (oportunidad.id == 0) {
            dao.insertar(oportunidad)
        } else {
            dao.actualizar(oportunidad)
        }
    }

    suspend fun eliminarOportunidad(oportunidad: OportunidadEntity) {
        dao.eliminar(oportunidad)
    }
}