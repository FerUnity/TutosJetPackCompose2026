package com.example.proyectocasaspildoras.data

import com.example.proyectocasaspildoras.R

//data class casa es donde se establecen los datos de las casas.
data class Casa(
    var id: Int,
    val nombre: String,
    val imagenId: Int,
    val descripcion: String
)

//Creamos un object RepositorioCasa que contiene una lista con los tipos de casas que seran 3.
//Los object son objetos de tipo singleton,
// lo que significa que solo se crea una instancia de la clase y se puede acceder a ella desde cualquier parte del codigo.
// Cada elemento casa de la lista, tendra un id, un nombre, una imagen y una descripcion.
// Tal como se muestra en el data class Casa.
// Es por eso que en cada elemento usamos el constructor del data class Casa asi:
// Casa(1, "Casa 1", R.drawable.casa1, "Descripción de la casa 1")
object RepositorioCasa {
    val listaCasas = listOf(
        Casa(1, "Casa Mediterranea", R.drawable.casa1, "Casa lumonosa junto al mar"),
        Casa(2, "Casa Rustica", R.drawable.casa2, "Ambiente calido en la montaña"),
        Casa(3, "Casa Moderna", R.drawable.casa3, "Diseño milimalista y elegante")
    )

    //    Aqui creamos una fun para obtener una casa por su id. Por tanto devuelve un elem de clase Casa:
    fun obtenerCasaPorId(id: Int): Casa? = listaCasas.find { it.id == id }
// find devuelve el primer elemento que cumpla con la condicion.
// El "it" es el elemento actual de tipo Casa, que se esta iterando.


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
