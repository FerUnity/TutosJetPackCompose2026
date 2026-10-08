package com.example.multitiendaapp.presentation.seller.root

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.multitiendaapp.navigation.AppRoute
import com.example.multitiendaapp.presentation.component.SellerBottomBar
import com.example.multitiendaapp.presentation.seller.categories.form.SellerCategoryFormScreen
import com.example.multitiendaapp.presentation.seller.categories.list.SellerCategoriesScreen
import com.example.multitiendaapp.presentation.seller.home.SellerHomeScreen

//Este composable se invoca cuando el usuario registrado es un vendedor.
// Este composable es el contenedor de la pantalla de inicio del cliente
// y lo que hace es crear un navController local para navegar entre pantallas,
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
            composable(AppRoute.SellerCategoryForm.route) {
                SellerCategoryFormScreen()
            }
        }
    }

}