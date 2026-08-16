package com.example.multitiendaapp.domain.repository

import com.example.multitiendaapp.core.model.Store

//En una interfaz solo se definen lo que hara la app por ejemplo: login, register, logout, etc,
// pero no los pasos logicos de como se hara.
// por eso se dice que la interfaz es un contrato que se debe cumplir.
interface StoreRepository {
//    Creamos una fun para crear una tienda:
//    Creamos una fun para crear o registrar una tienda:
    suspend fun createStore(
        store: Store
) : Result<Unit> //Aca se define que la funcion createStore() que devuelve si fue exitosa o no.
// No queremos que nos devuelva la tienda. Solo queremos saber si su creacion(registro) fue exitoso o no.
// Por eso se usa un objeto Unit (Result<Unit>), y no Store (Result<Store>).

//    Creamos otra fun para encontrar una tienda especifica segun el id del vendedor, podria ser nulo:
    suspend fun getStoreBySellerId(sellerId: String) : Result<Store?>

//    fun para actualizar datos de una tienda existente. Hay quenponer un parametro de tipo Store:
    suspend fun updateStore(store: Store) : Result<Unit> // Solo queremos saber si la actualizacion fue exitosa o no.
}