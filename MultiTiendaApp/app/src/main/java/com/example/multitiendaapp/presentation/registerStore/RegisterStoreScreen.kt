package com.example.multitiendaapp.presentation.registerStore

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.multitiendaapp.R
import com.example.multitiendaapp.presentation.component.CategoryDropdown

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterStoreScreen() {
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
        //El Scaffold nos devuelve aca un paddingValues que es un espacio entre el topbar y el contenido,
        // y sirve para evitar que el contenido de la pantalla sea tapado por el Scaffold en este caso por el topbar:
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
                value = "",
                onValueChange = {},
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
                value = "",
                onValueChange = {},
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


//            Categorias de la tienda. Llamamos al componente que creamos CategoryDropdown():
//            Menu desplegable con las categorias de la tienda para seleccionar:
            CategoryDropdown(
                selectedCategory = selectedCategory,
                onCategorySelected = { category ->
                    selectedCategory =
                        category //Aca guardamos la categoria seleccionada en la var selectedCategory
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

//            Boton para ir a la pantalla siguiente de registro de productos segun la categoria seleccionada:
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Row {
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