package com.example.multitiendaapp.di

import com.example.multitiendaapp.data.repository.CategoryRepositoryImpl
import com.example.multitiendaapp.domain.repository.CategoryRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module //Indica que esta clase es un modulo de Dagger Hilt, por ende proveera dependencias.
@InstallIn(SingletonComponent::class) //Indica que las dependencias proporcionadas por este modulo vivran en toda la app(GlobaL),
object CategoryRepositoryModule {
    @Provides //Indica que esta funcion proveera dependencias.
    @Singleton //Indica que solo se creara una instancia de la independencia, se crea una vez y se reutiliza.
    fun provideCategoryRepository(
        firestore: FirebaseFirestore,
    ): CategoryRepository {
        return CategoryRepositoryImpl(firestore)
    }
}
