package com.example.paginacion3tuto.data

import com.example.paginacion3tuto.data.response.ResponseWrapper
import retrofit2.http.GET
import retrofit2.http.Query

//Esta interface la usamos para hacer la llamada a la API de Rick and Morty con Retrofit y obtener los datos de los personajes.
// Entonces debe agregarse el endpoint a la URL de la API y un Repositorio.

//El endpoint es: "https://rickandmortyapi.com/api/character/?page=page"
// lo que cambia es la parte final: Ej ?page=2, que corresponde al numero de pagina 2, que queremos obtener:

interface RickMortyApiService {
    @GET("/api/character/")
    suspend fun getCharacters(@Query("page") page: Int): ResponseWrapper
//Esa Query pondra el int numero de pagina que queremos en la URL, o sea: ?page=page.
//    Y esta fun nos devuelve la informacion de la API en formato data class ResponseWrapper,
//    de la sgye forma: una lista de CharacterResponse y un InfoResponse.

}