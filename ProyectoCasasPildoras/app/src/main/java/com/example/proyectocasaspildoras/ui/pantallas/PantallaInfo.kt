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
fun PantallaInfo(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { /*navController.navigate("bienvenida")*/
                navController.popBackStack() },
            modifier = Modifier.align(Alignment.Start) //Para que el boton se alinee arriba a la izquierda de la pantalla
        ) {
            Text(text = "Volver", style = MaterialTheme.typography.titleSmall)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Desarrollado por Pildoras informaticas", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Version 1.0 de ejemplo para enseñanza de la navegacion", style = MaterialTheme.typography.titleMedium)
    }

}