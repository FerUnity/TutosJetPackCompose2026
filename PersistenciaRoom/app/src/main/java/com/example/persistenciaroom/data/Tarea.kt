package com.example.persistenciaroom.data

import androidx.room.Entity
import androidx.room.PrimaryKey

//Aca crearemos la Entidad(o Tabla) Tarea y el nombre de la tabla sera tareas:

@Entity(tableName = "tareas") //Nombre de la tabla o entidad sera tareas.
data class Tarea(
//    Aqui van los campos de la tabla:
    @PrimaryKey(autoGenerate = true) //Clave primaria y autoincrementable para que no tengamos que hacerlo manualmente.
    val id: Int = 0, //Clave primaria.
    val titulo: String = "" //Campo titulo.
)
