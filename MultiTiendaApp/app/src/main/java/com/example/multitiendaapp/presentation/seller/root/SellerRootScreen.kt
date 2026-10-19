package com.example.multitiendaapp.presentation.seller.root

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.multitiendaapp.navigation.AppRoute
import com.example.multitiendaapp.presentation.component.SellerBottomBar
import com.example.multitiendaapp.presentation.seller.categories.form.SellerCategoryFormScreen
import com.example.multitiendaapp.presentation.seller.categories.form.SellerCategoryFormViewModel
import com.example.multitiendaapp.presentation.seller.categories.list.SellerCategoriesScreen
import com.example.multitiendaapp.presentation.seller.home.SellerHomeScreen

//Este composable se invoca cuando el usuario registrado es un vendedor.
// Este composable es el contenedor de la pantalla de inicio del vendedor
// y de las pantallas que tienen que ver connel vendedor,
// y lo que hace es crear un navController local para navegar entre esas pantallas,
// y un Scaffold para mostrar el BottomBar donde se muestran las pestañas SellerHome y SellerCategories.
// La idea es que cada vez que se presione una de las pestañas, se navegue a la pantalla correspondiente.

@Composable
fun SellerRootScreen() {
    //    Creamos un navController local para navegar entre pantallas:
    val navController = rememberNavController()

    //    Creamos un Scaffold para mostrar el BottomBar: SellerBottomBar() que creamos como componente,
    //    para visualizar las pestañas SellerHome y SellerCategories:
    Scaffold(
        bottomBar = {
            SellerBottomBar(navController = navController)
        }
    ) { paddingValues ->
//        El NavHost en Jetpack Compose es el contenedor visual y de diseño principal
        // que aloja y muestra las diferentes pantallas (elementos Composable)
        // de una aplicación según la ruta o el estado actual de la navegación,
        // en este caso muestra las pantallas SellerHomeScreen, SellerCategoriesScreen y SellerCategoryFormScreen
        // porque corresponden a las pantallas que se mostrarán para el usuario registrado como vendedor.
        NavHost(
            navController = navController,
            startDestination = AppRoute.SellerHome.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            //  Aca registramos las pantallas con sus rutas, que queremos navegar entre cuando el ususario registrado es un vendedor,
            //  las pantallas son SellerHomeScreen y SellerCategoriesScreen, por ahora:

            //Pantalla de inicio del vendedor, luego de Registrarse o Iniciar sesion:
            composable(AppRoute.SellerHome.route) {
                //Y que se pinte la pantalla SellerHomeScreen() que creamos como vista,
                // que es la bienvenida del vendedor:
                SellerHomeScreen()
            }

//            Pantalla donde se mostraran las categorias de las tiendas registradas:
            composable(AppRoute.SellerCategoriesList.route) {
                // Y que se pinte la pantalla SellerCategoriesScreen() que creamos como vista,
                // en donde se mostraran las categorias de las tiendas registradas:
                SellerCategoriesScreen(
//                    Creamos un callback para llamar a la fun composable SellerCategoryFormScreen() de mas abajo,
//                    que es para ir al formulario de creacion de categorias:
                    onGoToForm = {
                        navController.navigate(AppRoute.SellerCategoryForm.route)
                    }
                )
            }

//            Ahora agregamos la vista del formulario de creacion de categorias:
            composable(AppRoute.SellerCategoryForm.route) { backStackEntry ->
                val viewModel: SellerCategoryFormViewModel = hiltViewModel(backStackEntry)
//                El backStackEntry es un objeto que representa la entrada en la pila o historial de navegación.
//                El hiltViewModel() es una función que se utiliza para obtener un ViewModel desde Hilt y asociarlo a una entrada en la pila de navegación.

                SellerCategoryFormScreen(
                    viewModel = viewModel,
                    onBack = {
                        navController.popBackStack()
                        //Para navegar hacia atras, o sea volver a la pantalla anterior.
                    // Esta pantalla sera la lista de categorias de las tiendas registradas, o sea SellerCategoriesScreen()
                    }
                )
            }
        }
    }

}