package com.example.persistenciaroom.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.persistenciaroom.data.Tarea
import com.example.persistenciaroom.data.TareaDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

//Este ViewModel sera el intermediario entre la vista y la base de datos.
// Porque sera el viewmodel el que accede a los datos de la base de datos, usando el DAO y no el usuario de la vista:

class TareaViewModel(application: Application) : AndroidViewModel(application) {
//    Creamos una instancia de la base de datos y el DAO asociado para trabajar con las operaciones def en el Dao:
    private val tareaDao = TareaDatabase.getDatabase(application).tareaDao()

    // Creamos un var de tipo Flow que contiene una lista de tareas almacenadas en la base de datos.
//    Para eso usamos la función obtenerTareas() del DAO, var tareaDao:
    val tareas: Flow<List<Tarea>> = tareaDao.obtenerTareas()

//    Luego creamos una función para insertar una tarea en la base de datos:
//    Debemos usar una corrutina(viewModelScope.launch{}) para realizar la operación de inserción en la base de datos en segundo plano:
        fun agregarTarea(texto: String) {
        viewModelScope.launch {
            tareaDao.insertarTarea(Tarea(titulo = texto))
        }

    }

//    Luego creamos una función para borrar una tarea en la base de datos:
//    Debemos usar una corrutina(viewModelScope.launch{}) para realizar la operación de borrado en la base de datos en segundo plano:
    fun borrarTarea(tarea: Tarea) {
       viewModelScope.launch {
           tareaDao.eliminarTarea(tarea)
       }
    }
}


