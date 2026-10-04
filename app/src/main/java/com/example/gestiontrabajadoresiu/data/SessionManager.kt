package com.example.gestiontrabajadoresiu.data

import android.content.Context
import android.content.SharedPreferences

object SessionManager {
    private const val PREFS_NAME = "session_prefs"
    private const val KEY_TOKEN = "token"
    private const val KEY_USUARIO_ID = "usuario_id"
    private const val KEY_NOMBRE_USUARIO = "nombre_usuario"

    private fun prefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun guardarSesion(context: Context, token: String, usuarioId: Int, nombreUsuario: String) {
        prefs(context).edit().apply {
            putString(KEY_TOKEN, token)
            putInt(KEY_USUARIO_ID, usuarioId)
            putString(KEY_NOMBRE_USUARIO, nombreUsuario)
            apply()
        }
    }

    fun obtenerToken(context: Context): String? {
        return prefs(context).getString(KEY_TOKEN, null)
    }

    fun obtenerNombreUsuario(context: Context): String? {
        return prefs(context).getString(KEY_NOMBRE_USUARIO, null)
    }

    fun obtenerUsuarioId(context: Context): Int {
        return prefs(context).getInt(KEY_USUARIO_ID, -1)
    }

    fun haySesionActiva(context: Context): Boolean {
        return obtenerToken(context) != null
    }

    fun cerrarSesion(context: Context) {
        prefs(context).edit().clear().apply()
    }
}