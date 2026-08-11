package com.example.multitiendaapp.presentation.registerSeller

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.multitiendaapp.R

//Pantalla de registro de vendedor:
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterSellerScreen(
    onBack: () -> Unit,
    onFinishRegisterSeller: () -> Unit
) {
    //    Scaffold es la estructura base de pantalla: topbar, bootmbar y actionFloatingButton y conrtenido
    Scaffold(
        topBar = {
//            Llamamos a la fun composable TopAppBar() para crear el topbar:
            TopAppBar(
//                Aca def el contenido del topbar:
                title = { Text(text = "Registro de vendedor") },

//              Luego el Icono de navegacion:
//              Icono de flecha ubicado a la izquierda del topbar, para volver a la pantalla anterior:
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onBack()  //Para volver a la pantalla anterior. se define en AppNavHost.kt
                        }
                    ) {
//                        Scope o contenido del boton icono: Sera un icono de flecha para volver:
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Volver"
                        )

                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary, //Color de fondo del topbar
                    titleContentColor = MaterialTheme.colorScheme.onPrimary, //Color del texto del topbar: "Registro de vendedor",
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary //Color del icono de flecha para volver:
                )
            ) //Cierre TopAppBar()

        } //Cierre de la fun composable TopBar()
    ) //Cierre de Scaffold()

    { //Aca definimos el contenido de la pantalla:
        //El Scaffold nos devuelve aca un paddingValues que es un espacio entre el topbar y el contenido
        // y sirve para evitar que el contenido de la pantalla sea tapado por el Scaffold en este caso por el topbar:
            paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) //Para que no se tape el topbar
                .padding(16.dp) //Padding adicional para el contenido de la pantalla:
                .verticalScroll(rememberScrollState()), //Para que el contenido de la pantalla sea scrollable
            horizontalAlignment = Alignment.CenterHorizontally //Para que el contenido de la pantalla sea centrado horizontalmente
        ) {
            Image(
                painterResource(id = R.drawable.ic_seller_register),
                contentDescription = "Registro de vendedor",
                modifier = Modifier.size(60.dp)//Para que la imagen sea de 60dp de alto y ancho
            )

            Spacer(modifier = Modifier.height(20.dp))

//          Creamos campos de texto con bordes o OutlinedTextField():
//           Para ingresar el NOMBRE del vendedor en el campo de texto y que quede registrado.
//           Aca usaremos el ViewModel para guardar el nombre del vendedor:
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text(text = "Nombre") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
//                   Aca va un icono de persona, en la parte izquierda del campo de texto:
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Icono Nombres"
                    )

                },
                singleLine = true, //Para que solo se pueda ingresar una linea de texto y no varias en el campo de texto
            )

            Spacer(modifier = Modifier.height(12.dp))

//           Campo de texto para los Apellidos del vendedor:
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text(text = "Apellidos") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
//                   Aca va un icono de persona, en la parte izquierda del campo de texto:
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Icono Apellidos"
                    )
                },
                singleLine = true, //Para que solo se pueda ingresar una linea de texto y no varias en el campo de texto
            )

            Spacer(modifier = Modifier.height(12.dp)) //Espacio entre los campos de texto

//           Campo de texto para el correo del vendedor:
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text(text = "Correo") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
//                   Aca va un icono de persona, en la parte izquierda del campo de texto
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Icono de Correo"
                    )
                },
//               Ahora definimos el tipo de teclado que se mostrara en el campo de texto
//               para optimizarlo para el ingreso de correos electronicos:
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                singleLine = true, //Para que solo se pueda ingresar una linea de texto y no varias en el campo de texto
            )

            Spacer(modifier = Modifier.height(12.dp)) //Espacio entre los campos de texto

//           Otro campo de texto para la contraseña del vendedor:
            OutlinedTextField(
                value = "",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Contraseña") },
                leadingIcon = {
//                   Aca va un icono de persona, en la parte izquierda del campo de texto
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Icono de Contraseña"
                    )
                },
//               Optimizamos el texto para contraseña:
                visualTransformation = PasswordVisualTransformation(),//Para que no se vea la contraseña
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                singleLine = true, //Para que solo se pueda ingresar una linea de texto y no varias en el campo de texto

            )

            Spacer(modifier = Modifier.height(12.dp)) //Espacio entre los campos de texto

//           Otro campo de texto para ingresar el telefono del vendedor:
            OutlinedTextField(
                value = "",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Telefono") },
                leadingIcon = {
//                   Aca va un icono, en la parte izquierda del campo de texto
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "Icono de Telefono"
                    )
                },
//               Optimizamos el texto para telefonos:
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone
                ),
                singleLine = true, //Para que solo se pueda ingresar una linea de texto

            )

            Spacer(modifier = Modifier.height(30.dp)) //Espacio entre los campos de texto

//           Creamos un boton para registrar datos del vendedor
//           y sobretodo para ir a registrar la tienda del vendedor:
            Button(
                onClick = { onFinishRegisterSeller() },
                modifier = Modifier.fillMaxWidth()
            ) {
//               En el contenido del boton creamos una fila de elementos: un texto y un icono:
                Row {
                    Text(text = "Siguiente")
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Ir a siguiente pantalla"
                    )

                }
            }


        } //Cierre de Column()

    }

}