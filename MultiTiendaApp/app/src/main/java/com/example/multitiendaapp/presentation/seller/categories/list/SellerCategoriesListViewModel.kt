package com.example.multitiendaapp.presentation.seller.categories.list

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

//Este viewmodel se usara para controlar la parte logica de la pantalla de lista de categorias: SellerCategoriesScreen.kt

//Primero creamos un data class que representa el estado de la pantalla de lista de categorias: SellerCategoriesScreen.kt
// y que se usara para controlar el estado de la pantalla de lista de categorias,
// o sea es lo que se mostrara en la pantalla de lista de categorias, segun los cambios de estado.
// Porque ira cambiando esa pantalla segun los cambios de estado: mostrara algunos campos o desactivara algunos campos, etc.

data class SellerCategoriesListUiState(
    val categories: List<Category> = emptyList(), //lista de categorias que se mostraran en la pantalla de lista de categorias
    val listLoading: Boolean = false, //para saber si la lista de categorias esta cargando o no, para controlar operaciones,
    // ej que los botones esten deshabilitados mientras no se carguen las categorias
    val errorMessage: String? = null //para saber si hubo un error en la pantalla de lista de categorias. Puede ser nulo
)

//Creamos una Sealed Interface que representa los eventos que vienen desde la vista de lista de categorias: SellerCategoriesScreen.kt,
//  hacia este viewmodel:

sealed interface SellerCategoriesListEvent {
    // creamos un objeto Load que representa el evento de cargar las categorias:
    data object Load : SellerCategoriesListEvent
}

//Creamos otro Sealed Interface que representa los efectos colaterales que se producen en la vista de lista de categorias:
// como mostrar mensajes, navegar a otra pantalla, etc. Se envian desde este viewmodel a la vista(UI): SellerCategoryFormScreen.kt
sealed interface SellerCategoriesListEffect {
    //data class que repr el efecto que se produce cuando se muestra un mensaje en la vista de lista de categorias
    data class ShowMessage(
        val message: String
    ) : SellerCategoriesListEffect
}

//Creamos otra sealed Interface para navegar desde la pantalla de lista de categorias,
// hacia la pantalla de formulario de creacion de categorias:
sealed interface SellerCategoriesListEffectNavigate {
    //data class que repr el efecto que se produce cuando se navega a la pantalla de formulario de creacion de categorias
    data object NavigateToForm : SellerCategoriesListEffectNavigate
}


@HiltViewModel //Para que este viewmodel pueda ser gestionado por hilt y pueda ser inyectado en otras clases de la app.
class SellerCategoriesListViewModel @Inject constructor(
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
//        Creamos el estado interno y externo de la pantalla de lista de categorias(SellerCategoriesScreen.kt),
//    entonces hacemos ref al data class SellerCategoriesListUiState()
//    que representa el estado de la pantalla de lista de categorias:

    //    Primero creamos el estado de la pantalla de lista de categorias en su version interna, para proteger de cambios en las vistas.
    //    Este estado es mutable pero solo desde el viewmodel, no desde la vista.:
    private val _uiState = MutableStateFlow(SellerCategoriesListUiState())

    //    Luego creamos el estado de la pantalla de lista de categorias en su version externa, expuesta en la vista pero solo lectura:
    val uiState: StateFlow<SellerCategoriesListUiState> = _uiState.asStateFlow()

    //    Ahora creamos el flujo interno para el efecto de ShowMessage, segun la sealed class SellerCategoriesListEffect().
    //    Esta es mutable pero solo desde el viewmodel, no desde la vista:
    private val _effect = MutableSharedFlow<SellerCategoriesListEffect>()

    //    Y luego creamos el flujo externo para los efectos, expuesta en la vista pero solo lectura,
//    para que no se pueda editarlo desde la vista:
    val effect: SharedFlow<SellerCategoriesListEffect> = _effect.asSharedFlow()

    //    Ahora creamos el flujo interno para el efecto de navegacion:
    private val _effectNavigate = MutableSharedFlow<SellerCategoriesListEffectNavigate>()

    //    Y ahora creamos el flujo externo para el efecto de navegacion, expuesta en la vista pero solo lectura,
    val effectNavigate: SharedFlow<SellerCategoriesListEffectNavigate> =
        _effectNavigate.asSharedFlow()

//    Creamos un init que es ejecutado automaticamente cuando se crea la instancia del viewmodel, lo cual se hace desde
//    la vista o pantalla de lista de categorias: SellerCategoriesScreen.kt:
    init {
//        En este caso llamamos a la fun loadAll() para obtener todas las categorias, al inicio de la pantalla de lista de categorias:
        loadAll()
    }

    //    Creamos fun para obtener todas las categorias, esta se invocara desde el init del viewmodel:
    private fun loadAll() {
//        Creamos una corrutina para obtener todas las categorias en segundo plano, ligada al ciclo de vida del viewmodel:
        viewModelScope.launch {
//            Actualizamos el estado de la pantalla de lista de categorias, actualizando desde Firebase:
            _uiState.update { current ->
                current.copy(
                    listLoading = true, //activamos el estado de carga
                    errorMessage = null //borramos el mensaje de error si lo habia
                )
            }
//            Ahora autenticamos al usuario actual:
            val user = authRepository.getCurrentUser()
//            Extraemos el id de ese vendedor, puede ser nulo:
            val sellerId = user?.uid
//            Validamos si el usuario no existe, es lo mismo que no este autenticado:
            if (sellerId.isNullOrBlank()) {
//             Si no existe, enviamos un mensaje de error y dejamos de cargar:
                _uiState.update { current ->
                    current.copy(
                        listLoading = false, //dejamos de cargar
                        errorMessage = "No se encontro el usuario autenticado"
                    )
                }
//                Y ademas emitimos un efecto con un mensaje de error:
                _effect.emit(SellerCategoriesListEffect.ShowMessage("No se encontro el usuario autenticado"))
                return@launch //Y cortamos la ejecucion de la corrutina
            }

//            Ahora si existe el usuario, obtenemos la tienda que le corresponde a ese vendedor segun su id:
            val storeResult = storeRepository.getStoreBySeller(sellerId)
//            En caso que sea exitoso que encontramos la tienda, obtenemos las categorias de la tienda encontrada:
            storeResult.onSuccess { store ->
//                Primero validamos si la tienda existe:
                if (store == null) {
//                    Si no encontramos la tienda, actualizamod el estado de la pantalla de lista de categorias,
                    // donde enviamos un mensaje de error y dejamos de cargar:
                    _uiState.update { current ->
                        current.copy(
                            listLoading = false, //dejamos de cargar
                            errorMessage = "Este vendedor no tiene ninguna tienda registrada"
                        )
                    }
//                    Y ademas emitimos un efecto con un mensaje de error:
                    _effect.emit(SellerCategoriesListEffect.ShowMessage("Este vendedor no tiene ninguna tienda registrada"))
                    return@onSuccess //Y cortamos la ejecucion de la corrutina
                }

//      Ahora si, fuera del if, implica que si existe una tienda registrada a ese vendedor,
//      obtenemos las categorias de la tienda encontrada, usando la fun getCategoriesByStore() del repositorio de categorias:
                val result = categoryRepository.getCategoriesByStore(store.id)
//            En caso que sea exitoso que encontramos las categorias,
//            actualizamos con update el estado de la pantalla de lista de categorias, con las categorias encontradas:
                result.onSuccess { list ->
                    _uiState.update { current ->
                        //hacemos una copia del estado actual y actualizamos el estado de la pantalla de lista de categorias:
                        current.copy(
                            categories = list, //actualizamos el estado de la pantalla de lista de categorias,
                            // con la lista de categorias encontradas
                            listLoading = false, //dejamos de cargar
                            errorMessage = null //borramos el mensaje de error si lo habia
                        )
                    }
//                    Y emitimos un efecto con un mensaje de exito:
                    _effect.emit(SellerCategoriesListEffect.ShowMessage("Lista cargada"))

//                    Ahora en caso que sea fallido que no encontramos las categorias, aunque si encontramos la tienda:
                }.onFailure { error ->
                    _uiState.update { current ->
                        //hacemos una copia del estado actual y actualizamos el estado de la pantalla de lista de categorias
                        current.copy(
                            listLoading = false, //dejamos de cargar
                            errorMessage = error.message
                                ?: "No se pudo obtener la lista de categorias"
                        )
                    }
//                    Y ademas emitimos un efecto con un mensaje de error usando el efecto colateral:
                    _effect.emit(
                        SellerCategoriesListEffect.ShowMessage(
                            error.message ?: "No se pudo obtener la lista de categorias"
                        )
                    )
                }

//                Ahora si no existe la tienda, es decir, si el usuario no tiene ninguna tienda registrada,
            }.onFailure { error ->
//                Actualizamos el estado de la pantalla de lista de categorias,
                _uiState.update { current ->
                    //hacemos una copia del estado actual y actualizamos el estado de la pantalla de lista de categorias
                    current.copy(
                        listLoading = false, //dejamos de cargar
                        errorMessage = error.message ?: "No se pudo obtener la Tienda" //OJO
                    )
                }
//                Y ademas emitimos un efecto con un mensaje de error usando el efecto colateral:
                _effect.emit(
                    SellerCategoriesListEffect.ShowMessage(
                        error.message ?: "No se pudo obtener la Tienda"
                    )
                )

            }

        }
    } //Aca finaliza la fun loadAll()
}

