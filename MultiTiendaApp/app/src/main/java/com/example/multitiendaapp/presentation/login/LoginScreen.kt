package com.example.multitiendaapp.presentation.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

//Pantalla de Login o inicio de sesion
@Composable //Componenete de interfaz para interactuar con el usuario
//Esta fun tiene un param que se ejecutara péro no devolversa ningun resultado, por eso es -> Unit.
//Lo que hara este param que es una fun Unit esta defuinido en el composable AppNavHost.kt.
//En este caso nos permitira llegar a la pantalla de seleccion de rol al presionar el boton "Registrarme".
fun LoginScreen(onGoToSelectRole: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = onGoToSelectRole
        ) {
            Text(text = "Registrarme")
        }
    }

}