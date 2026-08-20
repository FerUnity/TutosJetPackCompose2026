package com.example.multitiendaapp.data.repository

import com.example.multitiendaapp.core.model.Store
import com.example.multitiendaapp.domain.repository.StoreRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

//En esta clase es donde se implementara la logica real de las fun declaradas en la interfaz StoreRepository.kt
// respecto a la autenticacion del usuario con FireBase y Firestore.
//Entonces aca se implementan las fun declaradas en la interfaz StoreRepository.kt
class StoreRepoImplem(
    //    Ahora hacemos la injeccion de dependencias con FirebaseFirestore(de StoreRepositoryModule), en el constructor de la clase StoreRepoImplem:
    private val firestore: FirebaseFirestore // Para acceder a la base de datos de FireStore
) : StoreRepository {
    private val collectionStores: String =
        "stores" //Aca definimos la coleccion donde se gaurdaran las tiendas en la BBDD de FireStore

    //    Creamos una fun que haga referencia a la coleccion de tiendas almacenadas en FireStore,
//    para no tener que escribir firestore.collection() cada vez que se quiera acceder a la coleccion de tiendas:
    private fun storeRef() = firestore.collection(collectionStores)

    //    Cumplimos con el contrato de la interfaz StoreRepository.kt, implementando las fun de la interfaz.
//    Sobrescribimos todas las fun de la interfaz StoreRepository.kt:
    override suspend fun createStore(store: Store): Result<Unit> =
//        Es Unit porque solo queremos saber si la creacion de la tienda fue exitosa o no.
        //        Ademas recibe como parametro una tienda con sus datos:
        runCatching {
            val sellerIdTrimmed =
                store.sellerId.trim()// sellerId es el id del vendedor que crea la tienda, esta en el data class.
//            Y validamos que el id del vendedor no este vacio:
            if (sellerIdTrimmed.isBlank()) {
                throw IllegalArgumentException("El id del vendedor no puede estar vacio")
            }
            val nameTrimmed = store.name.trim()// name es el nombre de la tienda.
//            Y validamos que el nombre de la tienda no este vacio:
            if (nameTrimmed.isBlank()) {
                throw IllegalArgumentException("El nombre de la tienda no puede estar vacio")
            }
            val descriptionTrimmed =
                store.description.trim()// description es la descripcion de la tienda.
//            Y validamos que la descripcion de la tienda no este vacia:
            if (descriptionTrimmed.isBlank()) {
                throw IllegalArgumentException("La descripcion de la tienda no puede estar vacia")
            }
//            Ahora limpiamos la categoria de la tienda con trim para eliminar espacios en blanco al inicio y al final:
            val categoryTrimmed = store.category.trim()
//            Y validamos que la categoria de la tienda no este vacia:
            if (categoryTrimmed.isBlank()) {
                throw IllegalArgumentException("Se debe seleccionar una categoria para la tienda")
            }

//            Creamos un id final que tendra la tienda creada en FireStore, quien creara el id:
            val finalStoreId = storeRef().document().id

//            Ahora debemos obtener la fecha de creacion de la tienda en milisegundos:
            val createdAt = System.currentTimeMillis()

//            Creramos una copia del objeto store o tienda con el id final y la fecha de creacion.
//            copy nos permite copiar el objeto store y modificar solo algunos campos: el id, sellerId, name y fecha:
            val finalStore = store.copy(
                id = finalStoreId,// id es el id final que se creo en FireStore.
                sellerId = sellerIdTrimmed, // sellerId es el id del vendedor que crea la tienda, esta en el data class.
                name = nameTrimmed, // name es el nombre de la tienda.
                description = descriptionTrimmed, // description es la descripcion de la tienda.
                category = categoryTrimmed, // category es la categoria de la tienda.
                createdAt = createdAt // createdAt es la fecha de creacion de la tienda.
            )
//            Creamos el mapa con los datos que enviaremos a la BBDD de FireStore:
            val data =
                mapOf( //mapOf nos permite crear un mapa con los datos que enviaremos a la BBDD de FireStore:
                    "id" to finalStore.id,
                    "sellerId" to finalStore.sellerId,
                    "name" to finalStore.name,
                    "description" to finalStore.description,
                    "category" to finalStore.category,
                    "imageUrl" to finalStore.imageUrl,
                    "isActived" to finalStore.isActived,
                    "createdAt" to finalStore.createdAt
                )
//            Ahora enviamos los datos a la BBDD de FireStore, para eso usamos el metodo set() del objeto storeRef() que creamos antes
//            porque es el que nos permite enviar los datos a la BBDD de FireStore:
            storeRef().document(finalStoreId)
                .set(data)
                .await()

            Unit


        }

    // Sobreescribimos la fun getStoreBySellerId() de la interfaz StoreRepository.kt,
// para obtener una tienda por el id del vendedor:
    override suspend fun getStoreBySellerId(sellerId: String): Result<Store?> =
//            Esta fun devuelve un objeto de tipo Result<Store?>, segun el id del vendedor. pero puede ser nulo:
        runCatching {
            val sellerIdTrimmed = sellerId.trim() //Este es el id del vendedor.
//                COndicion para que el id del vendedor no este vacio:
            if (sellerIdTrimmed.isBlank()) {
                throw IllegalArgumentException("El id del vendedor no puede estar vacio")
            }
//                Llamamos a una fun para obtener la tienda en FireStore, por el id del vendedor.
//                La tienda la almacenamos en la val snapshot.
//                sellerId es el id del vendedor que crea la tienda, esta en el data class,
//                sellerIdTrimmed es el id del vendedor que estamos usando para consultar desde la app:
            val snapshot = storeRef().whereEqualTo("sellerId", sellerIdTrimmed)
                .limit(1) // Limitamos la consulta a 1 tienda.
                .get() // Obtenemos la tienda.
                .await() // Esperamos a que se obtenga la tienda de FireStore sin bloquear el hilo principal de la aplicacion.

//              Aca obtenemos el primer documento encontrado, queremos el primero o nada.
//                Si no existe la tienda, retornamos null:
            val document = snapshot.documents.firstOrNull()

//                Ahora convertimos ese documento en un objeto de clase Store y lo retornamos:
            document?.toObject(Store::class.java)


        }


    // Sobreescribimos la fun updateStore() de la interfaz StoreRepository.kt,
// fun para actualizar datos de una tienda existente. Hay que poner un parametro de tipo Store:
    override suspend fun updateStore(store: Store): Result<Unit> =
        runCatching {
//                Limpiamos el id y resto de datos de la tienda con trim para eliminar espacios en blanco al inicio y al final:
            val storeIdTrimmed = store.id.trim() //Este es el id de la tienda.
            val sellerIdTrimmed =
                store.sellerId.trim() //Este es el id del vendedor que crea la tienda, esta en el data class.
            val nameTrimmed = store.name.trim() //Este es el nombre de la tienda.
            val descriptionTrimmed = store.description.trim() //Esta es la descripcion de la tienda.
            val categoryTrimmed = store.category.trim() //Esta es la categoria de la tienda.
//                Luego validamos que los parametros de la tienda no esten vacios:
            if (storeIdTrimmed.isBlank()) {
                throw IllegalArgumentException("El id de la tienda no puede estar vacio")
            }
            if (sellerIdTrimmed.isBlank()) {
                throw IllegalArgumentException("El id del vendedor no puede estar vacio")
            }
            if (nameTrimmed.isBlank()) {
                throw IllegalArgumentException("El nombre de la tienda no puede estar vacio")
            }
            if (descriptionTrimmed.isBlank()) {
                throw IllegalArgumentException("La descripcion de la tienda no puede estar vacia")
            }
            if (categoryTrimmed.isBlank()) {
                throw IllegalArgumentException("Se debe seleccionar una categoria para la tienda")
            }

//                Ahora creamos una copia segura del objeto store con los datos limpiados con trim:
            val finalStore = store.copy(
                id = storeIdTrimmed, //Este es el id de la tienda.
                sellerId = sellerIdTrimmed, //Este es el id del vendedor que crea la tienda, esta en el data class.
                name = nameTrimmed, //Este es el nombre de la tienda.
                description = descriptionTrimmed, //Esta es la descripcion de la tienda.
                category = categoryTrimmed //Esta es la categoria de la tienda.
            )

//                Creamos el mapa con los nuevos datos que enviaremos a la BBDD de FireStore:
            val data =
                mapOf( //mapOf nos permite crear un mapa con los datos que enviaremos a la BBDD de FireStore:
                    "id" to finalStore.id,
                    "sellerId" to finalStore.sellerId,
                    "name" to finalStore.name,
                    "description" to finalStore.description,
                    "category" to finalStore.category,
                    "isActived" to finalStore.isActived,
                    "createdAt" to finalStore.createdAt
                )

//                Ahora enviamos los datos actualizados a la BBDD de FireStore,
//                para eso usamos el metodo set() del objeto storeRef() que tiene la coleccion de tiendas en FireStore:
            storeRef().document(storeIdTrimmed)
                .update(data) //Aca usamos el metodo update() para actualizar los datos de la tienda en FireStore.
                .await() // Esperamos a que se actualicen los datos de la tienda de FireStore sin bloquear el hilo principal de la aplicacion.

            Unit

        }
}


