package com.example.swiperefresh.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun ListadoNumerico(
    navController: NavHostController,
    viewModel: HomeViewmodel
){
    val state = viewModel.state
    val elements = (1..100).map {"Item $it"}

    // Hacemos el indicador visual o el loading:
    // PullToRefreshBox ya incluye el indicador visual (la ruedita)
    // Se posiciona automáticamente en la parte superior central.
    PullToRefreshBox(
        isRefreshing = state.isLoading,
        onRefresh = { viewModel.updateCurrentValue() },
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 25.dp)
            .background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                // IMPORTANTE: El contenido debe tener scroll para que el gesto funcione,
        // pero el LazyColumn ya tiene scroll, asi que no hace falta añadirlo:
//                .verticalScroll(rememberScrollState())
        )
        {
           LazyColumn(
               modifier = Modifier.fillMaxWidth(),
               contentPadding = PaddingValues(16.dp),
               verticalArrangement = Arrangement.spacedBy(16.dp)
           ){
       //Ahora por cada item se muestra un texto con el valor de cada elemento del listado elements:
               items(elements){
                   Text(text = it)
               }
           }

            Spacer(modifier = Modifier.height(15.dp))

            Button(onClick = {
//                Al hacer click queremos ir a la pantalla de ListadoNumerico:
                navController.navigate("home")

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
                Text(text = "Home")
            }
        }
    }

}