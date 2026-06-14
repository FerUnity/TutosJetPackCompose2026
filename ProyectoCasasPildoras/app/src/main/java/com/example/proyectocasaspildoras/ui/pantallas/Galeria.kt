package com.example.proyectocasaspildoras.ui.pantallas

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyectocasaspildoras.data.CardCasas
import com.example.proyectocasaspildoras.data.RepositorioCasa

@Composable
fun GaleriaCasas(
    navController: NavController
) {
/*    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        CardCasas(
            navController = navController,
            repositorioCasa = RepositorioCasa,
            casa = casa
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {navController.navigate("bienvenida")}
        )
        {
            Text(text = "Volver al inicio")
        }
    }*/

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(8.dp)
    ) {
//        Una separacion del borde superior de la pantalla:
        item {
            Spacer(modifier = Modifier.height(32.dp))
        }


//        Aqui van las casas de la lista de casas que estan en el object RepositorioCasa del data class Casa.
//        Se usa items porque es una lista de elementos y no un solo elemento:
        items(RepositorioCasa.listaCasas.size) {
            index: Int -> val casa = RepositorioCasa.listaCasas[index]
            CardCasas(
                navController = navController,
                casa = casa
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navController.navigate("bienvenida") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(text = "Volver al inicio")
            }
        }
    }

}
