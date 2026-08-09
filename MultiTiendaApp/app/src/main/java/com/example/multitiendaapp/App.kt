package com.example.multitiendaapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

//Esta es la clase inicial de toda la App, que se ejecutara al inicio de todo.
// USamos @HiltAndroidApp para generrar el contenedor de dependencias Hilt en la clase de la aplicación,
// prepara la inyeccion de dependencias.
@HiltAndroidApp
class App: Application() //Application es la clase base de la app. Rep el pto de inicio de toda la app.