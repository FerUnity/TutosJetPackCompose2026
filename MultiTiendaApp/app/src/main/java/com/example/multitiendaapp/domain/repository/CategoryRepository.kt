package com.example.multitiendaapp.domain.repository

import com.example.multitiendaapp.core.model.Category

//Una interfaz define todas las operaciones disponibles para una clase,
// en este caso Category.
// Solo se declaran las fun, que son suspendidas, pero no las implementaciones logicas:

interface CategoryRepository {
//    Creamos una fun que cree una Categoria, que reciba un objeto Category a ser creada, y devuelva un Result<Unit>
    suspend fun createCategory(
    category: Category
    ): Result<Unit> //Unit significa que no devuelve nada. Solo exito o error

//    Creanos otra fun para obtener todas las categorias(lista de categorias) de una tienda, segun el id de la tienda,
    //    esta info se encuentra en la base de datos de FireStore:
    suspend fun getCategoriesByStore(
        storeId: String
    ): Result<List<Category>> //Devuelve una lista de categorias

//    Otra fun para actualizar una categoria, que reciba un objeto Category que es la categoria a actualizar
    //    y devuelva un Result<Unit>: o sea nada.
    suspend fun updateCategory(
        category: Category
    ): Result<Unit>

//    Otra fun para eliminar la categoria de la Base de datos segun el id de la categoria:
    suspend fun deleteCategory(
        categoryId: String
    ): Result<Unit>

//    Otra fun para obtener una categoria segun el id de la categoria:
    suspend fun getCategoryById(
        categoryId: String
    ): Result<Category?> //Devuelve una categoria o null si no existe.
}