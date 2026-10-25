package com.example.androidapp.navegacion

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.androidapp.ui.auth.AuthViewModel
import com.example.androidapp.ui.auth.LoginScreen
import com.example.androidapp.ui.auth.RegisterScreen
import com.example.androidapp.ui.oportunidad.ListaOportunidadesScreen
import com.example.androidapp.ui.oportunidad.OportunidadFormScreen
import com.example.androidapp.ui.oportunidad.OportunidadViewModel

sealed class Pantalla(val ruta: String) {
    object Login : Pantalla("login")
    object Register : Pantalla("register")
    object Lista : Pantalla("lista_oportunidades")
    object Formulario : Pantalla("form_oportunidad?id={id}") {
        fun crearRuta(id: Int = 0) = "form_oportunidad?id=$id"
    }
}

@Composable
fun AppNavigation(
    oportunidadViewModel: OportunidadViewModel,
    authViewModel: AuthViewModel
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Pantalla.Login.ruta
    ) {
        composable(Pantalla.Login.ruta) {
            LoginScreen(
                viewModel = authViewModel,
                onLoginExitoso = {
                    navController.navigate(Pantalla.Lista.ruta) {
                        popUpTo(Pantalla.Login.ruta) { inclusive = true }
                    }
                },
                onIrARegistro = {
                    authViewModel.limpiarFormularios()
                    navController.navigate(Pantalla.Register.ruta)
                }
            )
        }

        composable(Pantalla.Register.ruta) {
            RegisterScreen(
                viewModel = authViewModel,
                onRegistroExitoso = {
                    navController.navigate(Pantalla.Lista.ruta) {
                        popUpTo(Pantalla.Login.ruta) { inclusive = true }
                    }
                },
                onVolverALogin = {
                    authViewModel.limpiarFormularios()
                    navController.popBackStack()
                }
            )
        }

        composable(Pantalla.Lista.ruta) {
            ListaOportunidadesScreen(
                viewModel = oportunidadViewModel,
                onAgregarClick = {
                    navController.navigate(Pantalla.Formulario.crearRuta(0))
                },
                onEditarClick = { id ->
                    navController.navigate(Pantalla.Formulario.crearRuta(id))
                },
                onCerrarSesion = {
                    authViewModel.logout()
                    navController.navigate(Pantalla.Login.ruta) {
                        popUpTo(Pantalla.Lista.ruta) { inclusive = true }
                    }
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
                oportunidadViewModel.cargarOportunidadParaEditar(id)
            } else {
                oportunidadViewModel.limpiarFormulario()
            }

            OportunidadFormScreen(
                viewModel = oportunidadViewModel,
                onVolver = { navController.popBackStack() }
            )
        }
    }
}
