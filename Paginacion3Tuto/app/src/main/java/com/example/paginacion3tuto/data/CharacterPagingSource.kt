package com.example.paginacion3tuto.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.paginacion3tuto.presentation.model.CharacterModel
import javax.inject.Inject

class CharacterPagingSource @Inject constructor(private val apiService: RickMortyApiService): PagingSource<Int, CharacterModel>() {
    //    Recordar que el CharacterModel es lo que se devuelve a la capa de ui o presentacion.
//    Esta fun carga el personaje en la pagina page que se obtiene de la API.
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterModel> {
        return try {
            val page = params.key ?: 1 //La ultima pagina o guardada sino la 1
            val response = apiService.getCharacters(page) //Obtenemos la respuesta de la API con la pagina page
            val characters = response.resultados
            val prevKey = if (page > 0) page -1 else null //El valor de la pagina previa
            val nextKey = if (response.information.next != null) page + 1 else null //El valor de la pagina siguiente

//            Aqui retornamos la carga del personaje de la pagina page, con el valor de la pagina previa y la siguiente,
//            pero para hacerlo correcto debemos mapear los datos obtenidos de la API y guardados en la data class CharacterResponse,
//            pero como necesitamos un CharacterModel,
//            llamamos a la fun toPresentation() de la data class CharacterResponse
//            y que recibe un CharacterResponse, pero devuelve un CharacterModel:
            LoadResult.Page(data = characters.map {it.toPresentation() }, prevKey = prevKey, nextKey = nextKey)

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, CharacterModel>): Int? {
        return state.anchorPosition
    }
}