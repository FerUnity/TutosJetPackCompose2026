package com.example.proyectocasaspildoras.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.proyectocasaspildoras.ui.pantallas.GaleriaCasas
import com.example.proyectocasaspildoras.ui.pantallas.Bienvenida
import com.example.proyectocasaspildoras.ui.pantallas.DetalleCasas
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

//        Ahora la fun composable DetalleCasas:
//        Pero en la ruta se debe agregar el id de la casa que queremos mostrar.
//        Para eso debemos indicar en la ruta, que busque el argumento "id" en la ruta y que sea de tipo Int.
//        Este arguneto id es recibido por la fun DetalleCasas, desde la fun Galeria(a traves de la fun CardCasas())
//        y se usara para decir a la fun DetalleCasas que casa mostrar, segun ese id recibido:
        composable("detalle/{id}", arguments =
            listOf(navArgument("id")
            { type = NavType.IntType })
        )
        {
//          Para lograr lo anterior, hacemos que con el sgte codigo:
            //el parametro id especificado en la ruta, recibido por la fun DetalleCasas,
            // se almacene en la variable it, y con it.arguments?.getInt("id")?. se obtiene el valor del argumento "id" de la ruta.
          // para la fun DetalleCasas muestre la casa correspondiente a ese parametro id (1,2,3):
            val id = it.arguments?.getInt("id") ?: 0 //Elvis por si el id es null.
            DetalleCasas(
                navController = navController,
                casaId = id //Aca pasamos el id al parametro casaId de la fun DetalleCasas,
            // para que esta fun DetalleCasas, muestre la casa correspondiente a ese id.
            )
        }

        }

    }