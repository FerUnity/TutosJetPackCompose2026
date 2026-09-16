package com.example.multitiendaapp.presentation.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.multitiendaapp.navigation.AppNavHost

//Este composable se encargara de arrancar la interfaz de la app y decidira que pantalla mostrara segun su rol,
//cuando el usuario se loguee.
// Por ende esta fun se llamara desde la clase MainActivity.kt:

//Como esta fun es composable puede dibujar una interfaz de usuario en la pantalla,
// pero en este caso es un composable contenedor que organizara los otros composables segun el usuario,
// asi que no es una pantalla, decidira el startDestination segun su rol, usando el viewModel de AppViewModel.kt:

@Composable
fun AppRoot(
    viewModel: AppViewModel = hiltViewModel()
) {
//    Observar los estados del viewmodel:
    val startDestination by viewModel.startDestination.collectAsState()

//    Evaluamos que valor tendra startDestination o bien sera null(aun calculando a que pantalla ira)
    if (startDestination == null) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            //Se mostrara un circulo de progreso hasta que se calcule el startDestination:
            CircularProgressIndicator()
        }
//        Luego con return, evitamos que se ejecute el resto del codigo hasta que se calcule el startDestination:
        return

    }

//    Luego cuando tengamos el valor de startDestination, lo enviamos a la pantalla correspondiente.
//    Usamos el navController porque es el que nos permite navegar entre pantallas,
//    contiene el backstack y sabe en que pantalla estamos
//    y con el remember permanene en memoria la pantalla entre re-composiciones:
    val navController = rememberNavController()

//    Ahora con AppNavHost() que es el que nos permite navegar entre pantallas,
//    le pasamos el navController y el startDestination:
    AppNavHost(
        navController = navController,
        startDestination = startDestination!!
    )


}