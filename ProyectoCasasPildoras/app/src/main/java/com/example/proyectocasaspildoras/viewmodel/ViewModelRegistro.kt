package com.example.proyectocasaspildoras.viewmodel

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ViewModelRegistro: ViewModel() {
//    En el ViewModel no usamos remember porque ya lo tenemos en el composable:
    var nombre by mutableStateOf("")
        private set

    var descripcion by mutableStateOf("")
        private set

//    Para imagen de la casa:
    /*var imagenId by mutableStateOf(R.drawable.casa1)*/
    var imagenUri by  mutableStateOf<Uri?>(null)


    //  Funciones:
    fun onNombreChange(value: String) {
        nombre = value //En el composable se usa onValueChange = {nombre = it}
    }

    fun onDescripcionChange(value: String) {
        descripcion = value //En el composable se usa onValueChange = {descripcion = it}
    }
}