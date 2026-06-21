package com.example.proyectocasaspildoras.data

import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import com.example.proyectocasaspildoras.R

//data class casa (clase de datos) que es donde se establecen los datos de las casas.
data class Casa(
    var id: Int,
    val nombre: String,
    val imagenId: Int?,
    val descripcion: String,
//    parametro para agrergar la ubicacion de la imagen de las nuevas casas:
    val imagenUri: Uri?//Ojo las que estan en la carpeta drawable tienen que tener un valor null
)

//Creamos un object RepositorioCasa que contiene una lista con los tipos de casas que seran 3.
//Los object son objetos de tipo singleton,
// lo que significa que solo se crea una instancia de la clase y se puede acceder a ella desde cualquier parte del codigo.
// Cada elemento casa de la lista, tendra un id, un nombre, una imagen y una descripcion.
// Tal como se muestra en el data class Casa.
// Es por eso que en cada elemento usamos el constructor del data class Casa asi:
// Casa(1, "Casa 1", R.drawable.casa1, "Descripción de la casa 1")

//Ahora como se nos ocurrio crear un formulario para agregar casas,
// debemos cambiar el tipo de lista listOf(que es inmutable) a una mutableStateListOf (que es mutable).
object RepositorioCasa {
    val listaCasas = mutableStateListOf(
        Casa(1, "Casa Mediterranea", R.drawable.casa1, "Casa lumonosa junto al mar", null),
        Casa(2, "Casa Rustica", R.drawable.casa2, "Ambiente calido en la montaña", null),
        Casa(3, "Casa Moderna", R.drawable.casa3, "Diseño milimalista y elegante", null)
    )

    //    Aqui creamos una fun para obtener una casa por su id. Por tanto devuelve un elem de clase Casa.
    //    it es el elemento que se espera encontrar dentro de la lista de casas.:
    fun obtenerCasaPorId(id: Int): Casa? = listaCasas.find { it.id == id }
// find devuelve el primer elemento que cumpla con la condicion.
// El "it" es el elemento actual de tipo Casa, que se esta iterando.

//    Creamos una fun para agregar una casa a la lista de casas:
//    (y debe ser de clase Casa, o sea debe tener un id, un nombre, una imagen y una descripcion):
    fun agregarCasa(casa: Casa) {
        listaCasas.add(casa)
    }



   /*Es igual a esta funcion:
    fun obtenerCasaPorId(id: Int): Casa? {
        for (casa in listaCasas) {
            if (casa.id == id) {
                return casa
            }
        }
        return null
    }*/
}
