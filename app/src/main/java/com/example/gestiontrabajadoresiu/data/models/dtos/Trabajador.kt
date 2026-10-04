package com.example.gestiontrabajadoresiu.data.models.dtos

import java.util.Date

data class Trabajador(
    val id: Int,
    val usuarioId: Int,
    val nombre: String,
    val apellido: String,
    val dni: String,
    val edad: Int,
    val genero: String,
    val cargo: String?,
    val estado: Boolean,
    )
