package com.example.androidapp.repository

import android.util.Log
import com.example.androidapp.datos.local.OportunidadDao
import com.example.androidapp.datos.local.OportunidadEntity
import com.example.androidapp.network.ApiService
import com.example.androidapp.network.RetrofitClient
import kotlinx.coroutines.flow.Flow

class OportunidadRepository(
    private val dao: OportunidadDao,
    private val api: ApiService = RetrofitClient.microserviceApi
) {

    val todasLasOportunidades: Flow<List<OportunidadEntity>> = dao.obtenerTodas()

    suspend fun sincronizarConServidor() {
        try {
            val remotas = api.obtenerOportunidades()
            remotas.forEach { dao.insertar(it) }
        } catch (e: Exception) {
            Log.e("OportunidadRepository", "Error al sincronizar con Spring Boot: ${e.message}")
        }
    }

    suspend fun obtenerPorId(id: Int): OportunidadEntity? {
        return dao.obtenerPorId(id)
    }

    suspend fun guardarOportunidad(oportunidad: OportunidadEntity) {
        if (oportunidad.id == 0) {
            try {
                // Envía al microservicio Spring Boot y guarda en Room con el ID generado por el servidor
                val creadaEnServidor = api.crearOportunidad(oportunidad)
                dao.insertar(creadaEnServidor)
            } catch (e: Exception) {
                Log.e("OportunidadRepository", "Fallo POST en Spring Boot, guardando en local: ${e.message}")
                dao.insertar(oportunidad)
            }
        } else {
            try {
                val actualizadaEnServidor = api.actualizarOportunidad(oportunidad.id, oportunidad)
                dao.actualizar(actualizadaEnServidor)
            } catch (e: Exception) {
                Log.e("OportunidadRepository", "Fallo PUT en Spring Boot, actualizando en local: ${e.message}")
                dao.actualizar(oportunidad)
            }
        }
    }

    suspend fun eliminarOportunidad(oportunidad: OportunidadEntity) {
        try {
            api.eliminarOportunidad(oportunidad.id)
        } catch (e: Exception) {
            Log.e("OportunidadRepository", "Fallo DELETE en Spring Boot: ${e.message}")
        }
        dao.eliminar(oportunidad)
    }
}