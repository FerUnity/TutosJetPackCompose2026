package com.example.proyectocasaspildoras.ui.pantallas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Bienvenida() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Bienvenido a la Galeria de Casas")

        Button(
            onClick = { /*TODO*/ }
        ) {
            Text(text = "Ver Galeria")
        }

        Button(
            onClick = { /*TODO*/ }
        ) {
            Text(text = "Sobre la App")
        }
    }
}

