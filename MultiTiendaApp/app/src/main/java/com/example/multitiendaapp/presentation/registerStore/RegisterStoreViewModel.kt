package com.example.multitiendaapp.presentation.registerStore

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.multitiendaapp.core.model.Store
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

//Este viewmodel es para la pantalla de registro de la tienda.

//Creamos un data class UiState, que es una clase de datos
// que se usa para representar el estado de la pantalla de registro de la tienda e ir actualizandolo:
data class UiState(
    val sellerUid: String = "", //repr el id del vendedor que registro la tienda.
    val storeName: String = "", //repr el nombre que el usuario ingresa en el campo de texto de la pantalla de registro de la tienda.
    val storeDescription: String = "", //repr la descripcion que el usuario ingresa en el campo de texto de la pantalla de registro de la tienda.
    val selectedCategoryId: String = "", //repr la categoria que el usuario selecciona en el menu desplegable de la pantalla de registro de la tienda.
    val isLoading: Boolean = false,
    //para saber si el registro esta cargando o no, para controlar operaciones, ej que los botones esten deshabilitados mientras no se ingrese los datos
    val errorMessage: String? = null //repr cualquier error que se presente en la pantalla de registro de la tienda.
)

//Creamos un sealed interface para los eventos de la pantalla de registro de la tienda.
//Los eventos son las acciones que el usuario realiza en la pantalla de registro de la tienda y que llegan a este viewmodel.
// Luego estos eventos se procesan mas abajo en la fun onEvent() del viewmodel.

sealed interface StoreEvent {
    data class OnStoreNameChange(val value: String) : StoreEvent

    //    En la var value guardamos el nuevo valor del nombre que el usuario ingreso(evento) en el campo de texto de la pantalla de registro de la tienda.
    data class OnStoreDescriptionChange(val value: String) : StoreEvent

    //    En la var value guardamos el nuevo valor de la descripcion que el usuario ingreso en el campo de texto de la pantalla de registro de la tienda.
    data class OnCategoryChange(val categoryId: String) : StoreEvent

    //    En la var value guardamos el nuevo valor de la categoria que el usuario selecciono en el menu desplegable de la pantalla de registro de la tienda.
    data object OnNextClick : StoreEvent
    //Indicamos el evento OnNextClick, que repr cuando el usuario hace click en el boton de registro en la base de datos FireStore.
}

//Creamos otra sealed interface para los efectos colaterales de la pantalla de registro de la tienda.
//Los efectos tambien son eventos o acciones que emitimos desde el viewmodel a la vista(UI) y que solo se ejecutan 1 vez,
// como mostrar mensaje, permisos, nav entre pantallas:

sealed interface SellerEffect {
    data class ShowMessage(
        val message: String
    ) : SellerEffect //repr un mensaje que se muestra en la pantalla de registro de la tienda
    // y que se emite una sola vez, desde el viewmodel a la vista(UI).

    data object NavigateToSellerHome : SellerEffect
    //repr el efecto que nos permitira navegar a la pantalla de registro de productos segun la categoria seleccionada,
// en que enviaremos el id del vendedor para registrarlo como dueño de la tienda.
}

@HiltViewModel //Indicamos que este viewmodel debe ser creado y gestionado por inyeccion de dependencias de Hilt.
class RegisterStoreViewModel @Inject constructor(
    private val storeRepository: StoreRepository, //Este es el repositorio de autenticacion que se inyecta en el viewmodel
    savedStateHandle: SavedStateHandle //Para capturar el id del vendedor que registro la tienda.
//Inyectamos la dependencia del repositorio de autenticacion, para registro de tienda en la base de datos FireStore.
) : ViewModel() // Indicamos que esta clase extiende de ViewModel y que vivira mientras la vista que lo llame este viva
// y que sobreviva a cambios de comfig como rotacion de pantalla.
{
    //    Creamos el estado interno y externo de la pantalla de registro de la tienda(UiState()):
//    Primero creamos el estado de la pantalla de registro de la tienda en su version interna,
//        //    que sera mutable pero solo desde el viewmodel.
////    // Y el estado inicial de cada campo, es el que esta def por defecto en el data class UiState(), ver arriba:
    private val _uiState = MutableStateFlow(UiState())

    //    Luego creamos el estado de pantalla de registro de la tienda en su version externa,
//    que estara expuesta a la vista(UI) para reaccionar a sus cambios y mostrarlos en la interfaz de usuario.
//    pero sera de solo lectura:
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

//    Ahora creamos el flujo de efectos colaterales de la pantalla de registro de la tienda, que sera mutable pero solo desde el viewmodel,
//    para emitir efectos colaterales a la vista(UI) como mostrar mensajes, navegar entre pantallas y que solo se emitiran 1 vez:

    private val _effect = MutableSharedFlow<SellerEffect>()
    //flujo interno para emitir efx de una vez, como son mostrar mensajes y navegar

    val effect: SharedFlow<SellerEffect> = _effect.asSharedFlow()
//    Esta es la version publica, o sea estara expuesta pero solo como lectura para coleccionarlas

    //Esta fun publica la creamos al final y es la puerta de entrada de la vista RegisterStoreScreen(UI) para interactuar con el viewmodel.
//    O sea se evalua que tipo de evento se recibe y se procesa en la fun correspondiente.
//    o sea si el usuario ingresa un nombre o apellido o correo o contraseña o hace click en el boton de registro,
//    el evento se procesa en la fun onEvent() y se ejecuta una de las 4 fun correspondientes.
    fun onEvent(event: StoreEvent) {
        when (event) {
            is StoreEvent.OnStoreNameChange -> updateStoreName(event.value) //Si hubo ingreso de nombre se envia ese evento a la fun updateStoreName()
            is StoreEvent.OnStoreDescriptionChange -> updateStoreDescription(event.value) //Si hubo ingreso de descripcion se envia ese evento a la fun updateStoreDescription()
            is StoreEvent.OnCategoryChange -> updateCategory(event.categoryId) //Si hubo seleccion de categoria se envia ese evento a la fun updateCategory()
            is StoreEvent.OnNextClick -> registerStore() //Si el usuario hace click en el boton de registro se envia ese evento a la fun registerStore()
        }
    }



    //    Ahora creamos las fun para actualizar el estado de la pantalla de registro de la tienda,
//    Esto significa que se actualiza el estado de los campos de texto del nombre, descripcion y categoria,
//    segun el evento que se recibe desde la vista: OnStoreNameChange, OnStoreDescriptionChange, OnCategoryChange y OnNextClick.
//    Entonces se dispara la fun correspondiente desde la fun onEvent(),
//    y se actualiza el estado de la pantalla de registro de la tienda:
//Primero el Nombre:
    private fun updateStoreName(value: String) {
//        hacemnos una copia del estado actual y actualizamos el valor del campo de texto del nombre con el que ingreso el usuario.
        _uiState.update { current ->
            current.copy(
                storeName = value, //actualizamos el valor del campo de texto del nombre con el que ingreso el usuario.
                errorMessage = null //borramos el mensaje de error si lo habia
            )
        }
    }

    //    La descripcion:
    private fun updateStoreDescription(value: String) {
//     hacemnos una copia del estado actual y actualizamos el valor del campo de texto de la descripcion con el que ingreso el usuario.
        _uiState.update { current ->
            current.copy(
                storeDescription = value, //actualizamos el valor del campo de texto de la descripcion con el que ingreso el usuario.
                errorMessage = null //borramos el mensaje de error si lo habia
            )
        }

    }

    //    Categoria:
    private fun updateCategory(categoryId: String) {
//    hacemnos una copia del estado actual y actualizamos el valor del campo de texto de la categoria con el que ingreso el usuario.
        _uiState.update { current ->
            current.copy(
                selectedCategoryId = categoryId, //actualizamos el valor del campo de texto de la categoria con el que ingreso el usuario.
                errorMessage = null //borramos el mensaje de error si lo habia
            )
        }

    }

    //    Ahora creamos una fun que se encargara de manejar los eventos del boton de registro de la tienda,
//    que contenga el flujo para registrar la tienda.
//    Es decir que cuando el usuario haga click en el boton de registro, se encargara de llamar a esta fun,
//    que va a verificar los datos del usuario y si son correctos.
    //    Ademas esta fun realiza una proteccion si se prersionea el boton de registro varias veces:

    private fun registerStore() {
        if (_uiState.value.isLoading) return
        //Este if lo que es hace es verificar si el estado de la pantalla de registro de la tienda es cargando, si esta cargando, esta fun no hara nada.
        // o sea si el ususario presiona el boton de registro varias veces, no hace nada porque se esta cargando.

        //    HArenos una corrutina sobre el ciclo de vida del viewmodel. Si el viewmodel muere, la corrutina tambien lo hara.
        //  Servira para operaciones asincronas:
        viewModelScope.launch {
            //Primero con trim() eliminamos los espacios en blanco al inicio y al final del texto.
            val storeNameTrimmed = _uiState.value.storeName.trim()
            val storeDescriptionTrimmed = _uiState.value.storeDescription.trim()
            val categoryIdTrimmed = _uiState.value.selectedCategoryId.trim()

            //            Ahora verificamos que los campos no esten vacios:
            if (storeNameTrimmed.isEmpty()) {
                _effect.emit(SellerEffect.ShowMessage("El nombre no puede estar vacio"))
                return@launch
                // Si el nombre esta vacio se envia el mensaje y se sale de la fun registerStore() lo que hace que no se haga el registro.
            }

            if (storeDescriptionTrimmed.isEmpty()) {
                _effect.emit(SellerEffect.ShowMessage("La descripcion no puede estar vacia"))
                return@launch

            }

            if (categoryIdTrimmed.isEmpty()) {
                _effect.emit(SellerEffect.ShowMessage("La categoria no puede estar vacia"))
                return@launch

            }

            //Ahora actualizamos el estado visual de la pantalla de registro de la tienda para que se muestre que esta cargando,
            // mientras se registra la tienda:
            _uiState.update { current ->
                //Actualizamos el estado de la pantalla de registro de la tienda, resetado a su estado inicial:
                current.copy(
                    isLoading = true, // Indica que esta cargando la pantalla de registro de vendedor.
                    errorMessage = null // Borra el mensaje de error si lo habia
                )
            }

            //Vamos a crear el objeto store(data class Store()) para enviarlo a la base de datos Firestore,
            // Para eso creamos una variable llamada store que almacena el resultado de la consulta a Firebase:
            val store = Store(
                sellerId = "",//aca va el id del vendedor que registro la tienda
                name = storeNameTrimmed,
                description = storeDescriptionTrimmed,
                category = categoryIdTrimmed
            )

//            Llamamos al reposirorio para registrar la tienda con la fun createStore(), y le pasamos el objeto store como parametro:
            val result = storeRepository.createStore(store)

//            Si el resultado del registro fue exitoso, el repositorio de autenticacion Firebase en este caso, nos devuelve un usuario autenticado.
            result.onSuccess {
                _uiState.update { current ->
                    //Actualizamos el estado de la pantalla de registro de la tienda, reseteando a su estado inicial:
                    current.copy(
                        storeName = "",
                        storeDescription = "",
                        selectedCategoryId = "",
                        isLoading = false, // Deja de estar cargando la pantalla de registro de la tienda luego de registrarse.
                        errorMessage = null // Borra el mensaje de error si lo habia
                    )
                }

//                efecto para mostrar un mensaje de registro exitoso:
                _effect.emit(SellerEffect.ShowMessage("La tienda fue creada exitosamente"))

//                Otro efecto para navegar a la pantalla de registro de productos segun la categoria seleccionada:
                _effect.emit(SellerEffect.NavigateToSellerHome)
            }
//            Si el proceso de registro no fue exitoso, el repositorio de autenticacion Firebase en este caso, nos devuelve un error:
                .onFailure { error ->
                    _uiState.update { current ->
                        current.copy( // Actualizamos el estado de la pantalla de registro de la tienda, reseteando a su estado inicial:
                            isLoading = false,
                            errorMessage = error.message ?: "No se pudo registrar la tienda"
                        )
                    }
//                    Finalmente llamamos a un efecto con un texto que muestre mensaje de arriba o bien un mensaje de error por defecto:
                    _effect.emit(SellerEffect.ShowMessage(error.message ?: "Registro fallido"))
                }



        }
    }
}