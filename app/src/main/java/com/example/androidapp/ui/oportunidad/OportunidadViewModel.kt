package com.example.androidapp.ui.oportunidad

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidapp.datos.local.OportunidadEntity
import com.example.androidapp.repository.OportunidadRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class OportunidadUiState(
    val id: Int = 0,
    val nombre: String = "",
    val tipo: String = "",
    val descripcion: String = "",
    val fuente: String = "",
    val prioridad: String = "",
    val potencial: String = "",
    val estado: String = "Evento identificado",
    val fotoUri: String? = null,
    val observaciones: String = "",

    // Errores de validación
    val nombreError: String? = null,
    val tipoError: String? = null,
    val descripcionError: String? = null,
    val fuenteError: String? = null,
    val prioridadError: String? = null,
    val potencialError: String? = null,

    val guardando: Boolean = false,
    val guardadoExitosamente: Boolean = false
)

class OportunidadViewModel(private val repository: OportunidadRepository) : ViewModel() {

    val listaOportunidades: StateFlow<List<OportunidadEntity>> = repository.todasLasOportunidades
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _uiState = MutableStateFlow(OportunidadUiState())
    val uiState: StateFlow<OportunidadUiState> = _uiState.asStateFlow()

    fun onFotoUriChange(path: String?) {
        _uiState.update { it.copy(fotoUri = path) }
    }

    fun onNombreChange(value: String) {
        _uiState.update { it.copy(nombre = value, nombreError = if (value.isBlank()) "El nombre es obligatorio" else null) }
    }

    fun onTipoChange(value: String) {
        _uiState.update { it.copy(tipo = value, tipoError = if (value.isBlank()) "El tipo es obligatorio" else null) }
    }

    fun onDescripcionChange(value: String) {
        _uiState.update { it.copy(descripcion = value, descripcionError = if (value.isBlank()) "La descripción es obligatoria" else null) }
    }

    fun onFuenteChange(value: String) {
        _uiState.update { it.copy(fuente = value, fuenteError = if (value.isBlank()) "La fuente es obligatoria" else null) }
    }

    fun onPrioridadChange(value: String) {
        _uiState.update { it.copy(prioridad = value, prioridadError = if (value.isBlank()) "La prioridad es obligatoria" else null) }
    }

    fun onPotencialChange(value: String) {
        _uiState.update { it.copy(potencial = value, potencialError = if (value.isBlank()) "El potencial comercial es obligatorio" else null) }
    }

    fun onEstadoChange(value: String) {
        _uiState.update { it.copy(estado = value) }
    }

    fun onObservacionesChange(value: String) {
        _uiState.update { it.copy(observaciones = value) }
    }

    fun cargarOportunidadParaEditar(id: Int) {
        viewModelScope.launch {
            val oportunidad = repository.obtenerPorId(id)
            if (oportunidad != null) {
                _uiState.update {
                    OportunidadUiState(
                        id = oportunidad.id,
                        nombre = oportunidad.nombre,
                        tipo = oportunidad.tipo,
                        descripcion = oportunidad.descripcion,
                        fuente = oportunidad.fuente,
                        prioridad = oportunidad.prioridad,
                        potencial = oportunidad.potencial,
                        estado = oportunidad.estado,
                        fotoUri = oportunidad.fotoUri,
                        observaciones = oportunidad.observaciones
                    )
                }
            }
        }
    }

    fun limpiarFormulario() {
        _uiState.value = OportunidadUiState()
    }



    fun guardarOportunidad() {
        val current = _uiState.value

        val nombreErr = if (current.nombre.isBlank()) "El nombre es obligatorio" else null
        val tipoErr = if (current.tipo.isBlank()) "El tipo es obligatorio" else null
        val descErr = if (current.descripcion.isBlank()) "La descripción es obligatoria" else null
        val fuenteErr = if (current.fuente.isBlank()) "La fuente es obligatoria" else null
        val prioErr = if (current.prioridad.isBlank()) "La prioridad es obligatoria" else null
        val potenErr = if (current.potencial.isBlank()) "El potencial comercial es obligatorio" else null

        if (nombreErr != null || tipoErr != null || descErr != null || fuenteErr != null || prioErr != null || potenErr != null) {
            _uiState.update {
                it.copy(
                    nombreError = nombreErr,
                    tipoError = tipoErr,
                    descripcionError = descErr,
                    fuenteError = fuenteErr,
                    prioridadError = prioErr,
                    potencialError = potenErr
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(guardando = true) }
            val entidad = OportunidadEntity(
                id = current.id,
                nombre = current.nombre,
                tipo = current.tipo,
                descripcion = current.descripcion,
                fuente = current.fuente,
                prioridad = current.prioridad,
                potencial = current.potencial,
                estado = current.estado,
                fotoUri = current.fotoUri,
                observaciones = current.observaciones
            )
            repository.guardarOportunidad(entidad)
            _uiState.update { it.copy(guardando = false, guardadoExitosamente = true) }
        }
    }
}