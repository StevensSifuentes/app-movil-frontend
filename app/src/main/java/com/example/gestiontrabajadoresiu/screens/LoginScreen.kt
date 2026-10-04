package com.example.gestiontrabajadoresiu.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gestiontrabajadoresiu.R
import com.example.gestiontrabajadoresiu.data.models.viewmodels.LoginUiState
import com.example.gestiontrabajadoresiu.data.models.viewmodels.LoginViewModel
import com.example.gestiontrabajadoresiu.navigation.AppNavigation
import com.example.gestiontrabajadoresiu.ui.theme.GestionTrabajadoresIUTheme


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewLoginScreen(){
    GestionTrabajadoresIUTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            AppNavigation()
        }
    }
}

@Composable
fun LoginScreen(viewModel: LoginViewModel = viewModel(), onLoginSuccess: () -> Unit) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Login(Modifier.align(Alignment.Center), viewModel, onLoginSuccess)
    }
}

@Composable
fun Login(modifier: Modifier, viewModel: LoginViewModel, onLoginSuccess: () -> Unit) {
    val email: String by viewModel.email.collectAsState()
    val password: String by viewModel.password.collectAsState()
    val loginEnable: Boolean by viewModel.loginEnable.collectAsState()
    val uiState : LoginUiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        if(uiState is LoginUiState.Success){
            onLoginSuccess()
        }
    }

    Column(modifier = modifier) {
        HeaderImage(Modifier.align(Alignment.CenterHorizontally))
        Spacer(modifier = Modifier.padding(16.dp))
        EmailField(email) { viewModel.onLoginChanged(it, password) }
        Spacer(modifier = Modifier.padding(4.dp))
        PasswordField(password) { viewModel.onLoginChanged(email, it) }
        Spacer(modifier = Modifier.padding(8.dp))
        ForgotPassword(Modifier.align(Alignment.End))
        Spacer(modifier = Modifier.padding(16.dp))
        LoginButton(
            loginEnable = loginEnable && uiState !is LoginUiState.Loading,
            onLoginSelected = { viewModel.onLoginSelected() }
        )
    }
    when (val state = uiState){
        is LoginUiState.Loading -> Box(Modifier.fillMaxSize())
        {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
        is LoginUiState.Error -> Text(
            text = state.mensaje,
            color = Color.Red,
            modifier = Modifier.padding(top = 8.dp)
        )
        else -> Unit
    }
}

@Composable
fun HeaderImage(modifier: Modifier) {
    Image(
        painter = painterResource(id = R.drawable.login),
        contentDescription = "Header",
        modifier = modifier
    )
}

@Composable
fun EmailField(email: String, onTextFieldChanged: (String) -> Unit) {
    TextField(
        value = email,
        onValueChange = { onTextFieldChanged(it) },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = "Email") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        singleLine = true,
        maxLines = 1,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun PasswordField(password: String, onTextFieldChanged: (String) -> Unit) {
    var passwordVisible by remember { mutableStateOf(false) }
    TextField(
        value = password,
        onValueChange = { onTextFieldChanged(it) },
        placeholder = { Text(text = "Contraseña") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        singleLine = true,
        maxLines = 1,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        visualTransformation = if (passwordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        trailingIcon = {
            val icon = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = icon, contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña")
            }
        },
    )
}

@Composable
fun ForgotPassword(modifier: Modifier) {
    Text(
        text = "¿Olvidaste la contraseña?",
        modifier = modifier.clickable { },
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF0AC4C1)
    )
}

@Composable
fun LoginButton(loginEnable: Boolean, onLoginSelected: () -> Unit) {
    Button(
        onClick = { onLoginSelected() },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF05706E),
            disabledContainerColor = Color(0x6F05706E),
            contentColor = Color.White,
            disabledContentColor = Color.White
        ),
        enabled = loginEnable
    ) {
        Text(text = "Iniciar sesión", fontSize = 16.sp)
    }
}

