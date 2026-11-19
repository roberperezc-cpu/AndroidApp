package com.example.androidapp

import com.example.androidapp.datos.local.OportunidadDao
import com.example.androidapp.datos.local.OportunidadEntity
import com.example.androidapp.repository.OportunidadRepository
import io.mockk.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class OportunidadRepositoryTest {

    private lateinit var dao: OportunidadDao
    private lateinit var repository: OportunidadRepository

    @Before
    fun setUp() {
        dao = mockk(relaxed = true)
        every { dao.obtenerTodas() } returns flowOf(emptyList())
        repository = OportunidadRepository(dao)
    }

    @Test
    fun testObtenerTodasLasOportunidades() = runTest {
        val listaMock = listOf(
            OportunidadEntity(id = 1, nombre = "Oportunidad 1", tipo = "Feria", descripcion = "Desc 1", fuente = "Fuente 1", prioridad = "Alta", potencial = "Alto")
        )
        val localDao = mockk<OportunidadDao>()
        every { localDao.obtenerTodas() } returns flowOf(listaMock)
        val localRepo = OportunidadRepository(localDao)

        val resultado = localRepo.todasLasOportunidades.first()
        assertEquals(listaMock, resultado)
    }

    @Test
    fun testGuardarNuevaOportunidadInserta() = runTest {
        val oportunidad = OportunidadEntity(id = 0, nombre = "Nueva", tipo = "Feria", descripcion = "Desc", fuente = "Fuente", prioridad = "Alta", potencial = "Alto")
        repository.guardarOportunidad(oportunidad)

        coVerify { dao.insertar(oportunidad) }
    }

    @Test
    fun testGuardarOportunidadExistenteActualiza() = runTest {
        val oportunidad = OportunidadEntity(id = 1, nombre = "Existente", tipo = "Feria", descripcion = "Desc", fuente = "Fuente", prioridad = "Alta", potencial = "Alto")
        repository.guardarOportunidad(oportunidad)

        coVerify { dao.actualizar(oportunidad) }
    }

    @Test
    fun testEliminarOportunidad() = runTest {
        val oportunidad = OportunidadEntity(id = 1, nombre = "Eliminar", tipo = "Feria", descripcion = "Desc", fuente = "Fuente", prioridad = "Alta", potencial = "Alto")
        repository.eliminarOportunidad(oportunidad)

        coVerify { dao.eliminar(oportunidad) }
    }
}
