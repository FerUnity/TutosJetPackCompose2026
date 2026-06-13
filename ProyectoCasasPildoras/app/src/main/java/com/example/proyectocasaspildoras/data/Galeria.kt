package com.example.proyectocasaspildoras.data

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun GaleriaCasas(
    navController: NavController,
    casa: Casa
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        CardCasas(
            navController = navController,
            repositorioCasa = RepositorioCasa,
            casa = casa
        )
    }

}
