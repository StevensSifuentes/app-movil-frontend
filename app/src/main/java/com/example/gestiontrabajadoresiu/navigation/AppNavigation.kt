package com.example.gestiontrabajadoresiu.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gestiontrabajadoresiu.data.models.viewmodels.CrearTrabajadorViewModel
import com.example.gestiontrabajadoresiu.data.RetrofitClient
import com.example.gestiontrabajadoresiu.data.SessionManager
import com.example.gestiontrabajadoresiu.data.models.viewmodels.TrabajadoresViewModel
import com.example.gestiontrabajadoresiu.screens.CrearTrabajadorScreen
import com.example.gestiontrabajadoresiu.screens.LoginScreen
import com.example.gestiontrabajadoresiu.screens.TrabajadoresScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current

    RetrofitClient.authToken = SessionManager.obtenerToken(context)

    val startDestination = if (SessionManager.haySesionActiva(context)) {
        Screens.TrabajadoresScreen.route
    } else {
        Screens.LoginScreen.route
    }

    NavHost(navController = navController, startDestination = startDestination){
        composable(Screens.LoginScreen.route){
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screens.TrabajadoresScreen.route){
                        // evita que el usuario vuelva al login con el botón "atrás"
                        popUpTo(Screens.LoginScreen.route) { inclusive = true }
                    }
                }
            )
        }

       composable(Screens.TrabajadoresScreen.route){
            TrabajadoresScreen(
                TrabajadoresViewModel(),
                onAgregarTrabajador = {
                    navController.navigate(Screens.CrearTrabajadorScreen.route)
                },
                onLogout = {
                    SessionManager.cerrarSesion(context)
                    RetrofitClient.authToken = null
                    RetrofitClient.nombreUsuarioActual = null
                    navController.navigate(Screens.LoginScreen.route) {
                        popUpTo(0) // limpia todo el back stack
                    }
                }
            )
       }

        composable(Screens.CrearTrabajadorScreen.route){
            CrearTrabajadorScreen(
                CrearTrabajadorViewModel(),
                onTrabajadorCreado = {
                    navController.popBackStack()
                }
            )
        }
    }
}