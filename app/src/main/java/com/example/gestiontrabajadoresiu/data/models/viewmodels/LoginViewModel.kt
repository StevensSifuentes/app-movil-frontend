package com.example.gestiontrabajadoresiu.data.models.viewmodels

import android.app.Application
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestiontrabajadoresiu.data.RetrofitClient
import com.example.gestiontrabajadoresiu.data.SessionManager
import com.example.gestiontrabajadoresiu.data.models.dtos.LoginRequest
import com.example.gestiontrabajadoresiu.data.models.dtos.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val usuario: Usuario) : LoginUiState()
    data class Error(val mensaje: String) : LoginUiState()
}

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _loginEnable = MutableStateFlow(false)
    val loginEnable: StateFlow<Boolean> = _loginEnable

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState

    fun onLoginChanged(email: String, password: String) {
        _email.value = email
        _password.value = password
        _loginEnable.value = isValidEmail(email) && isValidPassword(password)
    }

    private fun isValidPassword(password: String): Boolean = password.length > 8

    private fun isValidEmail(email: String): Boolean  = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    fun onLoginSelected() {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading

            try {
                val response = RetrofitClient.api.login(
                    LoginRequest(_email.value, _password.value)
                )

                if(response.isSuccessful){
                    val body = response.body()!!
                    RetrofitClient.authToken = body.token // guarda el token para futuras peticiones
                    RetrofitClient.nombreUsuarioActual = body.usuario.nombreUsuario // para mostrarlo en pantalla de gestión de trabajadores como usuario autenticado

                    SessionManager.guardarSesion(
                        context = getApplication(),
                        token = body.token,
                        usuarioId = body.usuario.id,
                        nombreUsuario = body.usuario.nombreUsuario
                    )
                    _uiState.value = LoginUiState.Success(body.usuario);
                }else{
                    _uiState.value = LoginUiState.Error("Usuario o contraseña incorrecta.");
                }
            }catch (e: Exception){
                Log.e("LOGIN_ERROR", "Error completo: ", e) // esto imprime el stack trace completo en Logcat
                _uiState.value = LoginUiState.Error("Error de conexión: ${e.message}");
            }
        }
    }
}