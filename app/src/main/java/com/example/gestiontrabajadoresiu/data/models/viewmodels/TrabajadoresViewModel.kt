package com.example.gestiontrabajadoresiu.data.models.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestiontrabajadoresiu.data.RetrofitClient
import com.example.gestiontrabajadoresiu.data.models.dtos.Trabajador
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TrabajadoresViewModel : ViewModel() {
    private val _trabajadores = MutableStateFlow<List<Trabajador>>(emptyList())
    val trabajadores: StateFlow<List<Trabajador>> = _trabajadores

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        cargarTrabajadores()
    }

    private fun cargarTrabajadores() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitClient.api.listarTrabajadores()
                if (response.isSuccessful) {
                    _trabajadores.value = response.body() ?: emptyList()
                }
            } finally {
                _isLoading.value = false
            }
        }
    }
}