package com.example.gestiontrabajadoresiu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.gestiontrabajadoresiu.data.RetrofitClient
import com.example.gestiontrabajadoresiu.data.SessionManager
import com.example.gestiontrabajadoresiu.navigation.AppNavigation
import com.example.gestiontrabajadoresiu.ui.theme.GestionTrabajadoresIUTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GestionTrabajadoresIUTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}