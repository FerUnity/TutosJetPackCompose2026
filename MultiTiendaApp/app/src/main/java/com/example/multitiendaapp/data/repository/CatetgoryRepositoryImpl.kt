package com.example.multitiendaapp.data.repository

import com.example.multitiendaapp.core.model.Category
import com.example.multitiendaapp.domain.repository.CategoryRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

//Aca es donde se implementara toda la logica real de las fun declaradas en la interfaz CategoryRepository.kt,
// o sea que en la capa de domain se define lo que se quiere hacer(contrato)
// y en la capa data se implementa la logica real de las fun declaradas en la capa de la interfaz.
//Por ende en esta capa data, se conectara con la base de datos de FireStore, para obtener las categorias de una tienda y otra info.

//Esta implementacion sera usada por el ViewModel para obtener las categorias de una tienda.

class CatetgoryRepositoryImpl(
//    Recibimo a firebase por hilt:
// FirebaseFirestore es para acceder a la base de datos de FireStore y
//// proviene del modulo FirestoreModule.kt ubicado en el paquete di, que provee la injeccion de dependencias de Firebase.
    private val firebase: FirebaseFirestore
) : CategoryRepository {
//    Y aca implementamos con logica real, las fun declaradas en la interfaz CategoryRepository.kt:

    //    Para eso creamos una val que representa la coleccion de todas las categorias guardadas en FireStore:
    private val collectionCategories = "categories"
//    Y luego creamos una fun privada que haga referencia a la coleccion de categorias en FireStore,
//    para no tener que escribir firebase.collection() cada vez que se quiera acceder a la coleccion de categorias:
    private fun categoriesRef() = firebase.collection(collectionCategories)

//    Y entonces implementamos las fun declaradas en la interfaz CategoryRepository.kt.
//    Usamos override porque se esta sobreescribiendo la fun ya declarada en la interfaz CategoryRepository.kt:
    override suspend fun createCategory(category: Category): Result<Unit> =
//        runCatching ejecuta el bloque que tenga y devuelve un Result con el resultado o excepcion automationicamente:
        runCatching {
//            Primero limpiamos los espacios en blanco del nombre y storeId,
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

//            Luego generamos una var para capturar el id final de la categoria, generado por FireStore:
            val finalId = categoriesRef().document().id

//            Luego crramos un obj final para vguardar en BBDD FireStore:
            val finalCategory = category.copy(
                //copy nos permite copiar un objeto de la clase Category para modificar algunos o en este caso todos los campos:
                id = finalId,
                name = nameTrimmed,
                storeId = storeIdTrimmed
            )

//            Luego convertimos el objeto finalCategory a un mapa para enviarlo a FireStore, porque FireStore solo recibe mapas:
            val data = mapOf( //mapOf nos permite crear un mapa con los datos que enviaremos a la BBDD de FireStore:
                "id" to finalCategory.id,
                "name" to finalCategory.name,
                "storeId" to finalCategory.storeId
            )

//            Luego enviamos los datos mapeados a FireStore, para eso usamos el metodo set() del objeto categoriesRef() que creamos antes:
            categoriesRef().document(finalId)
                .set(data)
                .await() // Esperamos a que se complete la tarea de registro en Firebase sin bloquear el hilo principal de la aplicacion.
            Unit


        }


    override suspend fun getCategoriesByStore(storeId: String): Result<List<Category>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateCategory(category: Category): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCategory(categoryId: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun getCategoryById(categoryId: String): Result<Category?> {
        TODO("Not yet implemented")
    }
}

