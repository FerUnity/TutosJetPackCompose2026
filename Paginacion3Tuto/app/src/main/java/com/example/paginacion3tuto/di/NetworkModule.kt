package com.example.paginacion3tuto.di

import com.example.paginacion3tuto.data.RickMortyApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//Este objeto lo creamos para poder hacer la  inyectaccion de dependencias de la API en el repositorio.

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "https://rickandmortyapi.com/api/"
// Para poder hacer la inyeccion de dependencias de la API,
// debemos proveerenos de 3 cosas: el OkHttpClient, el Retrofit y el ApiService, asi:
    @Provides
    fun provideApiService(retrofit: Retrofit): RickMortyApiService =
        retrofit.create(RickMortyApiService::class.java)

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()) //para convertir la respuesta JSON en data class Kotlin
            .build()

    @Provides
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .build()
}