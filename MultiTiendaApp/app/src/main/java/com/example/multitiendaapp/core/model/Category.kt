package com.example.multitiendaapp.core.model

//Este modelo rep cada categoria dentro de una tienda, porque una tienda tiene varias categorias.
// se almacenara id, nombre y tienda a la que pertenece esa categoria.

data class Category(
    val id: String = "", //id unico de la categoria, este lo genera firebase no el usuario.
    val name: String = "", //nombre de la categoria
    val storeId: String = "" //id de la tienda a la que pertenece la categoria
)