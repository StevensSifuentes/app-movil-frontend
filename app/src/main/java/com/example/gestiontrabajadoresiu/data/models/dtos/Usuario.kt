package com.example.gestiontrabajadoresiu.data.models.dtos

data class Usuario(
    val id: Int,
    val nombreUsuario: String,
    val imagenUsuario: String?,
    val email: String
)
