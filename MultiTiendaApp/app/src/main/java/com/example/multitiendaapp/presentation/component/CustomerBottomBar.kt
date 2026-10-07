package com.example.multitiendaapp.presentation.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.multitiendaapp.navigation.AppRoute

@Composable
fun CustomerBottomBar(
    navController: NavController,
) {
//    Creamos una var que almacene la ruta de la pantalla actual:
    val currentRoute = navController
        .currentBackStackEntryAsState()
        .value
        ?.destination
        ?.route

//    Creamos una lista de rutas(2: CustomerHome y CustomerStores) que rep cada item del boton bar,
//    en que cada elemento es una pantalla de AppRoute:
    val items = listOf(
        AppRoute.CustomerHome,
        AppRoute.CustomerStores
    )

//    Creamos el NavigationBar que es un contenedor visual del bottom bar:
    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,//Esta es la ruta actual que se muestra en el boton bar
                onClick = {
//                   Si la ruta actual es diferente a la ruta del item, entonces navegamos a la ruta del item:
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true //Guardamos el estado de la pantalla actual
                            }
                            launchSingleTop =
                                true //Evita que se creen varias instancias de la misma pantalla
                            restoreState = true //Restaura el estado de la pantalla actual
                        }
                    }
                },
//               signamos el icono de cada boton:
                icon = {
                    when (item) {
                        AppRoute.CustomerHome ->
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = "Home"
                            )

                        AppRoute.CustomerStores ->
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Stores"
                            )

                        else -> {}
                    }

                },
//                Ahora asignamos el label de cada boton:
                label = {
                    when (item) {
                        AppRoute.CustomerHome -> {
                            Text(text = "Inicio")
                        }

                        AppRoute.CustomerStores -> {
                            Text(text = "Tiendas")
                        }

                        else -> {}
                    }
                }
            )
        }
    }

}