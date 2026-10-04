package com.example.gestiontrabajadoresiu.data.models.dtos

data class UsuarioRegisterRequest(
    val nombreUsuario: String,
    val imagenUsuario: String?,
    val email: String,
    val contrasenia: String
)
