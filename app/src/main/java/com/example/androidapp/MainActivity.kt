package com.example.androidapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidapp.datos.local.AppDatabase
import com.example.androidapp.navegacion.AppNavigation
import com.example.androidapp.repository.OportunidadRepository
import com.example.androidapp.ui.oportunidad.OportunidadViewModel
import com.example.androidapp.ui.theme.AndroidAppTheme
class MainActivity : ComponentActivity() {

    private val database by lazy { AppDatabase.getDatabase(this) }
    private val repository by lazy { OportunidadRepository(database.oportunidadDao()) }

    private val viewModel: OportunidadViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return OportunidadViewModel(repository) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidAppTheme {
                AppNavigation(viewModel = viewModel)
            }
        }
    }
}