package com.example.multitiendaapp.core.model

//Es la data class que tiene la informacion de una tienda.
// Por ende al llamarse esta data class devuelve un objeto de tipo tienda:
data class Store(
    val id: String = "",
    val sellerId: String = "",
    val name: String = "",
    val description: String = "",
    val category: String = "",
    val imageUrl: String = "",
    val isActived: Boolean = true,
    val createdAt: Long = 0L
)
