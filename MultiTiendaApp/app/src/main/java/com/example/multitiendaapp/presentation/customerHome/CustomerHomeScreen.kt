package com.example.multitiendaapp.presentation.customerHome

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

// Vista de la pantalla de inicio del cliente, luego de Registrarse o Iniciar sesion:
@Composable
fun CustomerHomeScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    )
    {
        Text(
            text = "Home Customer Screen",
            style = MaterialTheme.typography.headlineLarge
        )
    }


}