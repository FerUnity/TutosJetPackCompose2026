package com.example.paginacion3tuto.data.response

import com.google.gson.annotations.SerializedName


//Aqui van los datos generales de la info que nos devuelve la API para cada personaje.
//Como esto es lo que nos devuelvwe la API en bruto crearemos despues otro archivo para trabajar en la app:
/* "info": {
    "count": 826,
    "pages": 42,
    "next": "https://rickandmortyapi.com/api/character/?page=3",
    "prev": "https://rickandmortyapi.com/api/character/?page=1"
  }
  */
data class InfoResponse(
//Usamos el SerializedName para que los nombres sean mas descriptivos y entendibles para nosotros, que los originales de la API:
    @SerializedName("count") val count: Int,
    @SerializedName("pages") val pages: Int,
    @SerializedName("next") val next: String?, //Este valor puede ser nulo, por eso el signo de interrogacion
    @SerializedName("prev") val prev: String? //Este valor puede ser nulo, por eso el signo de interrogacion
)

