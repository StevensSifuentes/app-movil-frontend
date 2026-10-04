package com.example.gestiontrabajadoresiu.screens

import android.se.omapi.Session
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberTooltipState

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gestiontrabajadoresiu.R
import com.example.gestiontrabajadoresiu.data.RetrofitClient
import com.example.gestiontrabajadoresiu.data.SessionManager
import com.example.gestiontrabajadoresiu.data.models.viewmodels.TrabajadoresViewModel
import com.example.gestiontrabajadoresiu.data.models.dtos.Trabajador
import com.example.gestiontrabajadoresiu.navigation.AppNavigation
import com.example.gestiontrabajadoresiu.ui.theme.GestionTrabajadoresIUTheme

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewTrabajadoresScreen(){
    GestionTrabajadoresIUTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            AppNavigation()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrabajadoresScreen(viewModel: TrabajadoresViewModel = viewModel(), onAgregarTrabajador: () -> Unit, onLogout: () -> Unit) {
    val trabajadores by viewModel.trabajadores.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val context = LocalContext.current
    val nombreUsuario = SessionManager.obtenerNombreUsuario(context) ?: "USER"

    if (isLoading) {
        Box(Modifier.fillMaxSize()) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text("Bienvenido, ${nombreUsuario.uppercase()}", maxLines = 1, overflow = TextOverflow.Ellipsis)
                    },
                    navigationIcon = {
                        IconButton(onClick = { }) {
                            Icon(imageVector = Icons.Filled.AccountCircle, contentDescription = "Imagen de Usuario")
                        }
                    },
                    actions = {
                        TooltipBox(
                            positionProvider =
                                TooltipDefaults.rememberTooltipPositionProvider(
                                    TooltipAnchorPosition.Above
                                ),
                            tooltip = { PlainTooltip { Text("Logout") } },
                            state = rememberTooltipState(),
                        ) {
                            IconButton(onClick = onLogout) {
                                Icon(imageVector = Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Cerrar sesión")
                            }
                        }
                    },
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = onAgregarTrabajador,
                    containerColor = Color(0xFF05706E),
                    contentColor = Color.White,
                    shape = RoundedCornerShape(50.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Agregar trabajador"
                    )
                }
            }
        ) { innerPadding ->
            SearchView(trabajadores, Modifier.padding(innerPadding))
        }
    }
}

@Composable
fun SearchView(
    trabajadores: List<Trabajador>,
    modifier: Modifier
){
    var textSearch by remember { mutableStateOf("") }

    Column(
        modifier
    ){
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            value = textSearch,
            onValueChange = {
                textSearch = it
            },
            placeholder =  {
                Text(text = "DNI a buscar")
            },
            maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(
                color = Color.Black, fontSize = 20.sp
            ),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null
                )
            },
            shape = RoundedCornerShape(8.dp),
        )

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(trabajadores.filter { it.dni.contains(textSearch, ignoreCase = true) }, key = { it.id })
            {
                TrabajadorCard(it)
            }
        }
    }
}

@Composable
fun TrabajadorCard(trabajador: Trabajador) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(
                    id = if (trabajador.genero == "Masculino") R.drawable.menavatar else R.drawable.womanavatar
                ),
                contentDescription = "Avatar de ${trabajador.nombre}",
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${trabajador.nombre} ${trabajador.apellido}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = trabajador.cargo ?: "Sin cargo asignado",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Género: ${trabajador.genero}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    Text(
                        text = "Edad: ${trabajador.edad}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
                Text(
                    text = "DNI: ${trabajador.dni}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
            Icon(
                imageVector = if (trabajador.estado) Icons.Default.CheckCircle else Icons.Default.Cancel,
                contentDescription = "Activo",
                tint = if (trabajador.estado) Color(0xFF4CAF50) else Color.Red
            )
        }
    }
}