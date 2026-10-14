package com.example.multitiendaapp.presentation.seller.categories.form

import androidx.lifecycle.ViewModel
import com.example.multitiendaapp.domain.repository.AuthRepository
import com.example.multitiendaapp.domain.repository.CategoryRepository
import com.example.multitiendaapp.domain.repository.StoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

//Creamos un data class que representa el estado de la pantalla de creacion de categorias: SellerCategoryFormScreen.kt
data class SellerCategoryFormUiState(
    val name: String = "", //nombre de la categoria que el usuario ingrese en el campo de texto de la pantalla de creacion de categorias
    val storeId: String = "", //id de la tienda, relacion con la tienda del vendedor que crea la categoria
    val isLoading: Boolean = false, //para saber si la pantalla esta cargando o no, para controlar operaciones,
    // ej que los botones esten deshabilitados mientras no se ingrese los datos
    val errorMessage: String? = null //para saber si hubo un error en la pantalla de creacion de categorias. Puede ser nulo
)

//Este sealed interface representa los eventos que vienen desde la vista de creacion de categorias:
// SellerCategoryFormScreen.kt
//Porque esa vista no ejecuta logica, solo envia eventos a este viewmodel:
sealed interface SellerCategoryFormEvent {
    data class OnNameChange(val value: String) : SellerCategoryFormEvent //repr el evento cuando el usuario ingresa el nombre de la categoria en el campo de texto de la pantalla de creacion de categorias.
    data class OnStoreIdChange(val value: String) : SellerCategoryFormEvent //repr el evento cuando el usuario ingresa el id de la tienda en el campo de texto de la pantalla de creacion de categorias.
    data object OnCreateClick : SellerCategoryFormEvent //repr el evento cuando el usuario hace click en el boton de crear categoria.

}

//Otra Sealed interface que representa los efectos colaterales que se producen en la vista de creacion de categorias,
// como mostrar mensajes, navegar a otra pantalla, etc. Se envian desde el viewmodel a la vista(UI)
sealed interface SellerCategoryFormEffect {
    //repr el efecto que se produce cuando se muestra un mensaje en la vista de creacion de categorias
    data class ShowMessage(
        val message: String
    ) : SellerCategoryFormEffect

//    Otro efecto para navegar a la pantalla anterior en la vista, luego de crear la categoria:
    data object NavigateBack : SellerCategoryFormEffect


}




@HiltViewModel //Para que este viewmodel pueda ser gestionado por hilt y pueda ser inyectado en otras clases de la app.
class SellerCategoryFormViewModel @Inject constructor(
//    Inyectamos la dependencia del repositorio de categorias,
//    para que el viewmodel pueda acceder a los metodos de la interfaz CategoryRepository,
//    por ej para crear una categoria, obtener todas las categorias de una tienda, borrar una categoria, etc.
    private val categoryRepository: CategoryRepository,

    //Inyectamos la dependencia del repositorio de autenticacion,
    // para que el viewmodel pueda acceder a los metodos de la interfaz AuthRepository,
    // por ej para obtener al usuario actual, logueado o no, etc.
    private val authRepository: AuthRepository,

//    Inyectamos la dependencia del repositorio de tiendas,
//    para que el viewmodel pueda acceder a los metodos de la interfaz StoreRepository,
//    por ej para crear una tienda, obtener todas las tiendas, borrar una tienda, etc.,
    private val storeRepository: StoreRepository

    ) : ViewModel() {
//        Creamos el estado interno y externo de la pantalla de creacion de categorias(SellerCategoryFormScreen):
//        Primero creamos el estado de la pantalla de creacion de categorias en su version interna,
        private val _uiState = MutableStateFlow(SellerCategoryFormUiState())

//    Creamos el estado de la pantalla de creacion de categorias en su version externa, expuesta en la vista pero solo lectura:
    val uiState: StateFlow<SellerCategoryFormUiState> = _uiState.asStateFlow()


}