package com.example.swiperefresh.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewmodel
) {
    val state = viewModel.state

    // PullToRefreshBox ya incluye el indicador visual (la ruedita)
    // Se posiciona automáticamente en la parte superior central.
    PullToRefreshBox(
        isRefreshing = state.isLoading,
        onRefresh = { viewModel.updateCurrentValue() },
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Usamos Column para que los elementos no se superpongan
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Valor actual: ${state.currentValue}"
            )

            Spacer(modifier = Modifier.height(15.dp))

            Button(
                onClick = {
//                Al hacer click queremos ir a la pantalla de ListadoNumerico:
                navController.navigate("listadoNumerico")

            },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFC107),  // Amarillo
                    contentColor = Color(0xFF7B001A)     // Rojo Arte Mayor
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
            ) {
                Text(text = "Listado Numerico")
            }
        }
    }
}
