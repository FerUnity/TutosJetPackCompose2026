package com.example.proyectocasaspildoras.ui.pantallas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun Bienvenida(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Bienvenido a la Galeria de Casas",
            style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { navController.navigate("galeria") }
        ) {
            Text(text = "Ver Galeria", style = MaterialTheme.typography.titleMedium)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {navController.navigate("info") }
        ) {
            Text(text = "Sobre la App", style = MaterialTheme.typography.titleSmall)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {navController.navigate("registro") }
        ) {
            Text(text = "Registrar nueva Casa", style = MaterialTheme.typography.titleMedium)
        }
    }
}

