package com.example.androidapp

import com.example.androidapp.datos.local.OportunidadDao
import com.example.androidapp.datos.local.OportunidadEntity
import com.example.androidapp.network.ApiService
import com.example.androidapp.repository.OportunidadRepository
import io.mockk.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class OportunidadRepositoryTest {

    private lateinit var dao: OportunidadDao
    private lateinit var api: ApiService
    private lateinit var repository: OportunidadRepository

    @Before
    fun setUp() {
        dao = mockk(relaxed = true)
        api = mockk(relaxed = true)
        every { dao.obtenerTodas() } returns flowOf(emptyList())
        repository = OportunidadRepository(dao, api)
    }

    @Test
    fun testObtenerTodasLasOportunidades() = runTest {
        val listaMock = listOf(
            OportunidadEntity(id = 1, nombre = "Oportunidad 1", tipo = "Feria", descripcion = "Desc 1", fuente = "Fuente 1", prioridad = "Alta", potencial = "Alto")
        )
        every { dao.obtenerTodas() } returns flowOf(listaMock)
        val localRepo = OportunidadRepository(dao, api)

        val resultado = localRepo.todasLasOportunidades.first()
        assertEquals(listaMock, resultado)
    }

    @Test
    fun testGuardarNuevaOportunidadLlamaApiEInsertaEnDao() = runTest {
        val nueva = OportunidadEntity(id = 0, nombre = "Nueva", tipo = "Feria", descripcion = "Desc", fuente = "Fuente", prioridad = "Alta", potencial = "Alto")
        val respuestaServidor = nueva.copy(id = 10)
        coEvery { api.crearOportunidad(nueva) } returns respuestaServidor

        repository.guardarOportunidad(nueva)

        coVerify { api.crearOportunidad(nueva) }
        coVerify { dao.insertar(respuestaServidor) }
    }

    @Test
    fun testGuardarOportunidadExistenteLlamaPutYActualizaDao() = runTest {
        val existente = OportunidadEntity(id = 1, nombre = "Existente", tipo = "Feria", descripcion = "Desc", fuente = "Fuente", prioridad = "Alta", potencial = "Alto")
        coEvery { api.actualizarOportunidad(1, existente) } returns existente

        repository.guardarOportunidad(existente)

        coVerify { api.actualizarOportunidad(1, existente) }
        coVerify { dao.actualizar(existente) }
    }

    @Test
    fun testEliminarOportunidadLlamaDeleteYEliminaEnDao() = runTest {
        val oportunidad = OportunidadEntity(id = 1, nombre = "Eliminar", tipo = "Feria", descripcion = "Desc", fuente = "Fuente", prioridad = "Alta", potencial = "Alto")
        coEvery { api.eliminarOportunidad(1) } returns Response.success(null)

        repository.eliminarOportunidad(oportunidad)

        coVerify { api.eliminarOportunidad(1) }
        coVerify { dao.eliminar(oportunidad) }
    }
}
