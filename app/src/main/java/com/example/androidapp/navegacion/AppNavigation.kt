package com.example.androidapp.navegacion

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.androidapp.ui.oportunidad.ListaOportunidadesScreen
import com.example.androidapp.ui.oportunidad.OportunidadFormScreen
import com.example.androidapp.ui.oportunidad.OportunidadViewModel

sealed class Pantalla(val ruta: String) {
    object Lista : Pantalla("lista_oportunidades")
    object Formulario : Pantalla("form_oportunidad?id={id}") {
        fun crearRuta(id: Int = 0) = "form_oportunidad?id=$id"
    }
}

@Composable
fun AppNavigation(viewModel: OportunidadViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Pantalla.Lista.ruta
    ) {
        composable(Pantalla.Lista.ruta) {
            ListaOportunidadesScreen(
                viewModel = viewModel,
                onAgregarClick = {
                    navController.navigate(Pantalla.Formulario.crearRuta(0))
                },
                onEditarClick = { id ->
                    navController.navigate(Pantalla.Formulario.crearRuta(id))
                }
            )
        }

        composable(
            route = Pantalla.Formulario.ruta,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                    defaultValue = 0
                }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 0
            if (id != 0) {
                viewModel.cargarOportunidadParaEditar(id)
            } else {
                viewModel.limpiarFormulario()
            }

            OportunidadFormScreen(
                viewModel = viewModel,
                onVolver = { navController.popBackStack() }
            )
        }
    }
}