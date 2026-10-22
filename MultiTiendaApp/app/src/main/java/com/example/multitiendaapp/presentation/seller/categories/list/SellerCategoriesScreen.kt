package com.example.multitiendaapp.presentation.seller.categories.list

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.multitiendaapp.presentation.seller.categories.form.SellerCategoryItem

//Esta vista muestra el listado de las categorias de las tiendas registradas,
// Esta vista hay que registrarla en el contenedor de SellerRootScreen()
@Composable
fun SellerCategoriesScreen(
    viewModel: SellerCategoriesListViewModel,
    //aca pasamos el viewModel, SellerCategoriesListViewModel, para la logica de esta vista.
    onGoToForm: () -> Unit //Solo se ejecuta la funcion que se define en SellerRootScreen(), no devuelve nada
) {
    //    Creamos un estado para escuchar u observar el estado del viewModel de esta pantalla de lista de categorias,
// esto es para que cuando haya un cambio en esta vista, se recomponga de inmediato:
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    //    Ademas un contexto para identificar esta app frente al OS Android,
//    y asi mostrar mensajes Toast, navegar a otras pantallas, etc:
    val context = LocalContext.current

    //    Creamos un Launcheffect para escuchar los efectos que vienen del viewModel, como
//    ShowMessage y NavigateBack, para mostrar mensajes Toast y navegar a otras pantallas, etc:
    LaunchedEffect(key1 = Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                //Aca se procesa el efecto ShowMessage, y se define que se mostraran como mensajes Toast:
                is SellerCategoriesListEffect.ShowMessage -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


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
        /*  topBar = {
              Text(text = "Categorias")
          },
          bottomBar = {
              Text(text = "Categorias")
          },*/
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
                    contentDescription = "Crear nueva categoria"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(12.dp)
                .fillMaxSize()
        ) {
//            Creamos una condicion para saber si se estan cargando los datos desde Firebase:
            if (state.listLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                    //Aca se muestra un CircularProgressIndicator() mientras se cargan los datos desde Firebase
                }
            } else {
//                Verificamos si la lista de categorias esta vacia en Firestore.
                //    Es obvio que si la primera vez que se abre la app no hay categorias:
            }
            if (state.categories.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay categorias registradas aun",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }
//            Si ya existen datos en Firestore los mostramos con una LazyColumn:
            else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                    //Para dar espacio vertical entre los elementos hijos de la columna
                ) {
//                    Iterms itera la lista categories y obtiene una categoria en cada iteracion,
                    // finalmente las muestra todas en la pantalla:
                    items(state.categories) { category ->
//                        Las categorias se mostraran segun el componente SellerCategoryItem() que creamos,
                        //para que se muestren las categorias en una Card:
                        SellerCategoryItem(category = category)
                    }
                }
            }

        }
    }
}