package com.example.gestiontrabajadoresiu.data.models.dtos

data class RegisterTrabajadorRequest(
    val nombre: String,
    val apellido: String,
    val dni: String,
    val edad: Int,
    val genero: String,
    val cargo: String?
)
