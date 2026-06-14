package com.example.proyectocasaspildoras.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyectocasaspildoras.data.Casa
import com.example.proyectocasaspildoras.data.GaleriaCasas
import com.example.proyectocasaspildoras.ui.pantallas.Bienvenida
import com.example.proyectocasaspildoras.ui.pantallas.PantallaInfo

@Composable
fun AppNavigation(modifier: Modifier) {
//   Creamos var de navController para poder navegar entre pantallas.
    //    rememberNavController() es una función que devuelve un NavController que se utilizará para navegar entre pantallas,
    //    el remember se usa para que el NavController se mantenga en memoria y no se elimine al girar la pantalla,
    //    ademas no se pierde el estado de la navegación al ir a las otras pantallas.
    val navController = rememberNavController()

//    Luego creamos el NavHost con el navController y la lista de pantallas que queremos navegar.
//    En el NavHost tenemos que especificar la pantalla inicial que queremos mostrar
//    y luego entre las {} podemos definir las pantallas entre las cuales queremos navegar,
//    definiendo el nombre de la ruta y el contenido que queremos mostrar relacionando la función composable
//    que recibe el nombre de esa ruta:
    NavHost(navController = navController, startDestination = "bienvenida") {
//   En cada coposable se hae la relacion entre el nombre de la ruta y la fun que esta en el contenido que queremos mostrar:
        composable("bienvenida") {
            Bienvenida(
                navController = navController
            )
        }

        composable("info") {
            PantallaInfo(navController = navController)

        }

        composable("galeria"){
            GaleriaCasas(
                navController = navController
            )
        }

    }
}