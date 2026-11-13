package com.example.multitiendaapp.presentation.seller.root

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.multitiendaapp.navigation.AppRoute
import com.example.multitiendaapp.presentation.component.SellerBottomBar
import com.example.multitiendaapp.presentation.seller.categories.form.SellerCategoryFormScreen
import com.example.multitiendaapp.presentation.seller.categories.form.SellerCategoryFormViewModel
import com.example.multitiendaapp.presentation.seller.categories.list.SellerCategoriesListViewModel
import com.example.multitiendaapp.presentation.seller.categories.list.SellerCategoriesScreen
import com.example.multitiendaapp.presentation.seller.home.SellerHomeScreen

//Este composable se invoca cuando el usuario registrado es un vendedor.
// Este composable es el contenedor de las pantallas que tienen que ver con el vendedor,
// como la pantalla de inicio del vendedor, la pantalla de lista de categorias y la pantalla de creacion de categorias.
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

//            Pantalla donde se mostrara la lista de categorias, segun cada tienda registrada:
            composable(AppRoute.SellerCategoriesList.route) { backStackEntry ->
                val viewModel: SellerCategoriesListViewModel = hiltViewModel(backStackEntry)
                //El backStackEntry es un objeto que representa la entrada en la pila o historial de navegación.
                // El hiltViewModel() es una función que se utiliza para obtener un ViewModel desde Hilt
                // y asociarlo a una entrada en la pila de navegación.


                // Aca pintamos la pantalla SellerCategoriesScreen() que creamos como vista,
                // en donde se mostraran la lista de categorias, segun cada tienda registrada:
                SellerCategoriesScreen(
//                    Usamos al viewModel como parametro para obtener la lista de categorias, para mostrarlos en la pantalla:
                    viewModel = viewModel,
//                    Creamos un callback para llamar a la fun composable SellerCategoryFormScreen() de mas abajo,
//                    que es para ir al formulario de creacion de categorias:
//                    Aca recibimos como parametro el id de la categoria que se selecciono, la cual puede ser nulo:
//                    Por eso llamamos a la fun createRoute() de SellerCategoryForm.kt,
//                    que gestiona si se crea una nueva categoria o se edita una existente,
//                    segun exista o no el parametro categoryId:
                    onGoToForm = {categoryId ->
                        navController.navigate(AppRoute.SellerCategoryForm.createRoute(categoryId))
                    }
                )
            }

//            Ahora agregamos la vista del formulario de creacion de categorias:
            composable(
                route = AppRoute.SellerCategoryForm.route,
//                Ahora agregamos una lista de argumentos que recibira esta pantalla, con
//                los argumentos de navegacion que definimos en SellerCategoryForm.kt:,
                arguments = listOf(
                    navArgument(AppRoute.SellerCategoryForm.ARG_CATEGORY_ID) {
//                        Ahora definimos el tipo de argumento que recibira esta pantalla,
                        //que sera un String que indicara el id de la categoria que se selecciono,
                        // y que podra ser nulable segun queramos crear una categoria nueva o editarla:
                        type = NavType.StringType
                        nullable = true //Para que pueda ser nulable y asi poder crear una nueva categoria
                        defaultValue = null //Para que tenga un valor por defecto nulo, para poder crear una nueva categoria
                    }
                )
            ) { backStackEntry ->
                val viewModel: SellerCategoryFormViewModel = hiltViewModel(backStackEntry)
//                El backStackEntry es un objeto que representa la entrada en la pila o historial de navegación.
//                El hiltViewModel() es una función que se utiliza para obtener un ViewModel desde Hilt
//                y asociarlo a una entrada en la pila de navegación.

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