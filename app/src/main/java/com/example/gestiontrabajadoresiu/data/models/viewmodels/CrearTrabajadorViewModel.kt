package com.example.gestiontrabajadoresiu.data.models.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestiontrabajadoresiu.data.RetrofitClient
import com.example.gestiontrabajadoresiu.data.models.dtos.RegisterTrabajadorRequest
import com.example.gestiontrabajadoresiu.data.models.dtos.Trabajador
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class CrearTrabajadorUiState {
    object Idle : CrearTrabajadorUiState()
    object Loading : CrearTrabajadorUiState()
    data class Success(val trabajador: Trabajador) : CrearTrabajadorUiState()
    data class Error(val mensaje: String) : CrearTrabajadorUiState()
}

class CrearTrabajadorViewModel : ViewModel() {
    private val _nombre = MutableStateFlow("")
    val nombre: StateFlow<String> = _nombre

    private val _apellido = MutableStateFlow("")
    val apellido: StateFlow<String> = _apellido

    private val _dni = MutableStateFlow("")
    val dni: StateFlow<String> = _dni

    private val _edad = MutableStateFlow("")
    val edad: StateFlow<String> = _edad

    private val _genero = MutableStateFlow("")
    val genero: StateFlow<String> = _genero

    private val _cargo = MutableStateFlow("")
    val cargo: StateFlow<String> = _cargo

    private val _uiState = MutableStateFlow<CrearTrabajadorUiState>(CrearTrabajadorUiState.Idle)
    val uiState: StateFlow<CrearTrabajadorUiState> = _uiState

    val formValido: StateFlow<Boolean> = combine(
        _nombre, _apellido, _dni, _edad, _genero
    ) { nombre, apellido, dni, edad, genero ->
        nombre.isNotBlank() &&
                apellido.isNotBlank() &&
                dni.length == 8 && dni.isNotBlank() &&
                edad.toIntOrNull() != null &&
                genero.isNotBlank()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun onNombreChanged(value: String) { _nombre.value = value }
    fun onApellidoChanged(value: String) { _apellido.value = value }
    fun onDniChanged(value: String) { if (value.length <= 8) _dni.value = value }
    fun onEdadChanged(value: String) { if (value.all { it.isDigit() }) _edad.value = value }
    fun onGeneroChanged(value: String) { _genero.value = value }
    fun onCargoChanged(value: String) { _cargo.value = value }

    private fun getTrabajador(): RegisterTrabajadorRequest {
        return RegisterTrabajadorRequest(
            nombre = _nombre.value,
            apellido = _apellido.value,
            dni = _dni.value,
            edad = _edad.value.toIntOrNull() ?: 0,
            genero = _genero.value,
            cargo = _cargo.value.ifBlank { null }
        )
    }

    fun onAgregarTrabajador() {
        var trabajador = getTrabajador();

        viewModelScope.launch {
            _uiState.value = CrearTrabajadorUiState.Loading
            try {
                val response = RetrofitClient.api.crearTrabajador(trabajador)
                if (response.isSuccessful) {
                    _uiState.value = CrearTrabajadorUiState.Success(response.body()!!)
                }else {
                    _uiState.value = CrearTrabajadorUiState.Error("No se pudo crear el trabajador.")
                }
            }catch (e: Exception) {
                _uiState.value = CrearTrabajadorUiState.Error("Error de conexión: ${e.message}")
            }
        }
    }
}