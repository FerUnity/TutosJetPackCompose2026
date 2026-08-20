package com.example.multitiendaapp.di

import com.example.multitiendaapp.data.repository.AuthRepoImplem
import com.example.multitiendaapp.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//Este modulo se encargara de proporcionar la implementacion del repositorio de autenticacion con Firebase,
//hilt dara la clase concreta a usar en la capa de autenticacion,
// para quien la necesite en la aplicacion: ViewModel, Activity, Fragment, etc.

@Module
@InstallIn(SingletonComponent::class)
object AuthRepositoryModule {
    @Provides
    @Singleton
    fun provideAuthRepository(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): AuthRepository = AuthRepoImplem(auth, firestore)
}
