package com.example.androidapp

import com.example.androidapp.datos.local.OportunidadEntity
import com.example.androidapp.repository.OportunidadRepository
import com.example.androidapp.ui.oportunidad.OportunidadViewModel
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class OportunidadViewModelTest {

    private lateinit var repository: OportunidadRepository
    private lateinit var viewModel: OportunidadViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk(relaxed = true)
        every { repository.todasLasOportunidades } returns flowOf(emptyList())
        viewModel = OportunidadViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testValidacionCamposObligatorios() {
        viewModel.guardarOportunidad()
        val state = viewModel.uiState.value
        assertEquals("El nombre es obligatorio", state.nombreError)
        assertEquals("El tipo es obligatorio", state.tipoError)
    }

    @Test
    fun testGuardarOportunidadExitoso() = runTest {
        viewModel.onNombreChange("Oportunidad Test")
        viewModel.onTipoChange("Feria")
        viewModel.onDescripcionChange("Descripción de prueba")
        viewModel.onFuenteChange("Fuente test")
        viewModel.onPrioridadChange("Alta")
        viewModel.onPotencialChange("Alto")

        viewModel.guardarOportunidad()
        advanceUntilIdle()

        coVerify { repository.guardarOportunidad(any()) }
        assertTrue(viewModel.uiState.value.guardadoExitosamente)
    }

    @Test
    fun testLimpiarFormulario() {
        viewModel.onNombreChange("Test")
        viewModel.limpiarFormulario()
        assertEquals("", viewModel.uiState.value.nombre)
    }
}
