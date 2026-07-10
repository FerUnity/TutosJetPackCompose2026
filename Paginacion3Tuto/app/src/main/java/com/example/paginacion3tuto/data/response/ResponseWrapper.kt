package com.example.paginacion3tuto.data.response

import com.google.gson.annotations.SerializedName

// En la carpeta response, almacenaremos las clases que nos devuelven la API. O sea sus respuestas.

//En esta data class creamos las clases que nos devuelven la API. O sea sus respuestas.
// Si miramos el endpoint de la API, por ej: https://rickandmortyapi.com/api/character/?page=2,
// obserevarmos que existen 2 clases en el json: "info" y "results", por ende los invocamos en este data class:

data class ResponseWrapper(
//    Creamos los 2 clases de la API en 2 archivos propios y los importamos aca,
//    pero usamos el SerializedName para que los nombres sean mas descriptivos y entendibles para nosotros,que los originales de la API:
    @SerializedName("info") val information: InfoResponse,
    @SerializedName("results") val resultados: List<CharacterResponse>
)
