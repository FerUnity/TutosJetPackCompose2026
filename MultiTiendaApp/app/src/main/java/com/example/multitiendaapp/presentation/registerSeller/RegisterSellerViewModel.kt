package com.example.multitiendaapp.presentation.registerSeller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.multitiendaapp.core.model.UserRole
import com.example.multitiendaapp.domain.repository.AuthRepository
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

//Este viewmodel es para la pantalla de registro de vendedor.

//Creamos un data class UiState, que es una clase de datos
// que se usa para representar el estado de la pantalla de registro de vendedor e ir actualizandolo:
data class UiState(
    val firstName: String = "", //repr el nombre que el usuario ingresa en el campo de texto de la pantalla de registro de vendedor, por defecto esta vacio.
    val lastName: String = "", //repr el apellido que el usuario ingresa en el campo de texto de la pantalla de registro de vendedor, por defecto esta vacio.
    val email: String = "", // repr el correo electronico que el usuario ingresa en el campo de texto de la pantalla de registro de vendedor, por defecto esta vacio.
    val password: String = "", //repr la contraseña que el usuario ingresa en el campo de texto de la pantalla de registro de vendedor, por defecto esta vacio.
    val confirmPassword: String = "", //repr la confirmacion de la contraseña que el usuario ingresa en el campo de texto de la pantalla de registro de vendedor, por defecto esta vacio.
    val phone: String = "", //repr el telefono que el usuario ingresa en el campo de texto de la pantalla de registro de vendedor, por defecto esta vacio.
    val isLoading: Boolean = false,
    //para saber si el registro esta cargando o no, para controlar operaciones, ej que los botones esten deshabilitados mientras no se ingrese los datos,
    // por defecto esta en false.
    val errorMessage: String? = null //repr cualquier error que se presente en la pantalla de registro de vendedor, por defecto esta en null.
)

//Creamos un sealed interface para los eventos de la pantalla de registro de vendedor o RegisterSellerScreen().
//Los eventos son las acciones que el usuario realiza en la pantalla de registro de vendedor y que llegan a este viewmodel.
// Luego estos eventos se procesan mas abajo en la fun onEvent() del viewmodel.

sealed interface SellerEvent {
    data class OnFirstNameChange(val value: String) : SellerEvent

    //    En la var value guardamos el nuevo valor de nombre que el usuario ingreso(evento) en el campo de texto de la pantalla de registro de vendedor.
    data class OnLastNameChange(val value: String) : SellerEvent

    //    En la var value guardamos el nuevo valor de apellido que el usuario ingreso en el campo de texto de la pantalla de registro de vendedor.
    data class OnEmailChange(val value: String) : SellerEvent

    //    En la var value guardamos el nuevo valor de correo electronico que el usuario ingreso en el campo de texto de la pantalla de registro de vendedor.
    data class OnPasswordChange(val value: String) : SellerEvent

    //    En la var value guardamos el nuevo valor de contraseña que el usuario ingreso en el campo de texto de la pantalla de registro de vendedor.
    data class OnConfirmPasswordChange(val value: String) : SellerEvent

    //    En la var value guardamos el nuevo valor de confirmacion de contraseña que el usuario ingreso en el campo de texto de la pantalla de registro de vendedor.
    data class OnPhoneChange(val value: String) : SellerEvent
//    En la var value guardamos el nuevo valor de telefono que el usuario ingreso en el campo de texto de la pantalla de registro de vendedor.

    data object OnNextClick : SellerEvent
    //Indicamos el evento OnNextClick, que repr cuando el usuario hace click en el boton de registro en la base de datos FireStore.
    // Como no hay datos extra como los casos anteiores, no pasamos nada como parametro, solo usamos data object:

}

//Creamos otra sealed interface para los efectos colaterales de la pantalla de registro de vendedor.
//Los efectos son las acciones que emitimos desde el viewmodel a la vista(UI) y que solo se ejecutan 1 vez,
// como mostrar mensaje, permisos, nav entre pantallas:
sealed interface SellerEffect {
    data class ShowMessage(
        val message: String
    ) : SellerEffect //repr un mensaje que se muestra en la pantalla de registro de vendedor
    // y que se emite una sola vez, desde el viewmodel a la vista(UI).

    data class NavigateToRegisterScreen(
        val sellerUid: String
    ) : SellerEffect
    //repr el efecto que nos permitira navegar a la pantalla de registro de la tienda,
// en que enviaremos el id del vendedor para registrarlo como dueño de la tienda.

}


@HiltViewModel //Indicamos que este viewmodel debe ser creado y gestionado por inyeccion de dependencias de Hilt.
class RegisterSellerViewModel @Inject constructor(
    private val authRepository: AuthRepository //Este es el repositorio de autenticacion que se inyecta en el viewmodel
//Inyectamos la dependencia del repositorio de autenticacion, para registro de vendedor en la base de datos FireStore.
) : ViewModel() // Indicamos que esta clase extiende de ViewModel y que vivira mientras la vista que lo llame este viva
// y que sobreviva a cambios de comfig como rotacion de pantalla.
{
    //    Creamos el estado interno y externo de la pantalla de registro de vendedor(UiState()):
//    Primero creamos el estado de la pantalla de registro de vendedor en su version interna,
//        //    que sera mutable pero solo desde el viewmodel.
////    // Y el estado inicial de cada campo, es el que esta def por defecto en el data class UiState(), ver arriba:
    private val _uiState = MutableStateFlow(UiState())

    //    Luego creamos el estado de pantalla de registro de vendedor en su version externa,
//    que estara expuesta a la vista(UI) para reaccionar a sus cambios y mostrarlos en la interfaz de usuario.
//    pero sera de solo lectura:
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    //    Ahora creamos el flujo de efectos colaterales de la pantalla de registro de vendedor, que sera mutable pero solo desde el viewmodel,
//    para emitir efectos colaterales a la vista(UI) como mostrar mensajes, navegar entre pantallas y que solo se emitiran 1 vez:
    private val _effect = MutableSharedFlow<SellerEffect>()
    //flujo interno para emitir efx de una vez, como son mostrar mensajes y navegar

    val effect: SharedFlow<SellerEffect> = _effect.asSharedFlow()
//    Esta es la version publica, o sea estara expuesta en la vista(UI) para reaccionar a sus cambios,
//    pero solo como lectura para colectarlas


//Esta fun publica la creamos al final y es la puerta de entrada de la vista RegisterSellerScreen(UI) para interactuar con el viewmodel.
//    O sea se evalua que tipo de evento se recibe y se procesa en la fun correspondiente.
//    o sea si el usuario ingresa un nombre o apellido o correo o contraseña o hace click en el boton de registro,
//    el evento se procesa en la fun onEvent() y se ejecuta una de las 7 fun correspondientes.

    fun onEvent(event: SellerEvent) {
        when (event) {
            is SellerEvent.OnFirstNameChange -> updateFirstName(event.value) //Si hubo ingreso de nombre se envia ese evento a la fun updateFirstName()
            is SellerEvent.OnLastNameChange -> updateLastName(event.value) //Si hubo ingreso de apellido se envia ese evento a la fun updateLastName()
            is SellerEvent.OnEmailChange -> updateEmail(event.value) //Si hubo ingreso de correo se envia ese evento a la fun updateEmail()
            is SellerEvent.OnPasswordChange -> updatePassword(event.value) //Si hubo ingreso de contraseña se envia ese evento a la fun updatePassword()
            is SellerEvent.OnConfirmPasswordChange -> updateConfirmPassword(event.value) //Si hubo ingreso de confirmacion de contraseña se envia ese evento a la fun updateConfirmPassword()
            is SellerEvent.OnPhoneChange -> updatePhone(event.value) //Si hubo ingreso de telefono se envia ese evento a la fun updatePhone()
            is SellerEvent.OnNextClick -> registerSeller() //Si el usuario hace click en el boton de registro se envia ese evento a la fun registerSeller()
        }
    }


    //    Ahora creamos las fun para actualizar el estado de la pantalla de registro de vendedor,
//    Esto significa, , que se actualiza el estado de los campos de texto del nombre, apellidos, correo, contraseña, confirmacion de contraseña y telefono.
//    Ademas una fun para manejar el evento de click en el boton de registro que revisa los datos ingresados por el usuario:
//Primero el Nombre
    private fun updateFirstName(value: String) {
        //hacemos una copia del estado actual y actualizamos el valor del campo de texto del nombre con el que ingreso el usuario.
        _uiState.update { current ->
            current.copy(
                firstName = value, //actualizamos el valor del campo de texto del nombre con el que ingreso el usuario.
                errorMessage = null //borramos el mensaje de error si lo habia
            )
        }

    }

    //    Apellidos:
    private fun updateLastName(value: String) {
        //hacemos una copia del estado actual y actualizamos el valor del campo de texto del apellido con el que ingreso el usuario.
        _uiState.update { current ->
            current.copy(
                lastName = value, //actualizamos el valor del campo de texto del apellido con el que ingreso el usuario.
                errorMessage = null //borramos el mensaje de error si lo habia
            )
        }

    }

    //    email:
    private fun updateEmail(value: String) {
        //hacemos una copia del estado actual y actualizamos el valor del campo de texto del correo electronico con el que ingreso el usuario.
        _uiState.update { current ->
            current.copy(
                email = value, //actualizamos el valor del campo de texto del correo electronico con el que ingreso el usuario.
                errorMessage = null //borramos el mensaje de error si lo habia
            )
        }
    }

    //    password:
    private fun updatePassword(value: String) {
        //hacemos una copia del estado actual y actualizamos el valor del campo de texto de la contraseña
        _uiState.update { current ->
            current.copy(
                password = value, //actualizamos el valor del campo de texto de la contraseña con el que ingreso el usuario.
                errorMessage = null //borramos el mensaje de error si lo habia
            )
        }
    }

    //    ConfirmPassword:
    private fun updateConfirmPassword(value: String) {
        //hacemos una copia del estado actual y actualizamos el valor del campo de texto de la confirmacion de contraseña con el que ingreso el usuario.
        _uiState.update { current ->
            current.copy(
                confirmPassword = value, //actualizamos el valor del campo de texto de la confirmacion de contraseña con el que ingreso el usuario.
                errorMessage = null //borramos el mensaje de error si lo habia
            )
        }
    }

    //    telefono:
    private fun updatePhone(value: String) {
        //hacemos una copia del estado actual y actualizamos el valor del campo de texto del telefono con el que ingreso el usuario.
        _uiState.update { current ->
            current.copy(
                phone = value, //actualizamos el valor del campo de texto del telefono con el que ingreso el usuario.
                errorMessage = null //borramos el mensaje de error si lo habia
            )
        }
    }

//    Ahora creamos una fun cuando se presione el BOTON de registro del vendedor,
//    que se encargara de manejar los eventos de la pantalla de registro de vendedor,
//    que contenga el flujo para registrar el vendedor.
//    Es decir que cuando el usuario haga click en el boton de registro, se encargara de llamar a esta fun,
//    que va a verificar los datos del usuario y si son correctos.
    //    Ademas esta fun realiza una proteccion si se prersiona el boton de registro varias veces:

    private fun registerSeller() {
        if (_uiState.value.isLoading) return
        //Este if lo que es hace es verificar si el estado de la pantalla de registro de vendedor es cargando, si esta cargando, esta fun no hara nada.
        // o sea si el ususario presiona el boton de registro varias veces, no hace nada porque ya esta cargando.

        //    HArenos una corrutina sobre el ciclo de vida del viewmodel. Si el viewmodel muere, la corrutina tambien lo hara.
        //  Servira para operaciones asincronas:
        viewModelScope.launch {
            //Primero con trim() eliminamos los espacios en blanco al inicio y al final del texto.
            val firstNameTrimmed = _uiState.value.firstName.trim()
            val lastNameTrimmed = _uiState.value.lastName.trim()
            val emailTrimmed = _uiState.value.email.trim()
            val passwordTrimmed = _uiState.value.password.trim()
            val confirmPasswordTrimmed = _uiState.value.confirmPassword.trim()
            val phoneTrimmed = _uiState.value.phone.trim()

            //            Ahora verificamos que los campos no esten vacios:
            if (firstNameTrimmed.isEmpty()) {
                _effect.emit(SellerEffect.ShowMessage("El nombre no puede estar vacio"))
                return@launch
                // Si el nombre esta vacio se envia el mensaje y se sale de la fun registerSeller() lo que hace que no se haga el registro.
            }

            if (lastNameTrimmed.isEmpty()) {
                _effect.emit(SellerEffect.ShowMessage("El apellido no puede estar vacio"))
                return@launch
                // Si el apellido esta vacio se envia el mensaje y se sale de la fun registerSeller() lo que hace que no se haga el registro.

            }

            if (emailTrimmed.isEmpty()) {
                _effect.emit(SellerEffect.ShowMessage("El correo electronico no puede estar vacio"))
                return@launch
                // Si el correo esta vacio se envia el mensaje y se sale de la fun registerSeller() lo que hace que no se haga el registro.
            }

            if (passwordTrimmed.isEmpty()) {
                _effect.emit(SellerEffect.ShowMessage("La contraseña no puede estar vacia"))
                return@launch
                // Si la contraseña esta vacia se envia el mensaje y se sale de la fun registerSeller() lo que hace que no se haga el registro.
            }

            if (confirmPasswordTrimmed.isEmpty()) {
                _effect.emit(SellerEffect.ShowMessage("La confirmacion de contraseña no puede estar vacia"))
                return@launch
                // Si la confirmacion de contraseña esta vacia se envia el mensaje y se sale de la fun registerSeller(),
                // lo que hace que no se haga el registro.
            }

            if (phoneTrimmed.isEmpty()) {
                _effect.emit(SellerEffect.ShowMessage("El telefono no puede estar vacio"))
                return@launch
                // Si el telefono esta vacio se envia el mensaje y se sale de la fun registerSeller() lo que hace que no se haga el registro.
            }
            //            Condicion que la contraseña y su confirmacion debe ser iiguales:
            if (passwordTrimmed != confirmPasswordTrimmed) {
                _effect.emit(SellerEffect.ShowMessage("Las contraseñas no coinciden"))
                return@launch
                // Si la contraseña no es igual a la confirmacion se envia el mensaje y se sale de la fun registerSeller()
                // lo que hace que no se haga el registro.
            }

            //Ahora actualizamos el estado visual de la pantalla de registro de vendedor para que se muestre que esta cargando:
            _uiState.update { current ->
                //Actualizamos el estado de la pantalla de registro de vendedor, reseteando a su estado inicial:
                current.copy(
                    isLoading = true, // Indica que esta cargando la pantalla de registro de vendedor.
                    errorMessage = null // Borra el mensaje de error si lo habia
                )
            }

            //Vamos a guardar el resdultado del registro, desde el repositorio de autenticacion Firebase,
            // en una var llamada result
            // Llamamos al repositorio de autenticacion Firebase para registrar al vendedor,
//            y le pasamos el nombre, apellido, correo y contraseña que ingreso el usuario.
            // Para eso creamos una variable llamada result que almacena la respuesta
            // a la solicitud de registro del vendedor en el repositorio de autenticacion Firebase:

            val result =
                authRepository.registerUser(
                    firstName = firstNameTrimmed,
                    lastName = lastNameTrimmed,
                    email = emailTrimmed,
                    password = passwordTrimmed,
                    phone = phoneTrimmed,
                    role = UserRole.SELLER
                )
//           Si el registro fue exitoso, el repositorio de autenticacion Firebase en este caso, nos devuelve un usuario autenticado.
//            // que tendra como datos el nombre, apellido, correo y contraseña que ingreso el usuario, osea
//                // firstNameTrimmed, lastNameTrimmed, emailTrimmed y passwordTrimmed.

            // y actulaizamos el estado de la pantalla de registro de vendedor,
            // reseteando a su estado inicial::
            result.onSuccess { sellerUser ->
                _uiState.update { current ->
                    current.copy(
                        firstName = "",
                        lastName = "",
                        email = "",
                        password = "",
                        confirmPassword = "",
                        phone = "",
                        isLoading = false, // Deja de estar cargando la pantalla de registro de vendedor luego de registrarse.
                        errorMessage = null // Borra el mensaje de error si lo habia
                    )
                }

                //                Agregamos un efecto para mostrar un mensaje de registro exitoso:
                _effect.emit(SellerEffect.ShowMessage("La cuenta del vendedor fue creada exitosamente"))

//                Agregamos un efecto para navegar a la pantalla de registro de la tienda,
                //       en que enviaremos el id del vendedor para registrarlo como dueño de la tienda:
                _effect.emit(SellerEffect.NavigateToRegisterScreen(sellerUid = sellerUser.uid))


            }

                //Si el proceso de registro no fue exitoso, el repositorio de autenticacion Firebase en este caso, nos devuelve un error:
                .onFailure { error ->
                    _uiState.update { current ->
                        current.copy( // Actualizamos el estado de la pantalla de registro de vendedor,
                            isLoading = false, // Deja de estar cargando la pantalla de registro de vendedor
                            errorMessage = error.message ?: "No se pudo registrar al vendedor"// Y se muestra un mensaje de error,
                        // que nos devuelve el repositorio de autenticacion Firebase,
                            //pero si ese mensaje es nulo, le ponemos un mensaje por defecto, usando el operador Elvis.
                        )

                    }

                    //Finalmente llamamos a un efecto con un texto que muestre mensaje de arriba o bien un mensaje de error por defecto:
                    _effect.emit(SellerEffect.ShowMessage(error.message ?: "Registro fallido"))
                }
        }
    }
}