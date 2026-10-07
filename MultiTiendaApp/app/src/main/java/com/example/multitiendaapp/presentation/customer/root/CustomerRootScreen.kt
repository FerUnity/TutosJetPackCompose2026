package com.example.multitiendaapp.presentation.customer.root

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.multitiendaapp.navigation.AppRoute
import com.example.multitiendaapp.presentation.component.CustomerBottomBar
import com.example.multitiendaapp.presentation.customer.home.CustomerHomeScreen
import com.example.multitiendaapp.presentation.customer.stores.CustomerStoresScreen

//Este composable se invoca cuando el usuario registrado es un customer.
// Este composable es el contenedor de la pantalla de inicio del cliente
// y lo que hace es crear un navController local para navegar entre pantallas,
// y un Scaffold para mostrar el BottomBar donde se muestran las pestañas Home y Stores.
// La idea es que cada vez que se presione una de las pestañas, se navegue a la pantalla correspondiente.
@Composable
fun CustomerRootScreen() {
//    Creamos un navController local para navegar entre pantallas:
    val navController = rememberNavController()

//    Creamos un Scaffold para mostrar el BottomBar: CustomerBottomBar() que creamos como componente,
//    para visualizar las pestañas Home y Stores:
    Scaffold(
        bottomBar = {
            CustomerBottomBar(navController = navController)
        }
    ) {
        paddingValues ->
        NavHost(
            navController = navController,
            startDestination = AppRoute.CustomerHome.route,
            modifier = Modifier.padding(paddingValues)
        ){
//            Aca registramos las pantallas con sus rutas, que queremos navegar entre cuando el ususario registrado es un customer,
            //            las pantallas son CustomerHomeScreen y CustomerStoresScreen:
            composable(AppRoute.CustomerHome.route){
//                Y que se pinte la pantalla CustomerHomeScreen() que creamos como vista:
                CustomerHomeScreen()
            }
            composable(AppRoute.CustomerStores.route){
                CustomerStoresScreen()
            }
        }
    }
    }
