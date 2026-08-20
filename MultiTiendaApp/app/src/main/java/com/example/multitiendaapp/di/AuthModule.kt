package com.example.multitiendaapp.di

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//Este modulo se encargara de proporcionar las dependencias de la capa de autenticacion con Firebase,
// para quien la necesite en la aplicacion.
//Para eso usamos hilt

@Module //Aca se indica que es un modulo de hilt.
@InstallIn(SingletonComponent::class) //Aca se indica que el modulo es un singleton,
// o sea que las dependencias viviran solo durante la ejecucion de la aplicacion.
object AuthModule {
    @Provides //Aca se indica a hilt que la funcion devuelve una dependencia lista para ser inyectada en otras clases.
    @Singleton //Aca se indica que hilt creara solo una instancia de FireBase auth

    fun provideFirebaseAuth(): FirebaseAuth = Firebase.auth
}