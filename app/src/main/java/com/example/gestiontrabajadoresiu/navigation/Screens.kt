package com.example.gestiontrabajadoresiu.navigation

sealed class Screens(val route: String) {
    object LoginScreen: Screens("login")
    object TrabajadoresScreen: Screens("trabajadores")
    object CrearTrabajadorScreen: Screens("crearTrabajador")
}