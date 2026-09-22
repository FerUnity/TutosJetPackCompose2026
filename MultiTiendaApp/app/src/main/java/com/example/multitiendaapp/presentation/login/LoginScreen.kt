package com.example.multitiendaapp.presentation.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.multitiendaapp.R

//Pantalla de Login o inicio de sesion
@OptIn(ExperimentalMaterial3Api::class)
@Composable //Componenete de interfaz para interactuar con el usuario
//Esta fun tiene un param que se ejecutara péro no devolversa ningun resultado, por eso es -> Unit.
//Lo que hara este param que es una fun Unit esta defuinido en el composable AppNavHost.kt.
//En este caso nos permitira llegar a la pantalla de seleccion de rol al presionar el boton "Registrarme".
fun LoginScreen(
    onGoToSelectRole: () -> Unit,
    viewModel: LoginViewModel,
    onNavigateByRol: (String) -> Unit //Aca pasamos como parametro String, el rol del usuario para navegar a la pantalla correspondiente.
) {
    //    Ahora observamos el estado de la pantalla de Login, este estado proviene del viewmodel
    //    y se llama uiState,
    //    entonces creamos una variable state que almacena el estado de la pantalla de Login:
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    //Para observar el estado de la pantalla de Login solo mientras este en la pantalla.

//    Ademas creanos una var context para identificar la aplicacion frente al sistema operativo,
//    esto para poder usar sus recursos y poder mostrar mensajes al usuario, por ej el Toast:
//    Para eso usamos LocalContext.current::
    val context = LocalContext.current

    //    Ahora creamos una corrutina especial de JetPack Compose llamada LaunchedEffect(),
//    que se encargara de mostrar los efectos colaterales de la pantalla de Login.,
//    sin bloquear el hilo principal de la aplicacion.
//    En este caso tenemos 2 efectos disponibles (ver sealed interface CustomerEffect en RegisterCustomerViewModel.kt):
//    Mostrar un mensaje(ShowMessage) y navegar a otra pantalla(NavigateToHome) :
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is LoginEffect.ShowMessage -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }

                is LoginEffect.NavigateByRole -> {
                    onNavigateByRol(effect.role)
                }
            }
        }
    }

//    Ahora generamos la parte grafica de la pantalla de Login, usando un Scaffold:
//   Scaffold es la estructura base de pantalla: topbar, bootmbar y actionFloatingButton y conrtenido,
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Login de usuario") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary, //Color de fondo del topbar
                    titleContentColor = MaterialTheme.colorScheme.onPrimary, //Color del texto del topbar: "Login",
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary //Color del icono de flecha para volver:
                )
            )

        }

    ) {
//        Aca definimos el contenido de la pantalla:
//    El Scaffold nos devuelve aca un paddingValues que es un espacio entre el topbar y el contenido
        //   y sirve para evitar que el contenido de la pantalla sea tapado por el Scaffold en este caso por el topbar:
            paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) //Para que no se tape el contenido por el topbar
                .padding(24.dp), //Padding adicional para el contenido de la pantalla:
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally //Para que el contenido de la pantalla sea centrado horizontalmente
        ) {
            Image(
                painterResource(id = R.drawable.ic_login),
                contentDescription = "Icono de Login",
                modifier = Modifier.size(100.dp)//Para que la imagen sea de 60dp de alto y ancho
            )

            Spacer(modifier = Modifier.height(20.dp))

//            Ahora los campos de texto para ingresar el correo y la contraseña, para iniciar sesion:
            OutlinedTextField(
                value = state.email,
                onValueChange = { newEmail ->
                    viewModel.onEvent(LoginEvent.OnEmailChange(newEmail))
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Correo electronico") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Icono de Correo"
                    )
                },
//      Ahora definimos el tipo de teclado que se mostrara en el campo de texto
//            para optimizarlo para el ingreso de correos electronicos:
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                singleLine = true, //Para que solo se pueda ingresar una linea de texto y no varias en el campo de texto

            )


            Spacer(modifier = Modifier.height(12.dp)) //Espacio entre los campos de texto

//            Campo de texto para la contraseña:
            OutlinedTextField(
                value = state.password,
                onValueChange = { newPassword ->
                    viewModel.onEvent(LoginEvent.OnPasswordChange(newPassword))
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Contraseña") },
//                Ahora un icono al lado del campo de texto para la contraseña:
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Icono de Contraseña"
                    )
                },
//               Optimizamos el texto para contraseña:
                //Para que no se vea la contraseña:
                visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                singleLine = true, //Para que solo se pueda ingresar una linea de texto y no varias en el campo de texto

            )

            Spacer(modifier = Modifier.height(12.dp)) //Espacio entre los campos de texto

//            Creamos un boton para iniciar sesion, el cual llama a la fun onEvent() del viewmodel
            Button(
                onClick = {
                    viewModel.onEvent(LoginEvent.OnLoginClick)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading //Para que el boton este deshabilitado mientras se inicie sesion o otro proceso de carga.
            )
            {
//                El texto del boton depende del estado de la pantalla de login:
                if (state.isLoading) {
                    Text(text = "Cargando...")
                } else {
                    Text(text = "Iniciar sesion")
                }

            }

            Spacer(modifier = Modifier.height(12.dp)) //Espacio entre los campos de texto

//     En caso que el usuario no tenga cuenta, creamos un TextButton que nos envie a pantalla de registro
//     segun el rol deseado:
            TextButton(
                onClick = {
                    onGoToSelectRole()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "No tienes una cuenta?....Registrarme")
            }
        }
    }
}

