package com.example.paginacion3tuto.data

import androidx.paging.PagingData
import com.example.paginacion3tuto.presentation.model.CharacterModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RMRepository @Inject constructor(private val apiService: RickMortyApiService) {
//    Usamos Flow porque es un flujo de datos que se actualiza siempre y en tiempo real.
    fun getAllCharacters(): Flow<PagingData<CharacterModel>> {
        apiService.getCharacters(2)

    }
}