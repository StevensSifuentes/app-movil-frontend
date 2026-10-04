package com.example.gestiontrabajadoresiu.data

import com.example.gestiontrabajadoresiu.data.models.dtos.LoginRequest
import com.example.gestiontrabajadoresiu.data.models.dtos.LoginResponse
import com.example.gestiontrabajadoresiu.data.models.dtos.RegisterTrabajadorRequest
import com.example.gestiontrabajadoresiu.data.models.dtos.Trabajador
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("api/usuarios/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @GET("api/trabajadores")
    suspend fun listarTrabajadores(): Response<List<Trabajador>>

    @POST("api/trabajadores")
    suspend fun crearTrabajador(@Body request: RegisterTrabajadorRequest): Response<Trabajador>
}