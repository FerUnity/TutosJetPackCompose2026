package com.example.multitiendaapp.presentation.registerCustomer

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.multitiendaapp.R


//Pantalla de registro del cliente
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterCustomerScreen(
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Registro de cliente") },
                navigationIcon = {
                    IconButton(
                        onClick = { onBack() } //Para volver a la pantalla anterior. se define en AppNavHost.kt
                    )
                    {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary, //Color de fondo del topbar
                    titleContentColor = MaterialTheme.colorScheme.onPrimary, //Color del texto del topbar: "Registro de cliente",
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary //Color del icono de flecha para volver:
                )
            )
        }
    )
    {
        //Aca definimos el contenido de la pantalla:
        //El Scaffold nos devuelve aca un paddingValues que es un espacio entre el topbar y el contenido
        // y sirve para evitar que el contenido de la pantalla sea tapado por el Scaffold en este caso por el topbar:
            paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painterResource(id = R.drawable.ic_customer_register),
                contentDescription = "Registro de cliente",
                modifier = Modifier.size(60.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

//            Campo de texto para nombres del cliente:
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text(text = "Nombres") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Icono Nombres"
                    )
                },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

//            Campo de texto para apellidos del cliente:
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text(text = "Apellidos") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Icono Apellidos"
                    )
                },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

//            Campo de texto para el correo del cliente:
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text(text = "Correo electronico") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Icono de Correo"
                    )
                },
//                Aca optimizamos el teclado para el correo electronico:
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))


//            Campo de texto para la contraseña del cliente:
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text(text = "Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Icono de Contraseña"
                    )
                },
//                Luego ocultamos la contraseña:
                visualTransformation = PasswordVisualTransformation(),
//                Aca optimizamos el teclado para la contraseña:
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                singleLine = true //Para que solo se pueda ingresar una linea de texto y no varias en el campo de texto
            )

            Spacer(modifier = Modifier.height(12.dp))


//            Campo de texto para confirmar la contraseña del cliente:
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text(text = "Confirmar contraseña") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Icono de Confirmar Contraseña"
                    )
                },
//                Luego ocultamos la contraseña:
                visualTransformation = PasswordVisualTransformation(),
//                Aca optimizamos el teclado para la contraseña:
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                singleLine = true //Para que solo se pueda ingresar una linea de texto y no varias en el campo de texto
            )

            Spacer(modifier = Modifier.height(30.dp))

//            Finalmente creamos un boton para registrar datos del cliente:
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Row{
                    Text(text = "Registrar")
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Finalizar registro del cliente"
                    )
                }
            }



        } //Cierre de Column()
    }
}




