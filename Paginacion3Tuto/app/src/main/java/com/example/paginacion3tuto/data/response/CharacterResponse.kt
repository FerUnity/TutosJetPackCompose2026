package com.example.paginacion3tuto.data.response

import com.example.paginacion3tuto.presentation.model.CharacterModel
import com.google.gson.annotations.SerializedName

// Aqui van los datos especificos que nos devuelve la API para cada personaje, solo tomaremos algunos de ellos.
// Como esto es lo que nos devuelvwe la API en bruto crearemos despues otro archivo CharacterModel, para trabajar en la app:
/*
*     "id": 21,
      "name": "Aqua Morty",
      "image": "https://rickandmortyapi.com/api/character/avatar/21.jpeg"
      * */
data class CharacterResponse(
    //Usamos el SerializedName para que los nombres sean mas descriptivos y entendibles para nosotros, que los originales de la API:
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("status") val status: String,
    @SerializedName("image") val image: String
) {
    //Aca creamos una fun para mapear los datos obtenidos de la API y guardados arriba en la data class CharacterResponse
// y que nos devuelva un CharacterModel, que es lo que usaremos en la app para mostrar los datos:
    fun toPresentation(): CharacterModel {
        val image = ""
        return CharacterModel(
            id = id,
            name = name,
            isAlive = status == "Alive",
            image = image
        )
    }
}
