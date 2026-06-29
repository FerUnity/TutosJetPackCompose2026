package com.example.persistenciaroom.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase //Define la estructura de la base de datos, por ende la clase TareaDatabase hereda de RoomDatabase:

//Aqui creamos la Base de Datos y ademas que las tablas o entidades que contendra seran las definidas en la clase Tarea.
// Sobre esta BBDD de clase TareaDatabase se realizaran las operaciones CRUD definidas en la clase TareaDao.
// Debemos indicar que entidades contiene la base de datos y su version.
// En este caso solo contiene una entidad que sera la clase Tarea y su version es 1:

@Database(entities = [Tarea::class], version = 1)
abstract class TareaDatabase : RoomDatabase() {
    //    Creamos una fun para obtener el DAO. Esto para poder acceder a los metodos definidos en la clase TareaDao.
    //    O sea estamos diciendo que TareaDatabase tiene el Dao TareaDao:
    //    Por ende TareaDatabase tiene acceso a los metodos definidos en la clase TareaDao.
    //    La fun debe ser abstracta y tener el mismo nombre que la clase Dao,
    //    es abstracta porque no tiene implementacion y solo se define.
    //    Y por ende la clase tambien debe ser abstracta, porque tiene una fun abstracta.
    abstract fun tareaDao(): TareaDao

    companion object {
    //    Usamos Volatile para que la instancia se actualice correctamente en todos los hilos en tiempo de ejecucion.
    @Volatile
    private var INSTANCIA: TareaDatabase? = null

    //    Ahora creamos la BBDD fisicamente, usando Room.databaseBuilder y el contexto para inidicar donde se va a guardar la BBDD.
    //    Para eso creamos la fun getDatabase que recibe un contexto y devuelve un objeto de la clase TareaDatabase, o sea la BBDD requerida:
    fun getDatabase(context: Context): TareaDatabase {
        return INSTANCIA ?: synchronized(this) {
            //Para que solo una instancia de la BBDD sea creada a la vez,
            // evitar que se abran multiples instancias de la BBDD, usamos el synchronized.

//   Ahora creamos la BBDD fisicamente, usando Room.databaseBuilder y el contexto para inidicar donde se va a guardar la BBDD:
            Room.databaseBuilder(
                context.applicationContext,
                TareaDatabase::class.java, //usamos .java porque el databaseBuilder requiere una clase java.
                "tarea_database" //nombre de la BBDD.
            ).build().also {
    //     Finalmente asignamos la instancia creada a la variable INSTANCIA para que sea accesible desde otras clases:
                INSTANCIA = it
            }

            /*INSTANCIA = instancia*/
        }
    }
    }

}

