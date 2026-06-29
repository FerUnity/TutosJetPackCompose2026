package com.example.persistenciaroom.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

//Aca creamos el CRUD, es decir, las operaciones a realizar con nuestra tabla "tareas". Estas operaciones son las siguientes:
// 1. Crear tarea, 2. Obtener tareas, 3. Actualizar tarea, 4. Eliminar tarea. Y las implementa Room:

@Dao
interface TareaDao {
//    Crud:
// Consulta para obtener todas las tareas en orden descendente, es decir, de la mas reciente sobre la mas antigua.
    @Query("SELECT * FROM tareas ORDER BY id DESC")
    fun obtenerTareas(): Flow<List<Tarea>> //No necesita ser suspend porque Flow es una corrutina que se ejecuta en segundo plano.

//    Insertar tarea en la tabla:
    @Insert(onConflict = OnConflictStrategy.REPLACE) //Si se intenta insertar una tarea que ya existe por tener el mismo id, esta se reemplaza.
    suspend fun insertarTarea(tarea: Tarea) //La fun es suspend porque puede ser asincrona o en segundo plano, o sea que puede tardar un tiempo.

//  Borrar tarea de la tabla:
    @Delete
    suspend fun eliminarTarea(tarea: Tarea)



}