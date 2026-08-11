package com.example.multitiendaapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

//Esta es la clase inicial de toda la App, que se ejecutara al inicio de la app,
// y donde decimos que usaremos Hilt para inyectar dependencias.
// USamos @HiltAndroidApp para generar el contenedor de dependencias Hilt en la clase de la aplicación,
// prepara la inyeccion de dependencias.
@HiltAndroidApp
class App: Application() //Application es la clase base de la app. Representa el pto de inicio de toda la app.