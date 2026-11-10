package com.example.androidapp.ui.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidapp.datos.local.UserEntity
import com.example.androidapp.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: UserRepository) : ViewModel() {

    var emailLogin by mutableStateOf("")
    var passwordLogin by mutableStateOf("")
    var errorLogin by mutableStateOf<String?>(null)

    var nombreRegister by mutableStateOf("")
    var emailRegister by mutableStateOf("")
    var passwordRegister by mutableStateOf("")
    var confirmPasswordRegister by mutableStateOf("")
    var errorRegister by mutableStateOf<String?>(null)
    var successRegister by mutableStateOf(false)

    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> = _currentUser.asStateFlow()

    fun login(onSuccess: () -> Unit) {
        if (emailLogin.isBlank() || passwordLogin.isBlank()) {
            errorLogin = "Por favor completa todos los campos"
            return
        }
        viewModelScope.launch {
            val user = repository.login(emailLogin.trim(), passwordLogin)
            if (user != null) {
                _currentUser.value = user
                errorLogin = null
                onSuccess()
            } else {
                errorLogin = "Correo o contraseña incorrectos"
            }
        }
    }

    fun registrar(onSuccess: () -> Unit) {
        if (nombreRegister.isBlank() || emailRegister.isBlank() || passwordRegister.isBlank() || confirmPasswordRegister.isBlank()) {
            errorRegister = "Por favor completa todos los campos"
            return
        }
        if (passwordRegister != confirmPasswordRegister) {
            errorRegister = "Las contraseñas no coinciden"
            return
        }
        viewModelScope.launch {
            val nuevoUsuario = UserEntity(
                nombre = nombreRegister.trim(),
                email = emailRegister.trim(),
                password = passwordRegister
            )
            val resultado = repository.registrar(nuevoUsuario)
            resultado.fold(
                onSuccess = {
                    errorRegister = null
                    successRegister = true
                    // Auto login or prompt login
                    _currentUser.value = nuevoUsuario
                    onSuccess()
                },
                onFailure = { error ->
                    errorRegister = error.localizedMessage ?: "Error al registrar"
                }
            )
        }
    }

    fun logout() {
        _currentUser.value = null
        emailLogin = ""
        passwordLogin = ""
        errorLogin = null
    }

    fun limpiarFormularios() {
        emailLogin = ""
        passwordLogin = ""
        errorLogin = null
        nombreRegister = ""
        emailRegister = ""
        passwordRegister = ""
        confirmPasswordRegister = ""
        errorRegister = null
        successRegister = false
    }
}
