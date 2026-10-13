package com.example.multitiendaapp.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.multitiendaapp.presentation.customer.root.CustomerRootScreen
import com.example.multitiendaapp.presentation.login.LoginScreen
import com.example.multitiendaapp.presentation.login.LoginViewModel
import com.example.multitiendaapp.presentation.registerCustomer.RegisterCustomerScreen
import com.example.multitiendaapp.presentation.registerCustomer.RegisterCustomerViewModel
import com.example.multitiendaapp.presentation.registerSeller.RegisterSellerScreen
import com.example.multitiendaapp.presentation.registerSeller.RegisterSellerViewModel
import com.example.multitiendaapp.presentation.registerStore.RegisterStoreScreen
import com.example.multitiendaapp.presentation.registerStore.RegisterStoreViewModel
import com.example.multitiendaapp.presentation.selectRole.SelectRoleScreen
import com.example.multitiendaapp.presentation.seller.root.SellerRootScreen

//Este sera el mapa de navegacion de la app.
// Aca definimos cual sera la pantalla inicial en mostrarse y las demas pantallas que se mostraran,
// las cuales definimos en la clase AppRoute.kt.

//Entonces definimos la fun composable de navegacion de la app que se llamara AppNavHost.
//Esta fun se llama desde la clase AppRoot.kt, la cual define la pantalla inicial de la app,
// segun el rol del usuario, o bien sera Login.
@Composable
fun AppNavHost(navController: NavHostController, startDestination: String) {
//    Definimos la pantalla inicial con su ruta, la cual sera Login:
    NavHost(
        navController = navController,
        startDestination = startDestination //Aca pasamos la ruta de la pantalla inicial
    // que proviene de AppRoot.kt el cual la define segun el rol del usuario, o bien sera Login.
    ) {
//        Aca definimos las vistas y los param para navegar a ellas:
     /*   composable("login") {
        }
              */
//       Ahora, en vez de escribir el nombre de la Ruta de la pantalla que queremos ir,
        //     usamos el objeto data object Login y su ruta, definidas en AppRoute.com
        //       Y luego definimos la fun composable que queremos que se muestre con esa ruta:
        composable(AppRoute.Login.route) {
//            Creamos el viewmodel para la pantalla de Login usando hiltViewModel()
            //y lo usaremos como parametro de la fun composable LoginScreen():
            val viewModel: LoginViewModel = hiltViewModel()

//           Entonces llamamos a la fun composable que representa esa ruta: LoginScreen()
            LoginScreen(
                viewModel = viewModel,
//                Aca necesitamos llegar a la pantalla de seleccion de Rol: SelectRole,
//                para ello pasamos el navController para navegar a la pantalla de SelectRole.
//                Para lo cual creamos una fun o callback que se llamara onGoToSelectRole:
                onGoToSelectRole = { navController.navigate(AppRoute.SelectRole.route) },
//                Ademas necesitamos navegar a la pantalla de Home segun el Rol(Seller o Customer) del usuario:
                onNavigateByRol = { role ->
//                    Con when evaluamos el rol del usuario y navegamos a la pantalla correspondiente,
                    //  CustomerRoot o SellerRoot:
                   when (role) {
                       "CUSTOMER" -> {
                           navController.navigate(AppRoute.CustomerRoot.route) {
                               popUpTo(AppRoute.Login.route) {
                                   inclusive = true
                                   //Esto es para que no se pueda volver a la pantalla de Login,
                               }
                           }
                       }
                       "SELLER" -> {
                           navController.navigate(AppRoute.SellerRoot.route) {
                               popUpTo(AppRoute.Login.route) {
                                   inclusive = true
                                   //Esto es para que no se pueda volver a la pantalla de Login,
                               }
                           }
                       }
                       //Si no tiene un Rol, Usuario nuevo, o algun error, lo redireccionamos a Login:
                       else -> { navController.navigate(AppRoute.Login.route) }

                   }//Cierre de when()

                }
            )
        }

//        Pantalla de SelectRole:
        composable(AppRoute.SelectRole.route) {
//            Definimos que esa ruta sea la de SelectRole, correspondiente a la fun composable SelectRoleScreen():
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
                    navController.navigate(AppRoute.SellerRoot.route) {
                        popUpTo(AppRoute.RegisterStore.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }



//        Pantalla de SellerHomeScreen, luego de Registrarse o Iniciar sesion.
//        NO VA porque estara dentro del contenedor de SellerRoot:
     /*   composable(AppRoute.SellerHome.route) {
            SellerHomeScreen()
        }*/



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

//                Ahora pasamos como argumento una fun Unit() onNavigateHome(),
//                que nos permite navegar a la pantalla de CustomerRoot.kt,
//                luego de registrarse:
                onNavigateHome = {
                    navController.navigate(AppRoute.CustomerRoot.route) {
//                        Luego para que no se pueda volver a la pantalla de RegisterCustomer, luego de registrarse,
                        //hacemos asi:
                        popUpTo(AppRoute.RegisterCustomer.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

//        Registramos las pantallas root de cliente y vendedor:
        composable(AppRoute.CustomerRoot.route) {
            CustomerRootScreen()
        }

        composable(AppRoute.SellerRoot.route) {
            SellerRootScreen()
        }


//        Pantalla de CustomerHomeScreen, luego de Registrarse o Iniciar sesion.
    //        NO VA porque estara dentro del contenedor de CustomerRoot:
      /*  composable(AppRoute.CustomerHome.route) {
            CustomerHomeScreen()
        }*/

    }

}