package com.example.gestiontrabajadoresiu.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gestiontrabajadoresiu.data.models.viewmodels.CrearTrabajadorUiState
import com.example.gestiontrabajadoresiu.data.models.viewmodels.CrearTrabajadorViewModel
import com.example.gestiontrabajadoresiu.navigation.AppNavigation
import com.example.gestiontrabajadoresiu.ui.theme.GestionTrabajadoresIUTheme

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewCrearTrabajadorScreen(){
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
fun CrearTrabajadorScreen(viewModel: CrearTrabajadorViewModel, onTrabajadorCreado: () -> Unit){
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Nuevo Trabajador",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onTrabajadorCreado) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        FormularioTrabajador(viewModel, onTrabajadorCreado, Modifier.padding(innerPadding), snackbarHostState)
    }
}

@Composable
fun FormularioTrabajador(
    viewModel: CrearTrabajadorViewModel,
    onTrabajadorCreado: () -> Unit,
    modifier: Modifier,
    snackbarHostState: SnackbarHostState
) {
    val nombre by viewModel.nombre.collectAsState()
    val apellido by viewModel.apellido.collectAsState()
    val dni by viewModel.dni.collectAsState()
    val edad by viewModel.edad.collectAsState()
    val genero by viewModel.genero.collectAsState()
    val cargo by viewModel.cargo.collectAsState()
    val formValido by viewModel.formValido.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        if (uiState is CrearTrabajadorUiState.Success) {
            snackbarHostState.showSnackbar("Trabajador registrado con éxito.")
            onTrabajadorCreado()
        }
    }

    Column(modifier) {

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = viewModel::onNombreChanged,
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = apellido,
            onValueChange = viewModel::onApellidoChanged,
            label = { Text("Apellido") },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = dni,
            onValueChange = viewModel::onDniChanged,
            label = { Text("DNI") },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = edad,
            onValueChange = viewModel::onEdadChanged,
            label = { Text("Edad") },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(8.dp))

        GeneroDropdown(
            generoSeleccionado = genero,
            onGeneroSeleccionado = viewModel::onGeneroChanged
        )

        Spacer(modifier = Modifier.height(8.dp))

        CargoDropdown(
            cargoSeleccionado = cargo,
            onCargoSeleccionado = viewModel::onCargoChanged
        )

        Spacer(modifier = Modifier.height(60.dp))

        Button(
            onClick = { viewModel.onAgregarTrabajador() },
            modifier = Modifier.fillMaxWidth().height(52.dp).padding(horizontal = 20.dp, vertical = 6.dp),
            enabled = formValido && uiState !is CrearTrabajadorUiState.Loading,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF05706E),
                disabledContainerColor = Color(0x6F05706E),
                contentColor = Color.White,
                disabledContentColor = Color.White
            ),
        ) {
            if (uiState is CrearTrabajadorUiState.Loading) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
            } else {
                Text("Guardar")
            }
        }

        if (uiState is CrearTrabajadorUiState.Error) {
            Text(
                text = (uiState as CrearTrabajadorUiState.Error).mensaje,
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneroDropdown(generoSeleccionado: String, onGeneroSeleccionado: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val opciones = listOf("Masculino", "Femenino")

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = generoSeleccionado,
            onValueChange = {},
            readOnly = true,
            label = { Text("Género") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp).menuAnchor()
        )

        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            opciones.forEach { opcion ->
                DropdownMenuItem(
                    text = { Text(opcion) },
                    onClick = {
                        onGeneroSeleccionado(opcion)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CargoDropdown(cargoSeleccionado: String, onCargoSeleccionado: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val opciones = listOf("Desarrollador Full Stack", "Desarrollador Backend", "Desarrollador Frontend", "QA", "Analista de Datos", "Arquitecto Cloud", "Administrador de Redes", "Scrum Master")

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = cargoSeleccionado,
            onValueChange = {},
            readOnly = true,
            label = { Text("Cargo/Puesto") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp).menuAnchor()
        )

        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            opciones.forEach { opcion ->
                DropdownMenuItem(
                    text = { Text(opcion) },
                    onClick = {
                        onCargoSeleccionado(opcion)
                        expanded = false
                    }
                )
            }
        }
    }
}