package com.example.multitiendaapp.presentation.seller.categories.form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.multitiendaapp.core.model.Category
import com.example.multitiendaapp.domain.repository.AuthRepository
import com.example.multitiendaapp.domain.repository.CategoryRepository
import com.example.multitiendaapp.domain.repository.StoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

//Este viewmodel es para controlar la parte logica de la pantalla de creacion de categorias: SellerCategoryFormScreen.kt

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
//    Creamos 3 data class que representan los eventos que vienen desde la vista de creacion de categorias
    //    y que seran procesados por la fun onEvent() de este viewmodel:
    data class OnNameChange(val value: String) : SellerCategoryFormEvent

    //repr el evento cuando el usuario ingresa el nombre de la categoria en el campo de texto de la pantalla de creacion de categorias.
//    data class OnStoreIdChange(val value: String) : SellerCategoryFormEvent

    //repr el evento cuando el usuario ingresa el id de la tienda en el campo de texto de la pantalla de creacion de categorias.
    data object OnCreateClick : SellerCategoryFormEvent
//repr el evento cuando el usuario hace click en el boton de crear categoria.

}

//Otra Sealed interface que representa los efectos colaterales que se producen en la vista de creacion de categorias,
// como mostrar mensajes, navegar a otra pantalla, etc. Se envian desde este viewmodel a la vista(UI): SellerCategoryFormScreen.kt
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
//        Creamos el estado interno y externo de la pantalla de creacion de categorias(SellerCategoryFormScreen.kt),
    //entonces hacemos ref al data class SellerCategoryFormUiState()
    // que representa el estado de la pantalla de creacion de categorias:

    //        Primero creamos el estado de la pantalla de creacion de categorias en su version interna, para proteger de cambios en las vistas:
    private val _uiState = MutableStateFlow(SellerCategoryFormUiState())

    //    Luego creamos el estado de la pantalla de creacion de categorias en su version externa, expuesta en la vista pero solo lectura:
    val uiState: StateFlow<SellerCategoryFormUiState> = _uiState.asStateFlow()

    //    Ahora creamos el flujo interno para los efectos, segun la sealed class SellerCategoryFormEffect():
    private val _effect = MutableSharedFlow<SellerCategoryFormEffect>()

    //    Luego creamos el flujo externo para los efectos, expuesta en la vista pero solo lectura:
    val effect: SharedFlow<SellerCategoryFormEffect> = _effect.asSharedFlow()

    //    Creamos una fun onEvent para manejar los eventos del usuario, que vienen desde la vista de creacion de categorias,
//    o sea es la puerta de entrada del usuario, en la vista de creacion de categorias, para interactuar con el viewmodel:
    fun onEvent(event: SellerCategoryFormEvent) {
        when (event) {
            is SellerCategoryFormEvent.OnNameChange -> updateName(event.value)
            //Si el usuario ingresa el nombre de la categoria, lo procesamos en la fun updateName()

//          EL StoreId no se gestiona aca porque se gestiona automaticamente en el init del viewmodel,

            is SellerCategoryFormEvent.OnCreateClick -> createCategory()
            //Si el usuario hace click en el boton de crear categoria, lo procesamos en la fun createCategory()

        }
    }

    //    Aca creamos un init que es ejecutado automaticamente cuando se crea la instancia del viewmodel:
    init {
        loadStoreId() //llamamos a la fun loadStoreId() para obtener el id de la tienda del vendedor actual.
    }

    //    Creamos 3 funciones esenciales:
    //    Primero una fun que nos permita obtener el id de la tienda del vendedor actual,
    //    esta fun se llamara en el init del viewmodel para obtener el id de la tienda del vendedor actual:
    private fun loadStoreId() {
        viewModelScope.launch {
//            Para eso es obtener un usuario autenticado desde firebase auth:
            val user = authRepository.getCurrentUser()
//            Extraemos el uid del usuario autenticado, puede ser nulo:
            val sellerId = user?.uid
//            Validamos si el usuario existe:
            if (sellerId.isNullOrBlank()) {
//                Si el usuario no existe, enviamos un mensaje de error:
                _effect.emit(SellerCategoryFormEffect.ShowMessage("No se encontro el usuario autenticado"))
                return@launch
                //O sea salimos de la corrutina
            }
//            Pero si el usuario si existe, continuamos con el siguiente paso, activamos el estado de carga:
            _uiState.update { current ->
                current.copy(
                    isLoading = true, //activamos el estado de carga
                    errorMessage = null //borramos el mensaje de error si lo habia
                )
            }

//            Una vez obtenido el usuario vendedor autenticado, obtenemos la tienda que le corresponde:
            val result = storeRepository.getStoreBySeller(sellerId)
            result.onSuccess { store ->
                if (store == null) {
//                    Si la tienda no existe, enviamos un mensaje de error y dejamos de cargar:
                    _uiState.update { current ->
                        current.copy(
                            isLoading = false, //dejamos de cargar
                            errorMessage = "No se encontro la tienda del vendedor"
                        )
                    }

                    _effect.emit(SellerCategoryFormEffect.ShowMessage("Este vendedor no tiene ninguna tienda registrada"))
                    return@onSuccess //salimos de la corrutina

                }

//                En caso que la tienda si exista, actualizamos el estado de la pantalla de creacion de categorias
//                y almcenamos el id de la tienda del vendedor, en el estado de la pantalla de creacion de categorias:
                _uiState.update { current ->
                    current.copy(
                        storeId = store.id, //almacenamos el id de la tienda en el estado de la pantalla de creacion de categorias
                        isLoading = false //dejamos de cargar
                    )
                }

            }.onFailure { error ->
                _uiState.update { current ->
//                hacemos una copia del estado actual y actualizamos el estado de la pantalla de creacion de categorias:
                    current.copy(
                        isLoading = false, //dejamos de cargar
                        errorMessage = error.message
                    )
                }
//                Y enviamos un mensaje de error usando el efecto colateral:
                _effect.emit(
                    SellerCategoryFormEffect.ShowMessage(
                        error.message ?: "No se pudo obtener la tienda del vendedor"
                    )
                )
            }
        }
    }

    //    Creamos otra fun para actualizar el nombre de la categoria, en el estado de la pantalla de creacion de categorias:
    private fun updateName(value: String) {
        _uiState.update { current ->
            current.copy(
                name = value, //actualizamos el nombre de la categoria con el que ingreso el usuario, sera value que va como parametro en la fun.
                errorMessage = null //borramos el mensaje de error si lo habia
            )
        }
    }

    //    Creamos otra fun que permita crear la categoria, cuando el usuario hace click en el boton de crear categoria:
    private fun createCategory() {
        if (_uiState.value.isLoading) return //si el estado de la pantalla de creacion de categorias esta cargando, no hacemos nada,
// para evitar que el boton de crear categoria se pueda presionar varias veces. Cuando ya este creando la categoria, el boton se desactiva.
//    Creamos una corrutina para crear la categoria en segundo plano,
//    primero se limpia el nombre ingresado por el usuario y el id,
//    para que al presionar el boton de crear categoria:
        viewModelScope.launch {
//        Primero limpiamos el nombre ingresado por el usuario:
            val nameTrimmed = _uiState.value.name.trim()
//        Obtenemos el valor del id de la tienda del vendedor actual qie ya obtuvimo en el init:
            val storeId = _uiState.value.storeId

//        Validamos nombre y id:
            if (nameTrimmed.isEmpty()) {
                _effect.emit(SellerCategoryFormEffect.ShowMessage("El nombre de la categoria no puede estar vacio"))
                return@launch
                //O sea salimos de la corrutina
            }

            if (storeId.isBlank()) {
                _effect.emit(SellerCategoryFormEffect.ShowMessage("No se encontro la tienda del vendedor"))
                return@launch
                //O sea salimos de la corrutina
            }
//        Activamos el estyado de carga de la pantalla de creacion de categorias:
            _uiState.update { current ->
                current.copy(
                    isLoading = true, //activamos el estado de carga
                    errorMessage = null //borramos el mensaje de error si lo habia
                )
            }

//        Creamos un objeto Category con el nombre de la categoria y el id de la tienda del vendedor actual:
            val category = Category(
//            id no lo pasamos porque es generado por firebase
                name = nameTrimmed,
                storeId = storeId
            )

//        Ahora si llamamos a la fun createCategory() del repositorio de categorias,
//        para crear la categoria en la BBDD firebase:
            val result = categoryRepository.createCategory(category)

//        Ahora validamos el resultado de la creacion de la categoria.
//        Si fue exitosa, actualizamos el estado de la pantalla de creacion de categorias a su estado inicial:
            result.onSuccess {
                _uiState.update { current ->
                    current.copy(
                        name = "", //limpiamos el nombre de la categoria
                        isLoading = false, //dejamos de cargar
                        errorMessage = null //borramos el mensaje de error si lo habia
                    )
                }
//            Generamos un efecto para mostrar un mensaje de creacion exitosa:
                _effect.emit(SellerCategoryFormEffect.ShowMessage("Categoria creada exitosamente"))
//            Y generamos un efecto para navegar a la pantalla anterior:
                _effect.emit(SellerCategoryFormEffect.NavigateBack)

//                Finalmente queremos que nos direccione a la pantalla de lista de categorias: SellerCategoriesScreen.kt:
            /*    navController.navigate(AppRoute.SellerCategoriesList.route)*/

//            Ahora si fallo la creacion de la categoria,
                //            nos devuelve un error,
                //            entonces hacemos una copia del estado actual de la pantalla de creacion de categorias, y
                //            actualizamos el estado de la pantalla en que ahora dejamos de cargar y
                //            mostramos el error:
            }.onFailure { error ->
                _uiState.update { current ->
                    current.copy(
                        isLoading = false, //dejamos de cargar
                        errorMessage = error.message ?: "No se pudo crear la categoria"
                    )
                }
//            Y ademas emitimos un efecto para mostrar el error:
                _effect.emit(
                    SellerCategoryFormEffect.ShowMessage(
                        error.message ?: "No se pudo crear la categoria"
                    )
                )
            }
        }
    }


}
