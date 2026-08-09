package com.example.multitiendaapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.multitiendaapp.presentation.login.LoginScreen
import com.example.multitiendaapp.presentation.registerCustomer.RegisterCustomerScreen
import com.example.multitiendaapp.presentation.registerSeller.RegisterSellerScreen
import com.example.multitiendaapp.presentation.selectRole.SelectRoleScreen

//Este sera el mapa de navegacion de la app.
// Aca definimos cual sera la pantalla inicial en mostrarse y cuales seran las demas,
// de las pantallas que definimos en la clase AppRoute.
//Definimos la fun composable de navegacion de la app que se llamara AppNavHost.
//Esta fun se llama desde la clase MainActivity.kt
@Composable
fun AppNavHost(navController: NavHostController){
//    Definimos la pantalla inicial con suruta, la cual sera Login:
    NavHost(navController = navController, startDestination = AppRoute.Login.route){
//        Aca definimos las vistas y los param para navegar entre ellas:
        composable(AppRoute.Login.route){
//            Llamamos a la fun composable que representa esa ruta: LoginScreen()
            LoginScreen(
//                Aca necesitamos llegar a la pantalla de SelectRole,
//                para ello pasamos el navController para navegar a la pantalla de SelectRole:
                onGoToSelectRole = { navController.navigate(AppRoute.SelectRole.route) }
            )
        }
        composable(AppRoute.SelectRole.route){
            SelectRoleScreen(
//                Aca podemos ir a las pantallas de RegisterSeller o RegisterCustomer,
//                para ello necesitamos pasarle el navController para navegar a las 2 pantallas:
                onGoToRegisterSeller = { navController.navigate(AppRoute.RegisterSeller.route) },
                onGoToRegisterCustomer = { navController.navigate(AppRoute.RegisterCustomer.route) }
            )
        }
        composable(AppRoute.RegisterSeller.route){
            RegisterSellerScreen(
//                Aca necesitamos poder volver a la pantalla anterior:
                onBack = { navController.popBackStack() }
            )
        }

        composable(AppRoute.RegisterCustomer.route){
            RegisterCustomerScreen(
//                Aca necesitamos poder volver a la pantalla anterior:
                onBack = { navController.popBackStack() }
            )
        }


    }

}