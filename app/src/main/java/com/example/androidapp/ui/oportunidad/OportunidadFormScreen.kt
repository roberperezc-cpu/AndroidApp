package com.example.androidapp.ui.oportunidad

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OportunidadFormScreen(
    viewModel: OportunidadViewModel,
    onVolver: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.guardadoExitosamente) {
        if (state.guardadoExitosamente) {
            viewModel.limpiarFormulario()
            onVolver()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (state.id == 0) "Nueva Oportunidad" else "Editar Oportunidad") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = state.nombre,
                onValueChange = { viewModel.onNombreChange(it) },
                label = { Text("Nombre de la Oportunidad *") },
                isError = state.nombreError != null,
                supportingText = { state.nombreError?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.tipo,
                onValueChange = { viewModel.onTipoChange(it) },
                label = { Text("Tipo (ej. Evento corporativo, Feria) *") },
                isError = state.tipoError != null,
                supportingText = { state.tipoError?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.descripcion,
                onValueChange = { viewModel.onDescripcionChange(it) },
                label = { Text("Descripción *") },
                isError = state.descripcionError != null,
                supportingText = { state.descripcionError?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            OutlinedTextField(
                value = state.fuente,
                onValueChange = { viewModel.onFuenteChange(it) },
                label = { Text("Fuente de Información *") },
                isError = state.fuenteError != null,
                supportingText = { state.fuenteError?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.prioridad,
                onValueChange = { viewModel.onPrioridadChange(it) },
                label = { Text("Prioridad (Alta, Media, Baja) *") },
                isError = state.prioridadError != null,
                supportingText = { state.prioridadError?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.potencial,
                onValueChange = { viewModel.onPotencialChange(it) },
                label = { Text("Potencial Comercial *") },
                isError = state.potencialError != null,
                supportingText = { state.potencialError?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.estado,
                onValueChange = { viewModel.onEstadoChange(it) },
                label = { Text("Estado de Gestión") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.observaciones,
                onValueChange = { viewModel.onObservacionesChange(it) },
                label = { Text("Observaciones") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { viewModel.guardarOportunidad() },
                enabled = !state.guardando,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (state.guardando) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Guardar Oportunidad")
                }
            }
        }
    }
}