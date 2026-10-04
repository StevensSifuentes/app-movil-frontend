package com.example.gestiontrabajadoresiu.data.models.dtos

data class LoginResponse(
    val token: String,
    val usuario: Usuario
)
