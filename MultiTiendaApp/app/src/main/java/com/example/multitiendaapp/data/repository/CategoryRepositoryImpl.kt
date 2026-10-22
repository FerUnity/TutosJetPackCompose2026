package com.example.multitiendaapp.data.repository

import com.example.multitiendaapp.core.model.Category
import com.example.multitiendaapp.domain.repository.CategoryRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

//Aca es donde se implementara toda la logica real de las fun declaradas en la interfaz CategoryRepository.kt,
// o sea que en la capa de domain se define lo que se quiere hacer(contrato)
// y en la capa data se implementa la logica real de las fun declaradas en la capa de la interfaz.
//Por ende en esta capa data, se conectara con la base de datos de FireStore, para obtener las categorias de una tienda y otra info.

//Esta implementacion sera usada por el ViewModel para obtener las categorias de una tienda.

class CategoryRepositoryImpl(
//    Recibimos una instancia de FirebaseFirestore, a firebase por hilt:
// FirebaseFirestore es para acceder a la base de datos de FireStore y
//// proviene del modulo FirestoreModule.kt ubicado en el paquete di, que provee la injeccion de dependencias de Firebase.
    private val firebase: FirebaseFirestore,
) : CategoryRepository {
//    Y aca implementamos con logica real, las fun declaradas en la interfaz CategoryRepository.kt:

    //    Para eso creamos una val que representa la coleccion de todas las categorias guardadas en FireStore:
    private val collectionCategories = "categories"

    //    Y luego creamos una fun privada que haga referencia a esa coleccion de categorias en FireStore,
//    para no tener que escribir firebase.collection() cada vez que se quiera acceder a la coleccion de categorias:
    private fun categoriesRef() = firebase.collection(collectionCategories)

    //    Y entonces implementamos las fun declaradas en la interfaz CategoryRepository.kt.
//    Usamos override porque se esta sobreescribiendo la fun ya declarada en la interfaz CategoryRepository.kt:
    override suspend fun createCategory(category: Category): Result<Unit> =
//        runCatching ejecuta el bloque que tenga entre {}
        //        y si ocurre un error lo captura y devuelve un Result con el resultado o excepcion automationicamente:
        runCatching {
//            Primero limpiamos los espacios en blanco de los 2 campos: nombre y storeId,
            // para que no haya espacios en blanco al inicio y al final de esos parametros de la categoria:
            val nameTrimmed = category.name.trim()
            val storeIdTrimmed = category.storeId.trim()

//        Luego validamos que el nombre y el storeId de la categoria no esten vacios:
            if (nameTrimmed.isBlank()) {
                throw IllegalArgumentException("El nombre de la categoria no puede estar vacio")
            }
            if (storeIdTrimmed.isBlank()) {
                throw IllegalArgumentException("El id de la tienda no puede estar vacio")
            }

//            Luego generamos una var para capturar el campo id de la categoria, que es generado por FireStore:
            val finalId = categoriesRef().document().id

//            Luego creamos la nueva Categoria como un obj final,
//            donde se guardaran los campos id, name y storeId, de la nueva categoria creada, en FireStore:
            val finalCategory = category.copy(
                //copy nos permite copiar un objeto de la clase Category para modificar algunos campos o en este caso todos los campos:
                id = finalId,
                name = nameTrimmed,
                storeId = storeIdTrimmed,
            )

//            Luego convertimos el objeto finalCategory a un mapa para enviarlo a FireStore, porque FireStore solo recibe mapas:
            //mapOf nos permite crear un mapa con los datos que enviaremos a la BBDD de FireStore:
            val data =
                mapOf(
                    "id" to finalCategory.id,
                    "name" to finalCategory.name,
                    "storeId" to finalCategory.storeId,
                )

//            Finalmente enviamos los datos mapeados a FireStore,
//            para eso usamos el metodo set() del objeto categoriesRef() que creamos antes:
            categoriesRef().document(finalId)
                .set(data)
                .await() // Esperamos a que se complete la tarea de registro en Firebase sin bloquear el hilo principal de la aplicacion.
        }


    //    Implementamos ahora la fun que obtiene todas las categorias(Lista de categorias) de una tienda especifica,
//    segun el id de la tienda:
    override fun getCategoriesByStoreFlow(storeId: String): Flow<List<Category>> =
//        Usamos Flow en vez de Result porque es una lista de categorias dinamica que se debe actualizar en tiempo real:
        callbackFlow {
//            Primero limpiamos los espacios en blanco del id de la tienda:
            val storeIdTrimmed = storeId.trim()
            //            Luego validamos que el id de la tienda no este vacio:
            if (storeIdTrimmed.isBlank()) {
//                Enviamos una lista vacia al callbackFlow si el id de la tienda esta vacio:
                trySend(emptyList())
                return@callbackFlow
                //O sea salimos de la fun callbackFlow para evitar cobsultas innecesarias a la BBDD Firestore..
            }

//            Fuera del if creamos un listener para la coleccion de categorias en Firestore:
            val registration = categoriesRef()
//                Aca creamos una consulta a la BBDD Firestore, para obtener la lista de categorias de una tienda especifica
                .whereEqualTo("storeId", storeIdTrimmed)

                //Ahora registramos un listener en Firestore, que se quedara escuchando cualquier cambio en la coleccion de categorias,
                //por ej que se agregue una nueva categoria, o que se elimine una categoria
                //para que finalmente se actualice la lista de categorias en tiempo real:
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        return@addSnapshotListener
//               O sea si hay error solo salimos del listener actual para evitar cobsultas innecesarias a la BBDD Firestore..
                    }
//                    Que pasa si snapshot es nulo, o sea si no hay categorias disponibles en la tienda,
//                    enviamos una lista vacia al callbackFlow:
                    if (snapshot == null) {
//                     Entonces enviamos una lista vacia al callbackFlow:
                        trySend(emptyList())
                        return@addSnapshotListener
//                        Para salir del listener actual para evitar cobsultas innecesarias a la BBDD Firestore..
                    }

//                    Ahora convertimos los documentos a objetos de tipo categoria para enviarlos al callbackFlow:
                    val categories = snapshot.documents.map { doc ->
                        Category(
                            id = doc.id,
                            name = doc.getString("name")
                                .orEmpty(),//.orEmpty() para conv null a vacio..
                            storeId = doc.getString("storeId").orEmpty()
                        )

                    }

                    trySend(categories)
                }
            awaitClose {
                registration.remove()
            // Removemos el listener cuando se elimine el callbackFlow para evitar leaks de memoria.
            }


//

        }
    /*        runCatching {
//            Primero limpiamos los espacios en blanco del id de la tienda
        val storeIdTrimmed = storeId.trim()

//            Luego validamos que el id de la tienda no este vacio:
        if (storeIdTrimmed.isBlank()) {
            throw IllegalArgumentException("El id de la tienda no puede estar vacio")
        }
//            Hacemos una consulta a la BBDD Firestore, para obtener la lista de categorias de una tienda especifica
//          cuyo id de tienda sea = storeIdTrimmed, que es el parametro limpiado de la fun.
//          snapshot es una variable que almacena el resultado de la consulta a la BBDD Firestore:
        val snapshot = categoriesRef()
            .whereEqualTo("storeId", storeIdTrimmed)
            .get() //get() nos permite obtener los datos de la consulta a la BBDD Firestore.
            .await() // Esperamos a que se complete la tarea de registro en Firebase sin bloquear el hilo principal de la aplicacion.

//            Luego creamos una lista mutable de categorias,
//            para almacenar los datos de la consulta que la BBDD Firestore nos devolvio,
//            cada document representa una categoria
//            pero esta en formato json, entonces lo convertimos a un objeto Category:
//            se hace asi:
        snapshot.documents
            .mapNotNull { doc ->
                doc.toObject(Category::class.java)
            }

    }*/


    //    Ahora implementamos la fun que actualiza "EL NOMBRE" de una categoria,
//    que recibe un objeto Category que es la categoria a actualizar:
    override suspend fun updateCategory(category: Category): Result<Unit> =
        runCatching {
//            Limpiamos los espacios en blanco de los 5 campos: id y name:
            val id = category.id.trim()
            val nameTrimmed = category.name.trim()

//            Luego validamos que el id y el name de la categoria no esten vacios:
            if (id.isBlank()) {
                throw IllegalArgumentException("El id de la categoria no puede estar vacio")
            }
            if (nameTrimmed.isBlank()) {
                throw IllegalArgumentException("El nombre de la categoria no puede estar vacio")
            }

//            Creamos el mapa que Firestore reconoce para actualizar el nombre de la categoria:
            val data = mapOf(
                "name" to nameTrimmed,
            )
//            Hacemos una ref a la coleccion de categorias en Firestore, para actualizar el nombre de la categoria:
            categoriesRef()
                .document(id)
                .update(data) //update() nos permite actualizar los datos de la coleccion de categorias en Firestore.
                .await() // Esperamos a que se complete la tarea de registro en Firebase sin bloquear el hilo principal de la aplicacion.
        }


    //    Ahora implementamos la fun que elimina una categoria en Firestore,
    //    segun el id de esa categoria que se recibe como parametro:
    override suspend fun deleteCategory(categoryId: String): Result<Unit> =
        runCatching {
//            Limpiamos los espacios en blanco del id de la categoria:
            val id = categoryId.trim()

            //            Luego validamos que el id de la categoria no este vacio:
            if (id.isBlank()) {
                throw IllegalArgumentException("El id de la categoria no puede estar vacio")
            }

//            No es necesario crear un mapa para eliminar una categoria en Firestore.

            //Hacemos una ref a la coleccion de categorias en Firestore,
            // para eliminar la categoria con el id que se recibe como parametro:
            categoriesRef()
                .document(id)
                .delete()
                .await() // Esperamos a que se complete la tarea de registro en Firebase sin bloquear el hilo principal de la aplicacion.
        }

    //            Ahora implementamos la fun que obtiene una categoria desde Firestore,
//            segun el id de esa categoria que se recibe como parametro:
    override suspend fun getCategoryById(categoryId: String): Result<Category?> =
        runCatching {
            //Limpiamos los espacios en blanco del id de la categoria:
            val id = categoryId.trim()
            //Luego validamos que el id de la categoria no este vacio:
            if (id.isBlank()) {
                throw IllegalArgumentException("El id de la categoria no puede estar vacio")
            }
            //Obtenemos un document, que representa la categoria que se quiere obtener, de la consulta a la BBDD Firestore,
            // para obtener la categoria con el id que se recibe como parametro.
            // doc sera la variable que almacena el resultado de la consulta a la BBDD Firestore,
            // document representa la categoria que se quiere obtener, pero esta en formato json,:
            val doc = categoriesRef()
                .document(id)
                .get()
                .await() // Esperamos a que se complete la tarea de registro en Firebase sin bloquear el hilo principal de la aplicacion.

            //Luego convertimos el documento de la consulta de json a un objeto Category:
            doc.toObject(Category::class.java)


        }
}

