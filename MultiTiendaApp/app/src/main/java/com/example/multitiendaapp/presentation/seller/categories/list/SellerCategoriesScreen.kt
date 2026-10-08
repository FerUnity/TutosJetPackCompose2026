package com.example.multitiendaapp.presentation.seller.categories.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

//Esta vista hay que registrarla en contenedor de SellerRootScreen()
@Composable
fun SellerCategoriesScreen(
    onGoToForm: () -> Unit //Solo se ejecuta, no devuelve nada
){
    //    Usaremos un Box para visyalizar algo:
  /*  Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    )
    {
        Text(
            text = "Categories",
            style = MaterialTheme.typography.headlineLarge
        )
    }*/

//    Creamos un Scaffold que es un contenedor visual de la pantalla,
//    donde usaremos un FloatingActionButton para agregar categorias:
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
//         El boton flotante lo usaremos para navegar desde la pantalla lista de categorias a la de agregar categoria
            // o sea desde SellerCategoriesScreen() a SellerCategoriesFormScreen()
                //Esta declarado asi: navController.navigate(AppRoute.SellerCategoryForm.route), en SellerRootScreen(),
            //pero usamos el callBack onGoToForm() que definimos en SellerCategoriesScreen():
                onGoToForm()
            }
//                , containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Crear nueva categoria")
            }
        }
    ){
        paddingValues ->


    }

}