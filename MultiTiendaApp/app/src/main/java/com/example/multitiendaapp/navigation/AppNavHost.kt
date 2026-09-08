package com.example.multitiendaapp.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.multitiendaapp.presentation.customerHome.CustomerHomeScreen
import com.example.multitiendaapp.presentation.login.LoginScreen
import com.example.multitiendaapp.presentation.registerCustomer.RegisterCustomerScreen
import com.example.multitiendaapp.presentation.registerCustomer.RegisterCustomerViewModel
import com.example.multitiendaapp.presentation.registerSeller.RegisterSellerScreen
import com.example.multitiendaapp.presentation.registerSeller.RegisterSellerViewModel
import com.example.multitiendaapp.presentation.registerStore.RegisterStoreScreen
import com.example.multitiendaapp.presentation.registerStore.RegisterStoreViewModel
import com.example.multitiendaapp.presentation.selectRole.SelectRoleScreen
import com.example.multitiendaapp.presentation.sellerHome.SellerHomeScreen

//Este sera el mapa de navegacion de la app.
// Aca definimos cual sera la pantalla inicial en mostrarse y cuales seran las demas,
// de las pantallas que definimos en la clase AppRoute.
//Definimos la fun composable de navegacion de la app que se llamara AppNavHost.
//Esta fun se llama desde la clase MainActivity.kt
@Composable
fun AppNavHost(navController: NavHostController) {
//    Definimos la pantalla inicial con su ruta, la cual sera Login:
    NavHost(navController = navController, startDestination = AppRoute.Login.route) {
//        Aca definimos las vistas y los param para navegar a ellas:
        composable(AppRoute.Login.route) {
//            Llamamos a la fun composable que representa esa ruta: LoginScreen()
            LoginScreen(
//                Aca necesitamos llegar a la pantalla de SelectRole,
//                para ello pasamos el navController para navegar a la pantalla de SelectRole.
//                Para lo cual creamos una fun o callback que se llamara onGoToSelectRole:
                onGoToSelectRole = { navController.navigate(AppRoute.SelectRole.route) }
            )
        }

//        Pantalla de SelectRole:
        composable(AppRoute.SelectRole.route) {
            SelectRoleScreen(
//                Aca podemos ir a las pantallas de RegisterSeller o RegisterCustomer,
//                para ello necesitamos pasarle el navController para navegar a las 2 pantallas:
                onGoToRegisterSeller = { navController.navigate(AppRoute.RegisterSeller.route) },
                onGoToRegisterCustomer = { navController.navigate(AppRoute.RegisterCustomer.route) }
            )
        }

//        Pantalla de registro del vendedor: RegisterSeller:
        composable(AppRoute.RegisterSeller.route) {
            val viewModel: RegisterSellerViewModel = hiltViewModel()
            RegisterSellerScreen(
                viewModel = viewModel, //Aca pasamos el viewmodel para configurar el boton hacia atras y para registrarse.
//                Aca necesitamos 2 parametros:
//                Poder volver a la pantalla anterior
//                y poder ir a la pantalla de reg de tienda:
                onBack = { navController.popBackStack() },
                onFinishRegisterSeller = { sellerUid ->
                    navController.navigate(AppRoute.RegisterStore.createRoute(sellerUid))
                }
            )
        }

//        Pantalla de registro de tienda: RegisterStore:
        composable(
            AppRoute.RegisterStore.route,
            arguments = listOf(navArgument(AppRoute.RegisterStore.ARG_SELLER_UID) {
                type = NavType.StringType
                nullable = false //Este argumento no puede ser nulo
            })
        ) {
            val viewModel: RegisterStoreViewModel = hiltViewModel()
            RegisterStoreScreen(
                viewModel = viewModel,
//                Aca necesitamos el viewmodel para configurar el boton hacia atras.

//                Aca pasamos como argumento una fun onNavigateHome, que nos permite navegar a la pantalla de SellerHomeScreen,
//                luego de registrarse:
                onNavigateHome = {
//                    Para ir a la pantalla de SellerHomeScreen, luego de registrarse,
                    navController.navigate(AppRoute.SellerHome.route) {
                        //Luego para que no se pueda volver a la pantalla de RegisterSeller, luego de registrarse,
                        //hacemos asi:
                        popUpTo(AppRoute.RegisterSeller.route) {
                            inclusive = true
                        }
                    }

                }
            )
        }


//        Pantalla de SellerHomeScreen, luego de Registrarse o Iniciar sesion:
        composable(AppRoute.SellerHome.route) {
            SellerHomeScreen()
        }



//        Pantalla de RegisterCustomer:
        composable(AppRoute.RegisterCustomer.route) {
            val viewModel: RegisterCustomerViewModel = hiltViewModel()
//            El viewmodel lo usamos para configurar el boton hacia atras.
//            Para eso necesitamos acceder a la pila de pantallas o stack de navegacion(back Stack),
//            donde android guarda el historial de navegacion o pantallas visitadas,
//            sirve para saber a que pantalla volver o navegar luego de presionar el boton de hacia atras.
//            Entonces agregamos como parametro el viewmodel, a la fun composable RegisterCustomerScreen():
            //Aca llamamos a la fun composable RegisterCustomerScreen()
            RegisterCustomerScreen(
                viewModel = viewModel,
//                Aca necesitamos poder volver a la pantalla anterior:
                onBack = { navController.popBackStack() },
//                Aca pasamos como argumento una fun onNavigateHome, que nos permite navegar a la pantalla de CustomerHomeScreen,
//                luego de registrarse:
                onNavigateHome = {
                    navController.navigate(AppRoute.CustomerHome.route) {
//                        Luego para que no se pueda volver a la pantalla de RegisterCustomer, luego de registrarse,
                        //hacemos asi:
                        popUpTo(AppRoute.RegisterCustomer.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

//        Pantalla de CustomerHomeScreen, luego de Registrarse o Iniciar sesion:
        composable(AppRoute.CustomerHome.route) {
            CustomerHomeScreen()
        }

    }

}