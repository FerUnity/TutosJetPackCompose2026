package com.example.multitiendaapp.presentation.registerStore

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.multitiendaapp.R
import com.example.multitiendaapp.presentation.component.CategoryDropdown

//Pantalla de registro de la tienda:
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterStoreScreen(
    viewModel: RegisterStoreViewModel,
    onNavigateHome: () -> Unit
) {
//    Ahora observamos el estado de la pantalla de registro de la tienda, este estado proviene del viewmodel
    // y se llama uiState,
    // entonces creamos una variable state que almacena el estado de la pantalla de registro de la tienda:
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    //Para observar el estado de la pantalla de registro de la tienda solo mientras este en la pantalla.

    //    Ademas creanos una var context para identificar la aplicacion frente al sistema operativo,
//    esto para poder usar sus recursos y poder mostrar mensajes al usuario, por ej el Toast:
//    Para eso usamos LocalContext.current::
    val context = LocalContext.current

    //    Ahora creamos una corrutina especial de JetPack Compose llamada LaunchedEffect(),
//    que se encargara de mostrar los efectos colaterales de la pantalla de registro del cliente.,
//    sin bloquear el hilo principal de la aplicacion.
//    En este caso tenemos 2 efectos disponibles desde el viewmodel (ver sealed interface CustomerEffect en RegisterCustomerViewModel.kt):
//    Mostrar un mensaje(ShowMessage) y navegar a otra pantalla(NavigateToSellerHome) :
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
//            Aca evaluamos que efecto llego desde el viewmodel:(son 2 efectos disponibles)
            when (effect) {
                is StoreEffect.ShowMessage -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                } //Esto es para mostrar un mensaje como toast en la pantalla de registro de la tienda pantalla que provienen del viewmodel
                is StoreEffect.NavigateToSellerHome -> {
                    onNavigateHome()
                }
            }
        }
    }

//    Creamos una var que almacena la categoria de tienda seleccionada por el vendedor.
    //    Esa seleccion va a ser recordada entre recomposiciones gracias a remember:
    var selectedCategory by remember { mutableStateOf("") }

    //    Scaffold es la estructura base de pantalla: topbar, bootmbar y actionFloatingButton y conrtenido
    Scaffold(
        topBar = {
//            Llamamos a la fun composable TopAppBar() para crear el topbar:
            TopAppBar(
//                Aca def el contenido del topbar:
                title = { Text(text = "Registro de tienda") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary, //Color de fondo del topbar
                    titleContentColor = MaterialTheme.colorScheme.onPrimary, //Color del texto del topbar: "Registro de tienda",
                )
            )


        }
    )
    {
        //Aca definimos el contenido de la pantalla:
        //El Scaffold nos devuelve aca un paddingValues que es un espacio entre el topbar, bootmbar, etc. y el contenido,
        // y sirve para evitar que el contenido de la pantalla sea tapado por elementos del Scaffold en este caso por el topbar:
            paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally //Para centrar horizontalmente el contenido de la Column
        ) {
            Image(
                painterResource(id = R.drawable.ic_store_register),
                contentDescription = "Registro de tienda",
                modifier = Modifier.size(60.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

//          Campos de texto:
//          Nombre de la tienda:
            OutlinedTextField(
                value = state.storeName, //Aca guardamos el valor del campo de texto por defecto, del nombre de la tienda
                onValueChange = { newStoreName ->
                    viewModel.onEvent(StoreEvent.OnStoreNameChange(newStoreName))
                    //Esto es para que cuando el usuario ingrese un nombre en el campo de texto,
                    // el viewmodel lo guarde en el estado de la pantalla de registro de la tienda y lo muestre en la pantalla.
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Nombre de la tienda") },
                leadingIcon = {
                    Image(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Icono de Nombre de la tienda"
                    )

                },
                singleLine = true, //Para que solo se pueda ingresar una linea de texto y no varias en el campo de texto
            )

            Spacer(modifier = Modifier.height(12.dp))


//          Descripcion de la tienda:
            OutlinedTextField(
                value = state.storeDescription, //Aca guardamos el valor del campo de texto por defecto, de la descripcion de la tienda
                onValueChange = { newStoreDescription ->
                    viewModel.onEvent(StoreEvent.OnStoreDescriptionChange(newStoreDescription))
                    //Esto es para que cuando el usuario ingrese una descripcion en el campo de texto,
                    // el viewmodel lo guarde en el estado de la pantalla de registro de la tienda y lo muestre en la pantalla.
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Descripcion de la tienda") },
                leadingIcon = {
                    Image(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Icono de descripcion de la tienda"
                    )

                },
                minLines = 3, //Para que el campo de texto tenga 3 lineas de texto como minimo y no solo una.
                maxLines = 4, //Para que el campo de texto tenga 4 lineas de texto como maximo.
            )

            Spacer(modifier = Modifier.height(12.dp))


//            Categorias de la tienda:
//            Llamamos al componente que creamos CategoryDropdown():
//            Aca aparece un Menu desplegable con las categorias de la tienda
//            donde debems seleccionar la categoria de la tienda:
            CategoryDropdown(
                selectedCategory = selectedCategory,
                onCategorySelected = { category ->
                    selectedCategory =
                        category //Aca guardamos la categoria seleccionada en la var selectedCategory

                    //Aca llamamos al evento OnCategoryChange() del viewmodel para que guarde la categoria seleccionada
                    // que quedo almacenada en la var selectedCategory:
                    viewModel.onEvent(StoreEvent.OnCategoryChange(category))
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

//            Boton para ir a la pantalla siguiente de registro de la tienda,
//           correspondiente a la pantalla de registro de productos segun la categoria seleccionada:
            Button(
                onClick = {
                    viewModel.onEvent(StoreEvent.OnNextClick)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Row {
                    // En el contenido del boton creamos una fila de elementos: un texto y un icono:
                    Text(text = "Siguiente")
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Ir a siguiente pantalla"
                    )
                }
            }


        }
    }

}