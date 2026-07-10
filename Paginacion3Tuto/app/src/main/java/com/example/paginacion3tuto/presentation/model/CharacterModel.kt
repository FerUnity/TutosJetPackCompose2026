package com.example.paginacion3tuto.presentation.model

data class CharacterModel(
//    Usamos los mismos datos de CharacterResponse, pero en este data class CharacterModel donde podemos modificarlo,
    val id: Int,
    val name: String,
//    val status: String, vamos a usar otro tipo de dato bool, para mostrar el status del personale: isAlive:
    val isAlive: Boolean,
    val image: String
)
