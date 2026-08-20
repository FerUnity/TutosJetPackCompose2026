package com.example.multitiendaapp.di

import com.example.multitiendaapp.data.repository.StoreRepoImplem
import com.example.multitiendaapp.domain.repository.StoreRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module //Aca se indica que es un modulo de hilt que sabe como crear una dependencia de StoreRepository.
@InstallIn(SingletonComponent::class)
object StoreRepositoryModule {
    @Provides //que dabe brindar dependecia de StoreRepository.
    @Singleton //Aca se indica que hilt creara solo una instancia de StoreRepository.
//    Luego creamos una fun que devuelve una instancia de StoreRepository.
    fun provideStoreRepository(
        firestore: FirebaseFirestore
    ): StoreRepository = StoreRepoImplem(firestore)
}
